import groovy.transform.Field
import java.io.File;
import java.text.SimpleDateFormat
import groovy.util.*
import groovy.json.*
import groovy.lang.*
import groovy.json.JsonSlurper

import com.hsbc.hap.*

@Field String gcrNode = "cm-linux-cjoc"
@Field def logger
@Field Boolean jobFailed = false
@Field String kongDeploymentAccountCredID = "Kong_Deployment_Pipeline_User"
@Field String KONGSandbox_CREDID = "KONGSandbox"
@Field String KONGdev = "KONGdev"

@Field String contentFile = ""
@Field def serviceId
@Field def routeId
@Field def buildno

@Field def configGKEYYMLAll;
@Field def WorkSpacesList = [];
@Field def pluginList = [];
@Field def serviceroutesList = [];
@Field def servicenameandindexMap = [:]

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
                gitcheckout()

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
                processJson()
            } catch (Exception e) {
                error("Error processing JSON files: " + e.getMessage())
            }
        }

        stage("Send Email Notification") {
            try {
                sendEmailNotification()
            } catch (Exception e) {
                error("Error sending email notification: " + e.getMessage())
            }
        }

        stage("Test Authentication") {
            try {
                testAuth("someCPValue") // Replace "someCPValue" with actual value
            } catch (Exception e) {
                error("Error testing authentication: " + e.getMessage())
            }
        }

        stage("Get Plugin ID from Service ID") {
            try {
                getPluginIDFromServiceId("someCPValue", "someServiceId", "someWorkspaceName") // Replace with actual values
            } catch (Exception e) {
                error("Error getting plugin ID: " + e.getMessage())
            }
        }
    }
}

def loadJsonFile(filePath) {
    try {
        def fileContent = libraryResource(filePath)
        def jsonText = fileContent
        def jsonData = new JsonSlurper().parseText(jsonText)

        return jsonData
    } catch (Exception e) {
        echo("Error reading JSON file: ${e.message}")
        return null
    }
}

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
            def licenseKey = jsonData.containsKey("license") ? jsonData.license.license_key : jsonData.license_key

            if (contentFile.isEmpty()) {
                def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                               "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
                contentFile += headers.join(",") + "\n"
            }

            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname,
                          cores, workspacesCount, licenseKey]

            contentFile += values.join(",") + "\n"
        }
    } catch (Exception e) {
        echo("Error converting JSON to CSV: ${e.message}")
    }
}

def processJson() {
    def jsonFilePaths = ["dev-HK_license.json", "dev-UK_license.json", "ppd-HK_license.json",
                         "ppd-UK_license.json", "prd-HK_license.json", "prd-UK_license.json"]

    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData == null) {
            println("JSON file loading failed for: ${filePath}")
        } else {
            println("Successfully loaded JSON file: ${filePath}")
            convertJsonToCsv(jsonData, filePath)
        }
    }

    String filename = "mycsvnew.csv";
    println "Filename for writing: ${filename}";
    println "Filename for content file: ${contentFile}";
    writeFile file: "${filename}", text: "${contentFile}";

    String responseBody = readFile encoding: 'UTF-8', file: "${filename}";
    println "Response body: ${responseBody}";
    println "Read file executed successfully";
}

def sendEmailNotification() {
    String filename = "mycsvnew.csv";
    println "Sending Email to Recipients ${filename}";

    String message = "Hi Team,\n\nThis is an auto-generated mail for "
    message += "Please find the attached report for Workspace, Services, Routes, and Plugins.\n"
    message += "For any queries, please reach out to Team.\n"

    emailext(
        to: "navneet.patidar@noexternalmail.hsbc.com",
        subject: "Admin API Pipeline Report- ",
        attachLog: false,
        attachmentsPattern: "${filename}",
        body: message
    );
}

def testAuth(def CP) {
    withCredentials([
        usernamePassword(
            credentialsId: kongDeploymentAccountCredID,
            usernameVariable: 'USERNAME',
            passwordVariable: 'PASSWORD'
        )
    ]) {
        sh """
            echo "test auth : \${PASSWORD}"
            curl -k -c cookie.txt --location --request GET "https://\${CP}/admin/auth" --header 'kong-admin-user:\${USERNAME}'
        """
    }
}

def getPluginIDFromServiceId(def CP, def passserviceid, def workspacename) {
    echo "getPluginIDFromServiceId"
    def response = ""
    def temppluginList = []

    withCredentials([
        usernamePassword(
            credentialsId: kongDeploymentAccountCredID,
            usernameVariable: 'USERNAME',
            passwordVariable: 'PASSWORD'
        )
    ]) {
        // Placeholder logic for getting plugin ID
        // Add actual implementation based on Kong API response
        echo "Fetching plugin ID for service: \${passserviceid} in workspace: \${workspacename}"
    }
	}
