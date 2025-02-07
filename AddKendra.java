import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths

// Function to load JSON file
def loadJsonFile(filePath) {
    try {
        return new JsonSlurper().parse(new File(filePath))
    } catch (Exception e) {
        println "❌ Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to extract and print values
def extractAndPrintValues(jsonData) {
    if (jsonData) {
        def rbacUsers = jsonData.rbac_users
        def workspacesCount = jsonData.workspaces_count
        println "rbac_users: ${rbacUsers}, workspaces_count: ${workspacesCount}"
    } else {
        println "❌ JSON data is null or incorrect format"
    }
}

// 📂 Set the file path (replace with actual path if needed)
def jsonFilePath = "/mnt/data/file-48NCNMp7sNk2mwV2x29DxY"

// 🔍 Load JSON and extract values
def jsonData = loadJsonFile(jsonFilePath)
extractAndPrintValues(jsonData)
