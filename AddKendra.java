import groovy.json.JsonSlurper
import java.io.File

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, csvFilePath) {
    try {
        def headers = ["services_count", "rbac_users", "kong_version", "db_version", 
                       "uname", "hostname", "cores", "workspaces_count", "license_key", 
                       "bucket", "request_count"]

        def csvFile = new File(csvFilePath)
        csvFile.withWriter { writer ->
            writer.writeLine(headers.join(","))  // Write headers

            jsonData.counters.each { item ->
                def row = [
                    jsonData.services_count, jsonData.rbac_users, 
                    jsonData.kong_version, jsonData.db_version,
                    jsonData.system_info.uname, jsonData.system_info.hostname, jsonData.system_info.cores,
                    jsonData.workspaces_count, jsonData.license_key,
                    item.bucket, item.request_count
                ]
                writer.writeLine(row.join(","))  // Write row
            }
        }
        println("CSV file created successfully: ${csvFilePath}")
    } catch (Exception e) {
        println("Error converting JSON to CSV: ${e.message}")
    }
}

// Function to load JSON file
def loadJsonFile(filePath) {
    try {
        def jsonFile = new File(filePath)
        if (jsonFile.exists()) {
            return new JsonSlurper().parseText(jsonFile.text)
        } else {
            println("Error: JSON file not found.")
            return null
        }
    } catch (Exception e) {
        println("Error reading JSON file: ${e.message}")
        return null
    }
}

// Main function to process JSON and convert to CSV format
def processJsonToCsv() {
    def jsonFilePath = "dev-HK_license.json"  // Path to your JSON file
    def csvFilePath = "output.csv"  // Output CSV file name
    def jsonData = loadJsonFile(jsonFilePath)

    if (jsonData == null) {
        println("JSON file loading failed.")
        return
    }

    println("Successfully loaded JSON file: ${jsonFilePath}")

    // Convert JSON to CSV
    convertJsonToCsv(jsonData, csvFilePath)
}

// Execute the function
processJsonToCsv()
