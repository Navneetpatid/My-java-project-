import groovy.json.JsonSlurper

// Function to load JSON from file
def loadJsonFile(filePath) {
    try {
        def fileContent = new File(filePath).text
        def jsonData = new JsonSlurper().parseText(fileContent)
        return jsonData
    } catch (Exception e) {
        println("Error reading JSON file: ${filePath}, Error: ${e.message}")
        return null
    }
}

// Function to process JSON files and convert to CSV
def processJson() {
    def jsonFilePaths = [
        "resources/dev-HK_license.json",
        "resources/dev-UK_license.json",
        "resources/ppd-HK_license.json",
        "resources/ppd-UK_license.json",
        "resources/prd-HK_license.json",
        "resources/prd-UK_license.json"
    ]

    // Print CSV headers
    def headers = ["Environment", "RBAC_Users", "Workspaces_Count", "System_Info", "DB_Version", "Kong_Version"]
    println headers.join(",")

    // Process each JSON file
    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData == null) return // Skip if file loading failed

        // Extract fields
        def environment = new File(filePath).name.replace(".json", "")
        def rbacUsers = jsonData.rbac_users ?: "N/A"
        def workspacesCount = jsonData.workspaces_count ?: "N/A"
        def systemInfo = "uname-${jsonData.system_info?.uname ?: 'N/A'}, hostname-${jsonData.system_info?.hostname ?: 'N/A'}"
        def dbVersion = jsonData.db_version ?: "N/A"
        def kongVersion = jsonData.kong_version ?: "N/A"

        // Print CSV row
        println [environment, rbacUsers, workspacesCount, systemInfo, dbVersion, kongVersion].join(",")
    }
}

// Run the process
processJson()
