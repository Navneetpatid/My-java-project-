import groovy.transform.Field
import groovy.json.JsonSlurper
import java.io.File
import java.text.SimpleDateFormat

@Field String gcrNode = "cm-l1linux-cjoc"
@Field def logger
@Field Boolean jobFailed = false
@Field String KONGSandbox_CREDID = "KONGSandbox"
@Field String KONGDev = "KONGdev"
@Field String contentFile = ""
@Field def WorkSpacesList = []
@Field def pluginList = []
@Field def serviceroutesList = []
@Field def servicenameandindexMap = [:]

def call(Map config) {
    this.config = config
    node(gcrNode) {
        logger = new Logger()
        echo "Starting process..."

        def miEvent = new MIEvent()
        def splunkRequestBody = new SplunkRequestBody()

        stage('Checkout') {
            try {
                cleanWs()
                gitCheckout()

                sh '''
                ls
                echo "DATE,ENVIRONMENT,CP,DATAPLANE,STATUS" >> email_data.txt
                '''

                def date = new Date()
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
                String runDate = sdf.format(date)
                echo "Run Date: ${runDate}"

                miEvent.Environment = "ENVIRONMENT"
                miEvent.CP = "CP"
                miEvent.Status = "STATUS"
                miEvent.Dataplane = "DATAPLANE"
                miEvent.Date = runDate

                splunkRequestBody.event = miEvent
                splunkRequestBody.sourcetype = sourceType

                def environment = "dev-HK,dev-UK,ppd-HK,ppd-UK,prd-HK,prd-UK"
                def DP_CTO, DP_ET, DP_GDT, DP_SHARED, CP

                buildno = env.BUILD_NUMBER
                echo "Build Number: ${buildno}"

                def envlist = environment.split(',') as List

            } catch (Exception e) {
                echo "Error during checkout: " + e.getMessage()
                jobFailed = true
            }
        }

        stage("Report Generation Process") {
            try {
                int counter = 0
                def myFinalOutput = ""

                for (def j in envlist) {
                    String Dataplanename = "GKE-GCP"

                    def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")
                    def config_detail_GKE = libraryResource '../resources/GKE.yaml'
                    writeFile file: 'GKE.yaml', text: config_detail_GKE

                    def configurationGKEYML = readYaml file: "resources/GKE.yaml"
                    def ENV_TYPE = "${j}".trim()
                    echo "Processing Environment: ${ENV_TYPE}"

                    def configGKEYML = configurationGKEYML."${ENV_TYPE}"
                    echo "Printing configGKEYML: ${configGKEYML}"

                    def configGKEYMLAll1 = configGKEYML
                    echo "Printing configGKEYMLAll1: ${configGKEYMLAll1}"

                    CP = configGKEYML.CP
                    DP_SHARED = configGKEYML.DP_Shared

                    if (!j.equals("sbox-HK")) {
                        DP_CTO = configGKEYML.DP_cto
                        DP_ET = configGKEYML.DP_et
                        DP_GDT = configGKEYML.DP_gdt
                    }

                    def dplist1 = ["${DP_SHARED}", "${DP_CTO}", "${DP_ET}", "${DP_GDT}"]

                    if (counter < 5000) {
                        counter++
                        this.testAuth(CP)

                        if (workspace_name == "ALL") {
                            if (myFinalOutput.isEmpty()) {
                                def headers = [
                                    "Environment", "Services_Count", "RBAC_Users", "Kong_Version", 
                                    "DB_Version", "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"
                                ]
                                myFinalOutput += headers.join(",") + "\n"
                            }

                            WorkSpacesList.clear()
                            getworkspaces(CP)

                            for (i in 0..<WorkSpacesList.size()) {
                                if (WorkSpacesList[i].contains("-")) {
                                    try {
                                        counterworkspace++
                                        echo "Writing for workspace " + WorkSpacesList[i] + " $i counterworkspace ${counterworkspace}"

                                        def jsonData = new JsonSlurper().parseText(libraryResource("workspace_data.json"))
                                        def servicesCount = jsonData.services_count
                                        def rbacUsers = jsonData.rbac_users
                                        def kongVersion = jsonData.kong_version
                                        def dbVersion = jsonData.db_version
                                        def uname = jsonData.system_info.uname
                                        def hostname = jsonData.system_info.hostname
                                        def cores = jsonData.system_info.cores
                                        def workspacesCount = jsonData.workspaces_count
                                        def licenseKey = jsonData.containsKey("license") ? jsonData.license.license_key : "N/A"

                                        def values = [
                                            ENV_TYPE, servicesCount, rbacUsers, kongVersion, 
                                            dbVersion, uname, hostname, cores, workspacesCount, licenseKey
                                        ]
                                        myFinalOutput += values.join(",") + "\n"

                                    } catch (Exception e) {
                                        echo "Error processing workspace: " + e.getMessage()
                                    }
                                }
                            }
                        }
                    }
                }

                // Write to CSV File
                def csvFilePath = "./Report/Report_Detail.csv"
                new File("./Report").mkdirs()
                new File(csvFilePath).withWriter { writer ->
                    writer.write(myFinalOutput)
                }

                echo "CSV file successfully created: ${csvFilePath}"

            } catch (Exception e) {
                echo "Error during report generation: " + e.getMessage()
                jobFailed = true
            }
        }

        // Finalizing & Emailing Report
        stage("Finalizing & Emailing Report") {
            try {
                String filename = "Report_Detail.csv"
                String responseBody = readFile encoding: 'UTF-8', file: "./Report/${filename}"
                echo "Report Content: ${responseBody}"

                String tempworkspacename1 = workspace_name.replace("-DEV", "")
                echo "Sending Email for: ${tempworkspacename1}"

                String message = """
                Hi Team,

                This is an auto-generated mail for ${tempworkspacename1}.
                Please find the attached report for Workspace, Services, Routes, and Plugins.
                For any queries, please reach out to the team.

                Thanks
                """

                emailext(
                    to: "navneet.patidar@noexternalmail.hsbc.com",
                    subject: "Admin API Pipeline Report - ${tempworkspacename1}",
                    attachLog: false,
                    attachmentsPattern: "Report/${filename}",
                    body: message
                )
            } catch (Exception e) {
                echo "Error sending email: " + e.getMessage()
            }
        }
    }
    }
