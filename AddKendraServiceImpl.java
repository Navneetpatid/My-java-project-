import groovy.json.JsonSlurper

def call(Map config) {
    this.config = config
    node(gcrNode) {
        logger = new Logger()
        echo "Pipeline execution started"

        miEvent = new MiEvent()
        splunkRequestBody = new SplunkRequestBody()

        stage('Checkout') {
            try {
                cleanWs()
                gitCheckout()
                sh 'echo "DATE,ENVIRONMENT,CP,DATAPLANE,STATUS" > email_data.txt'

                def date = new Date()
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
                rundate = sdf.format(date)
                echo "rundate:${rundate}"

                def environment = "dev-HK,dev-UK,ppd-HK,ppd-UK,prd-HK,prd-UK"
                buildno = env.BUILD_NUMBER
                echo "buildno:${buildno}"

                def enlist = environment.split(',') as List

                stage("Report Generation Process") {
                    try {
                        def contentFile = "Workspace Name, Service Count, CP\n"
                        def counterworkspace = 0

                        for (def j in enlist) {
                            String DataPlanename = "GKE-GCP"
                            def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")
                            writeFile file: 'GKE.yaml', text: libraryResource('../resources/GKE.yaml')
                            def configurationGKEYML = readYaml file: "resources/GKE.yaml"

                            ENV_TYPE = j.trim()
                            echo "${ENV_TYPE}"

                            def configGKEYML = configurationGKEYML."${ENV_TYPE}"
                            CP = configGKEYML.CP
                            DP_SHARED = configGKEYML.DP_Shared

                            if (!j.equals("sbox-HK")) {
                                DP_CTO = configGKEYML.DP_cto
                                DP_ET = configGKEYML.DP_et
                                DP_GDT = configGKEYML.DP_gdt
                            }

                            if (workspace_name == "ALL") {
                                WorkSpacesList.clear()
                                getworkspaces(CP)

                                for (i = 0; i < WorkSpacesList.size(); i++) {
                                    if (WorkSpacesList[i].contains("-")) {
                                        try {
                                            counterworkspace++
                                            echo "Processing workspace ${WorkSpacesList[i]} (${counterworkspace})"
                                            def servicecount = getworkspaceservices(CP, WorkSpacesList[i])
                                            contentFile += "${WorkSpacesList[i]},${servicecount},CP\n"
                                        } catch (Exception e) {
                                            error("Error fetching workspace services: ${e.getMessage()}")
                                        }
                                    }
                                }
                            }
                        }

                        // Save workspace report
                        String workspaceReport = "Report/Workspace_Report.csv"
                        writeFile file: workspaceReport, text: contentFile
                        echo "Workspace report generated: ${workspaceReport}"

                        // Process license reports
                        def licenseFiles = [
                            "dev-HK_license.json", "dev-UK_license.json",
                            "ppd-HK_license.json", "ppd-UK_license.json"
                        ]

                        licenseFiles.each { filePath ->
                            def jsonData = loadJsonFile(filePath)
                            if (jsonData) {
                                convertJsonToCsv(jsonData, filePath)
                            } else {
                                echo "Skipping ${filePath} due to errors."
                            }
                        }

                        // Send Email
                        def licenseReport = "Report/dev-HK_license.csv" // Assuming this is generated
                        def emailMessage = """
                            Hi Team,

                            Please find attached reports for Workspace and License data.

                            Best regards,
                        """

                        emailext(
                            to: "navneet.patidar@noexternalmail.hsbc.com",
                            subject: "Admin API Pipeline & License Report",
                            attachLog: false,
                            attachmentsPattern: "Report/*.csv",
                            body: emailMessage
                        )

                        echo "Email sent successfully with reports"

                    } catch (Exception e) {
                        error("Error during report generation: ${e.getMessage()}")
                    }
                }
            }
        }
    }
}

// Function to load JSON
def loadJsonFile(filePath) {
    try {
        def fileContent = new File(filePath).text
        return new JsonSlurper().parseText(fileContent)
    } catch (Exception e) {
        echo "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            def environment = new File(filePath).getName().replace(".json", "")

            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                           "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]

            def values = [
                environment, jsonData.services_count, jsonData.rbac_users,
                jsonData.kong_version, jsonData.db_version,
                jsonData.system_info.uname, jsonData.system_info.hostname,
                jsonData.system_info.cores, jsonData.workspaces_count,
                jsonData.containsKey("license") ? jsonData.license.license_key : "N/A"
            ]

            def csvContent = headers.join(",") + "\n" + values.join(",")

            def csvFilePath = "Report/" + filePath.replace(".json", ".csv")
            writeFile file: csvFilePath, text: csvContent
            echo "CSV report created: ${csvFilePath}"
        } else {
            echo "Skipping JSON conversion due to null data."
        }
    } catch (Exception e) {
        echo "Error converting JSON to CSV: ${e.message}"
    }
                    }
