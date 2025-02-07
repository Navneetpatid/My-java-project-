import groovy.json.JsonSlurper

def processJson() {
    // List of JSON file paths
    def jsonFilePaths = [
        "resources/dev-HK_license.json",
        "resources/dev-UK_license.json",
        "resources/ppd-HK_license.json",
        "resources/ppd-UK_license.json",
        "resources/prd-HK_license.json",
        "resources/prd-UK_license.json"
    ]

    def allJsonData = [] // Store parsed JSON data

    // ✅ Iterate over each JSON file and load data
    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath) // Load JSON
        if (jsonData == null) {
            println("❌ JSON file loading failed: ${filePath}")
            return // Skip this file
        }
        println("✅ Successfully loaded JSON file: ${filePath}")
        allJsonData << [filePath, jsonData] // Store file name + data
    }

    if (allJsonData.isEmpty()) {
        println("⚠️ No valid JSON files to process.")
        return
    }

    // ✅ Convert JSON data to CSV format (without saving)
    convertJsonToCsv(allJsonData)
}

// ✅ Function to load JSON file
def loadJsonFile(filePath) {
    try {
        def fileContent = new File(filePath).text
        return new JsonSlurper().parseText(fileContent) // Parse JSON
    } catch (Exception e) {
        println("❌ Error reading JSON file: ${filePath} - ${e.message}")
        return null
    }
}

// ✅ Function to convert JSON to CSV (Prints to Console)
def convertJsonToCsv(jsonDataList) {
    def headers = ["File", "RBAC_Users", "Workspaces_Count", "System_Info", "DB_Version", "Kong_Version"]
    
    // Print CSV headers
    println(headers.join(","))

    jsonDataList.each { entry ->
        def filePath = entry[0]
        def jsonData = entry[1]

        // Extract fields
        def rbacUsers = jsonData.rbac_users ?: "N/A"
        def workspacesCount = jsonData.workspaces_count ?: "N/A"
        def systemInfo = "uname-${jsonData.system_info?.uname ?: "N/A"} hostname-${jsonData.system_info?.hostname ?: "N/A"}"
        def dbVersion = jsonData.db_version ?: "N/A"
        def kongVersion = jsonData.kong_version ?: "N/A"

        // Print CSV row
        println([filePath, rbacUsers, workspacesCount, systemInfo, dbVersion, kongVersion].join(","))
    }
}

// Run the function
processJson()
