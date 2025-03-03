import groovy.json.JsonSlurper

def call(Map config) {
    this.config = config
    node(gcrNode) {
        logger = new Logger()
        echo "Pipeline execution starts"

        miEvent = new MiEvent()
        splunkRequestBody = new SplunkRequestBody()

        stage('Checkout') {
            try {
                cleanWs()
                gitCheckout()

                sh '''
                    touch email_data.txt
                    ls
                    echo "DATE ENVIRONMENT CP DATAPLANE STATUS" >> email_data.txt
                '''

                def date = new Date()
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
                rundate = sdf.format(date)
                echo "Run Date: ${rundate}"

                miEvent.Environment = "ENVIRONMENT"
                miEvent.CP = "CP"
                miEvent.Status = "STATUS"
                miEvent.Dataplane = "DATAPLANE"
                miEvent.Date = "DATE"

                splunkRequestBody.event = miEvent
                splunkRequestBody.sourcetype = sourceType

                def environmentList = "dev-HK,dev-UK,ppd-HK,ppd-UK,prd-HK,prd-UK".split(',') as List

                buildno = env.BUILD_NUMBER
                echo "Build Number: ${buildno}"

                stage("Report Generation Process") {
                    try {
                        def counter = 0
                        def counterWorkspace = 0
                        echo "Processing workspace: ${workspace_name}"

                        for (def envType in environmentList) {
                            String DataPlanename = "GKE-GCP"
                            def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")

                            def config_detail_GKE = libraryResource '../resources/GKE.yaml'
                            writeFile file: 'GKE.yaml', text: config_detail_GKE
                            def configurationGKEYML = readYaml file: "resources/GKE.yaml"

                            ENV_TYPE = envType.trim()
                            echo "Processing Environment: ${ENV_TYPE}"

                            def configGKEYML = configurationGKEYML."${ENV_TYPE}"
                            configGKEYMLAll = configGKEYML

                            echo "Configuration Details: ${configurationGKEYML}"
                            echo "Parsed Config: ${configGKEYML}"
                            echo "All Configs: ${configGKEYMLAll}"

                            def CP = configGKEYML.CP
                            def DP_SHARED = configGKEYML.DP_Shared

                            if (!envType.equals("sbox-HK")) {
                                def DP_CTO = configGKEYML.DP_cto
                                def DP_ET = configGKEYML.DP_et
                                def DP_GDT = configGKEYML.DP_gdt

                                def dplist = [DP_SHARED, DP_CTO, DP_ET, DP_GDT]

                                if (counter < 5000) {
                                    logger.info("Processing CP: ${CP}")
                                    counter++

                                    this.testAuth(CP)

                                    if (workspace_name == "ALL") {
                                        def contentFile = "Workspace Name, Service Count, CP\n"

                                        logger.info("Fetching Workspaces for CP: ${CP}")
                                        WorkSpacesList.clear()
                                        getworkspaces(CP)

                                        for (workspace in WorkSpacesList) {
                                            if (workspace.contains("-")) {
                                                try {
                                                    counterWorkspace++
                                                    echo("Processing Workspace: ${workspace}")

                                                    def serviceCount = this.getworkspaceservices(CP, workspace)
                                                    contentFile += "${workspace},${serviceCount},CP\n"

                                                } catch (Exception e) {
                                                    error("Error fetching workspace services: " + e.getMessage())
                                                }
                                            }
                                        }
                                    } else {
                                        def contentFile = "Workspace Name, Service Count, Service Name, CP\n"
                                        logger.info("Fetching License Info for CP: ${CP}")
                                        getCPLicenceinfo(CP, envType)
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        error("Error during report generation: " + e.getMessage())
                    }
                }

                stage("JSON to CSV Conversion") {
                    processJsonToCsv()
                }

                stage("Send Email Report") {
                    def filename = "Report_Detail.csv"
                    echo "Creating Report File: ${filename}"

                    writeFile file: "./Report/${filename}", text: contentFile
                    echo "Report File Written Successfully"

                    String responseBody = readFile encoding: 'UTF-8', file: "./Report/${filename}"
                    echo "Response Body Read Successfully"

                    String tempWorkspaceName = workspace_name.replace("-DEV", "")
                    echo "Sending Email for Report: Report/${filename}"

                    String message = """
                    Hi Team,

                    This is an auto-generated email for ${tempWorkspaceName}.
                    Please find the attached report for Workspace, Services, Routes, and Plugins.
                    For any queries, please reach out to the team.

                    Thanks
                    """

                    emailext(
                        to: "${email_reciver}",
                        subject: "Admin API Pipeline Report - ${tempWorkspaceName}",
                        attachLog: false,
                        attachmentsPattern: "Report/${filename}",
                        body: message
                    )

                    println "Email sent successfully!"
                }
            }
        }
    }
}

// Function to process multiple JSON files and generate CSV files
def processJsonToCsv() {
    def jsonFilePaths = [
        "dev-HK_license.json", "dev-UK_license.json",
        "ppd-HK_license.json", "ppd-UK_license.json"
    ]

    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData == null) {
            println "JSON file loading failed for: ${filePath}"
        } else {
            println "Successfully loaded JSON file: ${filePath}"
            convertJsonToCsv(jsonData, filePath)
        }
    }
}

// Function to load a JSON file
def loadJsonFile(filePath) {
    try {
        def fileContent = new File(filePath).text  // Read file content
        def jsonData = new JsonSlurper().parseText(fileContent)  // Parse JSON
        return jsonData
    } catch (Exception e) {
        println "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            def environment = new File(filePath).getName().replace(".json", "")

            def servicesCount = jsonData.services_count
            def rbacUsers = jsonData.rbac_users
            def kongVersion = jsonData.kong_version
            def dbVersion = jsonData.db_version
            def uname = jsonData.system_info.uname
            def hostname = jsonData.system_info.hostname
            def cores = jsonData.system_info.cores
            def workspacesCount = jsonData.workspaces_count
            def licenseKey = jsonData.containsKey("license") ? jsonData.license.license_key : "N/A"

            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                           "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion,
                          uname, hostname, cores, workspacesCount, licenseKey]

            def csvContent = headers.join(",") + "\n" + values.join(",")
            def csvFilePath = filePath.replace(".json", ".csv")
            new File(csvFilePath).text = csvContent
            println "CSV file created successfully: ${csvFilePath}"
        } else {
            println "Error: JSON data is null or incorrect format"
        }
    } catch (Exception e) {
        println "Error converting JSON to CSV: ${e.message}"
    }
            }
