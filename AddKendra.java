import groovy.json.JsonSlurper

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData) {
    try {
        def headers = "services_count,rbac_users,kong_version,db_version,uname,hostname,cores,workspaces_count,license_key,bucket,request_count\n"
        def csvContent = new StringBuilder(headers)

        jsonData.counters.each { item ->
            def row = [
                jsonData.services_count, jsonData.rbac_users, 
                jsonData.kong_version, jsonData.db_version,
                jsonData.system_info.uname, jsonData.system_info.hostname, jsonData.system_info.cores,
                jsonData.workspaces_count, jsonData.license_key,
                item.bucket, item.request_count
            ].join(",")

            csvContent.append(row).append("\n")
        }

        // Use Jenkins-safe method instead of File.write
        writeFile(file: "output.csv", text: csvContent.toString())
        echo "CSV file created successfully: output.csv"

    } catch (Exception e) {
        echo "Error converting JSON to CSV: ${e.message}"
    }
}

// Function to load JSON file (Jenkins-safe)
def loadJsonFile(filePath) {
    try {
        def jsonText = readFile(filePath)  // Jenkins-safe file read
        return new JsonSlurper().parseText(jsonText)
    } catch (Exception e) {
        echo "Error reading JSON file: ${e.message}"
        return null
    }
}

// Jenkins Pipeline-friendly execution
node {
    stage('Convert JSON to CSV') {
        def jsonFilePath = "dev-HK_license.json"  // JSON file location
        def jsonData = loadJsonFile(jsonFilePath)

        if (jsonData == null) {
            echo "JSON file loading failed."
            return
        }

        echo "Successfully loaded JSON file: ${jsonFilePath}"

        // Convert JSON to CSV
        convertJsonToCsv(jsonData)
    }
}
