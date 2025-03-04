import groovy.transform.Field
import java.io.File
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import java.text.SimpleDateFormat

// Define global variables with @Field annotation
@Field String gcrNode = "cm-linux-cjoc"
@Field def logger
@Field Boolean jobFailed = false
@Field String kongDeploymentAccountCredID = "Kong_Deployment_Pipeline_User"
@Field String KONGSandbox_CREDID = "KONGSandbox"
@Field String KONGdev = "KONGdev"

// Define lists and maps
@Field def serviceId
@Field def routeId
@Field def buildNo
@Field String contentFile = ""
@Field def configKEYMAIL1
@Field def WorkSpacesList = []
@Field def pluginList = []
@Field def serviceroutesList = []
@Field def servicenameandindexMap = [:]
@Field def rundate
@Field String sourceType = "HAP_JENKINS_KONG"

def call(Map config) {
    this.config = config
    node(gcrNode) {
        logger = new Logger()
        echo "sandip starts"

        miEvent = new MiEvent()
        splunkRequestBody = new SplunkRequestBody()

        stage('Checkout') {
            try {
                cleanWs()
                gitCheckout()
                sh '''
                    >> email_data.txt
                    ls
                    echo "DATE ENVIRONMENT CP DATAPLANE STATUS" >> email_data.txt
                '''

                def date = new Date()
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
                rundate = sdf.format(date)
                echo "rundate:${rundate}"

                miEvent.Environment = "ENVIRONMENT"
                miEvent.CP = "CP"
                miEvent.Status = "STATUS"
                miEvent.Dataplane = "DATAPLANE"
                miEvent.Date = "DATE"

                splunkRequestBody.event = miEvent
                splunkRequestBody.sourcetype = sourceType

                def environment = "dev-HK,dev-UK,ppd-HK,ppd-UK,prd-HK,prd-UK"
                def DP_CTO, DP_ET, DP_GDT, DP_SHARED, CP
                buildno = env.BUILD_NUMBER
                echo "buildno:${buildno}"

                envlist = "${environment}".split(',') as List
            } catch (Exception e) {
                error("Error while fetching gcloud output response: " + e.getMessage())
            }
        }

        stage("Report Generation Process") {
            try {
                def counter = 0
                def counterworkspace = 0
                echo "pass workspace name ${workspace_name}"

                for (def j in envlist) {
                    String DataPlanename = "GKE-GCP"
                    def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")

                    def config_detail_GKE = libraryResource '../resources/GKE.yaml'
                    writeFile file: 'GKE.yaml', text: config_detail_GKE
                    def configurationGKEYML = readYaml file: "resources/GKE.yaml"

                    ENV_TYPE = "${j}".trim()
                    echo "${ENV_TYPE}"

                    def configGKEYML = configurationGKEYML."${ENV_TYPE}"
                    configGKEYMLAll = configGKEYML

                    echo "printing ${configurationGKEYML}"
                    echo "Printing configGKEYML: ${configGKEYML}"
                    echo "Printing configGKEYMLAll: ${configGKEYMLAll}"

                    CP = configGKEYML.CP
                    DP_SHARED = configGKEYML.DP_Shared

                    if (!j.equals("sbox-HK")) {
                        DP_CTO = configGKEYML.DP_cto
                        DP_ET = configGKEYML.DP_et
                        DP_GDT = configGKEYML.DP_gdt

                        def dplist1 = ["${DP_SHARED}", "${DP_CTO}", "${DP_ET}", "${DP_GDT}"]

                        if (counter < 5000) {
                            logger.info("********** Fetching Workspaces ************** - ${CP}")
                            counter = counter + 1

                            logger.info("********** Authenticating ************** ${counter}")
                            this.testAuth(CP)

                            if (workspace_name == "ALL") {
                                if (contentFile == "")
                                    contentFile = "Workspace Name, Service Count, CP\n"

                                logger.info("********** Fetching Workspaces ************** - ${CP}")
                                WorkSpacesList.clear()
                                getworkspaces(CP)

                                for (i = 0; i < WorkSpacesList.size(); i++) {
                                    if (WorkSpacesList[i].contains("-")) {
                                        try {
                                            counterworkspace = counterworkspace + 1
                                            echo("Processing workspace " + WorkSpacesList[i] + " ${i} count: ${counterworkspace}")

                                            def servicecount = this.getworkspaceservices(CP, "${WorkSpacesList[i]}")
                                            contentFile += WorkSpacesList[i] + "," + servicecount + "," + "CP" + "\n"

                                        } catch (Exception e) {
                                            error("Error fetching workspace data: " + e.getMessage())
                                        }
                                    }
                                }
                            } else {
                                if (contentFile == "") {
                                    contentFile = "Workspace Name,Service Count,Service Name, CP\n"
                                }
                                getCPLicenceinfo("${CP}", "${j}")
                            }
                        }
                    }
                }
            } catch (Exception e) {
                error("Error during report generation: " + e.getMessage())
            }

            String filename = "Report_Detail.csv"
            echo "Writing report to: ./Report/${filename}"

            writeFile file: "./Report/${filename}", text: "${contentFile}"
            echo "Report file written successfully."

            // **NEW: Save JSON data in /resources folder**
            try {
                def jsonData = [
                    "date": rundate,
                    "environment": envlist,
                    "cp": CP,
                    "workspaces": WorkSpacesList,
                    "report": contentFile
                ]

                def jsonString = JsonOutput.prettyPrint(JsonOutput.toJson(jsonData))
                String jsonFilename = "./resources/report_data.json"

                // Ensure directory exists
                sh "mkdir -p ./resources"

                writeFile file: jsonFilename, text: jsonString
                echo "JSON data successfully written to: ${jsonFilename}"
            } catch (Exception e) {
                error("Error writing JSON data: " + e.getMessage())
            }

            // **Email Notification**
            String tempworkspacename1 = "${workspace_name}".replace("-DEV", "")
            String message = """Hi Team,

This is an auto-generated mail for ${tempworkspacename1}.
Please find the attached report for Workspaces, Services, Routes, and Plugins.

For any queries, please reach out to the team.

Thanks
"""

            emailext to: "${email_reciver}",
                subject: "Admin API Pipeline Report - ${tempworkspacename1}",
                attachLog: false,
                attachmentsPattern: "Report/${filename}",
                body: message
        }
    }
    }
