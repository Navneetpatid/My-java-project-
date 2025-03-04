import groovy.transform.Field
import groovy.json.JsonSlurper
import java.text.SimpleDateFormat

// Global Variables
@Field String gcrNode = "cm-linux-cjoc"
@Field def logger
@Field String contentFile = ""
@Field def WorkSpacesList = []
@Field def email_recipient = "navneet.patidar@noexternalmail.hsbc.com"
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

                def date = new Date()
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
                def rundate = sdf.format(date)
                echo "Run Date: ${rundate}"
            } catch (Exception e) {
                error("Error during checkout: " + e.getMessage())
            }
        }

        stage("Process JSON & Generate CSV") {
            try {
                processJsonFiles()
            } catch (Exception e) {
                error("Error processing JSON files: " + e.getMessage())
            }
        }

        stage("Send Report via Email") {
            try {
                sendEmailWithCSV()
            } catch (Exception e) {
                error("Error sending email: " + e.getMessage())
            }
        }
    }
}

// Function to process JSON files and generate CSV
def processJsonFiles() {
    def jsonFilePaths = [
        "dev-HK_license.json", "dev-UK_license.json", 
        "ppd-HK_license.json", "ppd-UK_license.json"
    ]

    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData) {
            convertJsonToCsv(jsonData, filePath)
        } else {
            echo "Skipping file due to load failure: ${filePath}"
        }
    }
}

// Function to load JSON file
def loadJsonFile(String filePath) {
    try {
        def file = new File(filePath)
        if (!file.exists()) {
            echo "File not found: ${filePath}"
            return null
        }

        def fileContent = file.text
        return new JsonSlurper().parseText(fileContent)
    } catch (Exception e) {
        echo "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, String filePath) {
    try {
        def environment = new File(filePath).getName().replace(".json", "")
        def servicesCount = jsonData.services_count ?: 0
        def rbacUsers = jsonData.rbac_users ?: 0
        def kongVersion = jsonData.kong_version ?: "Unknown"
        def dbVersion = jsonData.db_version ?: "Unknown"
        def uname = jsonData.system_info?.uname ?: "Unknown"
        def hostname = jsonData.system_info?.hostname ?: "Unknown"
        def cores = jsonData.system_info?.cores ?: "Unknown"
        def workspacesCount = jsonData.workspaces_count ?: 0
        def licenseKey = jsonData.license?.license_key ?: "N/A"

        def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                       "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
        def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion,
                      uname, hostname, cores, workspacesCount, licenseKey]

        def csvContent = headers.join(",") + "\n" + values.join(",")
        def csvFilePath = filePath.replace(".json", ".csv")

        new File(csvFilePath).text = csvContent
        echo "CSV file created successfully: ${csvFilePath}"
    } catch (Exception e) {
        echo "Error converting JSON to CSV: ${e.message}"
    }
}

// Function to send email with the generated CSV file
def sendEmailWithCSV() {
    def csvFiles = ["dev-HK_license.csv", "dev-UK_license.csv", "ppd-HK_license.csv", "ppd-UK_license.csv"]
    def attachmentPattern = csvFiles.findAll { new File(it).exists() }.join(",")

    if (attachmentPattern) {
        emailext(
            to: email_recipient,
            subject: "Kong License Report",
            attachLog: false,
            attachmentsPattern: attachmentPattern,
            body: "Hi Team,\n\nPlease find attached the latest Kong License Report.\n\nThanks."
        )
        echo "Email sent successfully with attachments: ${attachmentPattern}"
    } else {
        echo "No CSV files found to attach."
    }
}
