import groovy.transform.Field
import java.io.File
import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

// Define global variables
@Field String gcrNode = "cm-linux-cjoc"
@Field def logger
@Field Boolean jobFailed = false
@Field String kongDeploymentAccountCredID = "Kong_Deployment_Pipeline_User"
@Field String KONGSandbox_CREDID = "KONGSandbox"
@Field String KONGdev = "KONGdev"

// Define lists and maps
@Field String contentFile = ""
@Field def WorkSpacesList = []
@Field def rundate
@Field String sourceType = "HAP_JENKINS_KONG"

def call(Map config) {
    this.config = config
    node(gcrNode) {
        logger = new Logger()
        echo "Pipeline Execution Started"

        stage('Checkout') {
            try {
                cleanWs()
                gitCheckout()
                sh '''
                    >> email_data.txt
                    ls
                    echo "DATE,ENVIRONMENT,CP,DATAPLANE,STATUS" >> email_data.txt
                '''
                
                def date = new Date()
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
                rundate = sdf.format(date)
                echo "Run Date: ${rundate}"

                def environmentList = ["dev-HK", "dev-UK", "ppd-HK", "ppd-UK", "prd-HK", "prd-UK"]
                def buildNo = env.BUILD_NUMBER
                echo "Build Number: ${buildNo}"

                // Process JSON files for License Data
                processJson(environmentList)
            } catch (Exception e) {
                error("Error during Checkout stage: " + e.getMessage())
            }
        }

        stage("Report Generation Process") {
            try {
                def counter = 0
                def counterworkspace = 0
                
                for (def j in ["dev-HK", "dev-UK", "ppd-HK", "ppd-UK"]) {
                    echo "Processing Environment: ${j}"
                    
                    def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")
                    def configurationGKEYML = readYaml file: "resources/GKE.yaml"

                    def configGKEYML = configurationGKEYML."${j}"
                    def CP = configGKEYML.CP
                    def DP_SHARED = configGKEYML.DP_Shared

                    def dplist1 = ["${DP_SHARED}", configGKEYML.DP_cto, configGKEYML.DP_et, configGKEYML.DP_gdt]

                    if (counter < 5000) {
                        counter++
                        testAuth(CP)

                        if (workspace_name == "ALL") {
                            if (contentFile == "")
                                contentFile = "Workspace Name,Service Count,CP\n"

                            WorkSpacesList.clear()
                            getworkspaces(CP)

                            for (i = 0; i < WorkSpacesList.size(); i++) {
                                if (WorkSpacesList[i].contains("-")) {
                                    try {
                                        counterworkspace++
                                        def servicecount = getworkspaceservices(CP, "${WorkSpacesList[i]}")
                                        contentFile += "${WorkSpacesList[i]},${servicecount},CP\n"
                                    } catch (Exception e) {
                                        error("Error fetching workspace services: " + e.getMessage())
                                    }
                                }
                            }
                        } else {
                            getCPLicenceinfo("${CP}", "${j}")
                        }
                    }
                }
            } catch (Exception e) {
                error("Error during Report Generation: " + e.getMessage())
            }
        }

        stage("Generate & Send CSV Report") {
            try {
                String filename = "Report_Detail.csv"
                writeFile file: "./Report/${filename}", text: contentFile
                echo "CSV File Generated: ${filename}"

                String responseBody = readFile encoding: 'UTF-8', file: "./Report/${filename}"
                echo "CSV Content Preview:\n${responseBody}"

                sendEmail(filename)
            } catch (Exception e) {
                error("Error generating CSV report: " + e.getMessage())
            }
        }
    }
}

// Function to process JSON files and generate CSV data
def processJson(List environmentList) {
    def jsonFilePaths = environmentList.collect { "${it}_license.json" }

    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData) {
            convertJsonToCsv(jsonData, filePath)
        } else {
            println "Skipping file due to error: ${filePath}"
        }
    }
}

// Function to load JSON file
def loadJsonFile(filePath) {
    try {
        def fileContent = new File(filePath).text
        return new JsonSlurper().parseText(fileContent)
    } catch (Exception e) {
        println "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to convert JSON to CSV format
def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            def environment = new File(filePath).getName().replace(".json", "")

            def servicesCount = jsonData.services_count ?: 0
            def rbacUsers = jsonData.rbac_users ?: 0
            def kongVersion = jsonData.kong_version ?: "N/A"
            def dbVersion = jsonData.db_version ?: "N/A"
            def uname = jsonData.system_info.uname ?: "N/A"
            def hostname = jsonData.system_info.hostname ?: "N/A"
            def cores = jsonData.system_info.cores ?: 0
            def workspacesCount = jsonData.workspaces_count ?: 0
            def licenseKey = jsonData.containsKey("license") ? jsonData.license.license_key : "N/A"

            if (contentFile.isEmpty()) {
                contentFile = "Environment,Services_Count,RBAC_Users,Kong_Version,DB_Version,Uname,Hostname,Cores,Workspaces_Count,License_Key\n"
            }

            contentFile += "${environment},${servicesCount},${rbacUsers},${kongVersion},${dbVersion},${uname},${hostname},${cores},${workspacesCount},${licenseKey}\n"
            println "Data added for environment: ${environment}"
        }
    } catch (Exception e) {
        println "Error converting JSON to CSV: ${e.message}"
    }
}

// Function to send an email with the CSV attachment
def sendEmail(String filename) {
    String message = """\
        Hi Team,

        This is an auto-generated email.

        Please find attached the latest license report.

        Best regards.
    """.stripIndent()

    emailext(
        to: "navneet.patidar@noexternalmail.hsbc.com",
        subject: "Admin API Pipeline Report",
        attachLog: false,
        attachmentsPattern: "Report/${filename}",
        body: message
    )

    println "Email sent successfully with attachment: ${filename}"
	}
