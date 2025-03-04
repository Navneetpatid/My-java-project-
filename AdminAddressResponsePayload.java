import groovy.transform.Field
import java.io.File
import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

// Define global variables with @Field annotation
@Field String gcrNode = "cm-linux-cjoc"
@Field def logger
@Field Boolean jobFailed = false
@Field String kongDeploymentAccountCredID = "Kong_Deployment_Pipeline_User"
@Field String KONGSandbox_CREDID = "KONGSandbox"
@Field String KONGdev = "KONGdev"

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
        echo "Starting process..."

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
                echo "Run date: ${rundate}"

                def environment = "dev-HK,dev-UK,ppd-HK,ppd-UK,prd-HK,prd-UK"

                buildno = env.BUILD_NUMBER
                echo "Build number: ${buildno}"

                envlist = environment.split(',') as List
            } catch (Exception e) {
                error("Error while fetching gcloud output response: " + e.getMessage())
            }
        }

        stage("Report Generation Process") {
            try {
                def counter = 0
                def counterworkspace = 0

                for (def j in envlist) {
                    String DataPlanename = "GKE-GCP"
                    def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")

                    ENV_TYPE = j.trim()
                    echo "Processing Environment: ${ENV_TYPE}"

                    def configGKEYML = configDetailsGKEYaml."${ENV_TYPE}"
                    def CP = configGKEYML.CP
                    def DP_SHARED = configGKEYML.DP_Shared

                    if (!j.equals("sbox-HK")) {
                        def DP_CTO = configGKEYML.DP_cto
                        def DP_ET = configGKEYML.DP_et
                        def DP_GDT = configGKEYML.DP_gdt
                        def dplist1 = [DP_SHARED, DP_CTO, DP_ET, DP_GDT]

                        if (counter < 5000) {
                            counter += 1
                            this.testAuth(CP)

                            if (workspace_name == "ALL") {
                                if (contentFile == "") {
                                    contentFile = "Workspace Name, Service Count, CP\n"
                                }

                                WorkSpacesList.clear()
                                getworkspaces(CP)

                                for (i = 0; i < WorkSpacesList.size(); i++) {
                                    if (WorkSpacesList[i].contains("-")) {
                                        try {
                                            counterworkspace += 1
                                            def servicecount = this.getworkspaceservices(CP, WorkSpacesList[i])

                                            contentFile += "${WorkSpacesList[i]},${servicecount},${CP}\n"

                                        } catch (Exception e) {
                                            error("Error while fetching workspace services: " + e.getMessage())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                error("Error while generating report: " + e.getMessage())
            }

            String filename = "Report_Detail.csv"

            writeFile file: "./Report/${filename}", text: "${contentFile}"
            echo "Report written successfully."

            String responseBody = readFile encoding: 'UTF-8', file: "./Report/${filename}"
            echo "Response Body: ${responseBody}"

            sendEmailReport(filename)
        }

        // New Stage for JSON Parsing
        stage("Read JSON Response") {
            try {
                def jsonFilePath = "path/to/your/jsonfile.json" // Update with actual JSON file path
                def jsonContent = readFile(file: jsonFilePath)
                def jsonSlurper = new JsonSlurper()
                def jsonData = jsonSlurper.parseText(jsonContent)

                echo "JSON Data: ${jsonData}"

                // Example: Extract specific values
                def exampleValue = jsonData.someKey // Replace with actual key from JSON
                echo "Extracted Value: ${exampleValue}"

                // Process JSON Data further if needed
                jsonData.each { key, value ->
                    echo "Key: ${key}, Value: ${value}"
                }

            } catch (Exception e) {
                error("Error while reading JSON: " + e.getMessage())
            }
        }
    }
}

// Function to send email report
def sendEmailReport(String filename) {
    String tempworkspacename1 = "${workspace_name}".replace("-DEV", "")
    String message = """Hi Team,

This is an auto-generated mail for ${tempworkspacename1}.
Please find the attached report for Workspace, Services, Routes, and Plugins.
For any queries, please reach out to the team.

Thanks
"""

    emailext to: "${email_reciver}",
        subject: "Admin API Pipeline Report - ${tempworkspacename1}",
        attachLog: false,
        attachmentsPattern: "Report/${filename}",
        body: message
  }
