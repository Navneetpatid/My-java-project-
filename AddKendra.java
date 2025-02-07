import groovy.json.JsonSlurper

// Function to load JSON from a file
def loadJsonFile(filePath) {
    try {
        def fileContent = libraryResource(filePath)  // Load file as text
        echo "${fileContent}"  // Print raw JSON content (for debugging)
        
        // Parse JSON
        def jsonData = new JsonSlurper().parseText(fileContent)
        return jsonData
    } catch (Exception e) {
        echo "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to extract values and print in required format
def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            // Extract environment name from file path (Example: "dev-UK.json" -> "dev-UK")
            def environment = new File(filePath).getName().replace(".json", "")

            // Define headers
            def headers = ["Environment", "rbac_users", "workspaces_count"]
            println headers.join(",")

            // Extract values
            def rbacUsers = jsonData.rbac_users
            def workspacesCount = jsonData.workspaces_count

            // Print values in a single string format
            println "${environment},${rbacUsers},${workspacesCount}"
        } else {
            echo "Error: JSON data is null or incorrect format"
        }
    } catch (Exception e) {
        echo "Error processing JSON: ${e.message}"
    }
}

// 📂 Set the file path (replace with actual path if needed)
def jsonFilePath = "/mnt/data/file-GhysiA78jwxLVuBAjtPiH3"

// 🔍 Load JSON and convert it
def jsonData = loadJsonFile(jsonFilePath)
convertJsonToCsv(jsonData, jsonFilePath)
