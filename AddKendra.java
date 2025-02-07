import groovy.json.JsonSlurper
import java.nio.file.*

def loadJsonFile(filePath) {
    try {
        def fileContent = libraryResource(filePath) // Read JSON file
        def jsonData = new JsonSlurper().parseText(fileContent)
        return jsonData
    } catch (Exception e) {
        echo "Error reading JSON file: ${e.message}"
        return null
    }
}

def processJsonFiles(folderPath) {
    try {
        def folder = new File(folderPath)
        if (!folder.exists() || !folder.isDirectory()) {
            echo "Error: Specified folder does not exist or is not a directory."
            return
        }

        def jsonFiles = folder.listFiles().findAll { it.name.endsWith('.json') }
        if (jsonFiles.isEmpty()) {
            echo "No JSON files found in the folder."
            return
        }

        // Print CSV Headers
        def headers = ["Environment", "RBAC_Users", "Workspaces_Count", "System_Info", "DB_Version", "Kong_Version", "Services_Count"]
        println headers.join(",")

        jsonFiles.each { file ->
            def jsonData = loadJsonFile("resources/${file.name}")
            if (jsonData == null) return // Skip if JSON loading failed

            // Extract fields
            def environment = file.name.replace(".json", "")
            def rbacUsers = jsonData.rbac_users
            def workspacesCount = jsonData.workspaces_count
            def systemInfo = "uname:-${jsonData.system_info.uname} hostname:-${jsonData.system_info.hostname} cores:-${jsonData.system_info.cores}"
            def dbVersion = jsonData.db_version
            def kongVersion = jsonData.kong_version
            def servicesCount = jsonData.services_count

            // Print CSV row
            def row = [environment, rbacUsers, workspacesCount, systemInfo, dbVersion, kongVersion, servicesCount]
            println row.join(",")
        }
    } catch (Exception e) {
        echo "Error processing files: ${e.message}"
    }
}

// Call the function with your resources folder path
processJsonFiles("resources")
