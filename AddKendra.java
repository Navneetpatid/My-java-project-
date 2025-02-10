import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            println "Processing JSON file: ${filePath}"

            // Extract environment name
            def environment = new File(filePath).getName().replace(".json", "")

            // Extract values safely
            def servicesCount = jsonData.services_count ?: 0
            def rbacUsers = jsonData.rbac_users?.toString()?.replace(",", ";") ?: "N/A"
            def kongVersion = jsonData.kong_version ?: "Unknown"
            def dbVersion = jsonData.db_version ?: "Unknown"
            def uname = jsonData.system_info?.uname ?: "Unknown"
            def hostname = jsonData.system_info?.hostname ?: "Unknown"
            def cores = jsonData.system_info?.cores ?: 0
            def workspacesCount = jsonData.workspaces_count ?: 0
            def licenseKey = jsonData.license_key ?: "N/A"

            // Get parent directory of the JSON file (resources folder)
            def parentDir = new File(filePath).parent
            def csvFile = "${parentDir}/output.csv"
            def file = new File(csvFile)
            def fileExists = file.exists()

            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version", "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname, cores, workspacesCount, licenseKey]

            // Append data to CSV file safely
            file.withWriterAppend { writer ->
                if (!fileExists) {
                    writer.writeLine(headers.join(","))
                }
                writer.writeLine(values.join(","))
            }

            println "CSV data saved successfully at: ${csvFile}"
        } else {
            println "Error: JSON data is null or incorrect format"
        }
    } catch (Exception e) {
        println "Error processing JSON file '${filePath}': ${e.message}"
    }
}

// Function to process multiple JSON files
def processJson() {
    def resourceDir = "resources" // Change this to your actual path if needed
    def jsonFilePaths = new File(resourceDir).listFiles()?.findAll { it.name.endsWith(".json") }?.collect { it.path } ?: []

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
            println "Loading JSON file: ${filePath}"
            return new JsonSlurper().parse(file)
        } else {
            println "File not found: ${filePath}"
        }
    } catch (Exception e) {
        println "Error reading JSON file '${filePath}': ${e.message}"
    }
    return null
}

// Run the process
processJson()
