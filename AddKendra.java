import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths

def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            // Extract environment name from file path
            def environment = new File(filePath).getName().replace(".json", "")

            // Extract values
            def servicesCount = jsonData.services_count
            def rbacUsers = jsonData.rbac_users
            def kongVersion = jsonData.kong_version
            def dbVersion = jsonData.db_version
            def uname = jsonData.system_info.uname
            def hostname = jsonData.system_info.hostname
            def cores = jsonData.system_info.cores
            def workspacesCount = jsonData.workspaces_count
            def licenseKey = jsonData.license_key

            def csvFile = "output.csv"
            def fileExists = new File(csvFile).exists()

            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version", "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname, cores, workspacesCount, licenseKey]

            def output = new StringBuilder()

            if (!fileExists) {
                output.append(headers.join(",") + "\n")
            }

            output.append(values.join(",") + "\n")

            // Write to CSV file
            Files.write(Paths.get(csvFile), output.toString().getBytes(), fileExists ? StandardOpenOption.APPEND : StandardOpenOption.CREATE)

            println "CSV data saved to: ${csvFile}"
        } else {
            println "Error: JSON data is null or incorrect format"
        }
    } catch (Exception e) {
        println "Error processing JSON: ${e.message}"
    }
}

// Function to process multiple JSON files
def processJson() {
    def jsonFilePaths = ["dev-HK_license.json", "dev-UK_license.json", "ppd-HK_license.json", "ppd-UK_license.json"]

    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData != null) {
            convertJsonToCsv(jsonData, filePath)
        } else {
            println "JSON file loading failed for: ${filePath}"
        }
    }
}

// Function to load JSON file
def loadJsonFile(filePath) {
    try {
        def file = new File(filePath)
        if (file.exists()) {
            return new JsonSlurper().parse(file)
        }
    } catch (Exception e) {
        println "Error reading JSON file ${filePath}: ${e.message}"
    }
    return null
}

// Run the process
processJson()
