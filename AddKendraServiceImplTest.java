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
        echo "Starting process..."

        miEvent = new MiEvent()
        splunkRequestBody = new SplunkRequestBody()
        
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
                echo "Build Number: ${buildno}"
                
                envlist = environment.split(',') as List
            } catch (Exception e) {
                error("Error during checkout: " + e.getMessage())
            }
        } 
        
        stage("Report Generation Process") {
            try {
                def counter = 0
                def counterworkspace = 0

                for (def j in envlist) {
                    def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")
                    def configurationGKEYML = readYaml file: "resources/GKE.yaml"
                    
                    ENV_TYPE = j.trim()
                    echo "Processing Environment: ${ENV_TYPE}"
                    
                    def configGKEYML = configurationGKEYML."${ENV_TYPE}"
                    CP = configGKEYML.CP
                    DP_SHARED = configGKEYML.DP_Shared
                    
                    if (!j.equals("sbox-HK")) {
                        DP_CTO = configGKEYML.DP_cto
                        DP_ET = configGKEYML.DP_et
                        DP_GDT = configGKEYML.DP_gdt
                    }

                    def dplist1 = [DP_SHARED, DP_CTO, DP_ET, DP_GDT]

                    if (counter < 5000) {
                        counter++
                        this.testAuth(CP)

                        if (workspace_name == "ALL") {
                            if (contentFile == "") {
                                contentFile = "Workspace Name, Service Count, CP\n"
                            }

                            WorkSpacesList.clear()
                            getworkspaces(CP)

                            for (i in 0..<WorkSpacesList.size()) {
                                if (WorkSpacesList[i].contains("-")) {
                                    try {
                                        counterworkspace++
                                        def servicecount = this.getworkspaceservices(CP, WorkSpacesList[i])
                                        contentFile += "${WorkSpacesList[i]},${servicecount},${CP}\n"
                                    } catch (Exception e) {
                                        error("Error fetching services: " + e.getMessage())
                                    }
                                }
                            }
                        } else {
                            if (contentFile == "") {
                                contentFile = "Workspace Name, Service Count, Service Name, CP\n"
                            }
                            getCPLicenceinfo(CP, j)
                        }
                    }
                }
            } catch (Exception e) {
                error("Error generating report: " + e.getMessage())
            }
        }
        
        stage("License Report Processing") {
            try {
                def jsonFilePaths = ["dev-HK_license.json", "dev-UK_license.json", "ppd-HK_license.json", "ppd-UK_license.json"]

                jsonFilePaths.each { filePath ->
                    def jsonData = loadJsonFile(filePath)
                    if (jsonData) {
                        convertJsonToCsv(jsonData, filePath)
                    }
                }
            } catch (Exception e) {
                error("Error processing license report: " + e.getMessage())
            }
        }

        stage("Finalizing & Emailing Report") {
            try {
                String filename = "Report_Detail.csv"
                writeFile file: "./Report/${filename}", text: contentFile
                echo "CSV file created: ${filename}"

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

                echo "Email sent successfully!"
            } catch (Exception e) {
                error("Error in finalizing report: " + e.getMessage())
            }
        }
    }
}

// Function to load a JSON file
def loadJsonFile(filePath) {
    try {
        def fileContent = new File(filePath).text
        return new JsonSlurper().parseText(fileContent)
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

            def csvContent = "Environment,Services_Count,RBAC_Users,Kong_Version,DB_Version,Uname,Hostname,Cores,Workspaces_Count,License_Key\n"
            csvContent += "${environment},${servicesCount},${rbacUsers},${kongVersion},${dbVersion},${uname},${hostname},${cores},${workspacesCount},${licenseKey}\n"

            new File(filePath.replace(".json", ".csv")).text = csvContent
            println "CSV created successfully for ${filePath}"
        }
    } catch (Exception e) {
        println "Error converting JSON to CSV: ${e.message}"
    }
  }
