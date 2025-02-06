import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths

// Function to read and parse JSON file
def readJsonFile(filePath) {
    try {
        def jsonFile = new File(filePath)
        return new JsonSlurper().parse(jsonFile)
    } catch (Exception e) {
        println "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to write data to CSV
def writeCsvFile(csvFilePath, jsonData) {
    try {
        def csvFile = new File(csvFilePath)
        csvFile.withWriter('UTF-8') { writer ->
            // Writing CSV header
            writer.writeLine("services_count,rbac_users,kong_version,db_version,system_info,workspaces_count,request_count,bucket")

            // Writing general JSON data
            def systemInfo = "${jsonData.system_info.uname} | ${jsonData.system_info.hostname} | Cores: ${jsonData.system_info.cores}"
            writer.writeLine("${jsonData.services_count},${jsonData.rbac_users},${jsonData.kong_version},${jsonData.db_version},\"${systemInfo}\",${jsonData.workspaces_count},,")

            // Writing "counters" data
            jsonData.counters.each { counter ->
                writer.writeLine(",,,,,,,${counter.request_count},${counter.bucket}")
            }
        }
        println "CSV file generated: ${csvFilePath}"
    } catch (Exception e) {
        println "Error writing CSV file: ${e.message}"
    }
}

// **Main function to execute JSON to CSV conversion**
def convertJsonToCsv(jsonFilePath, csvFilePath) {
    def jsonData = readJsonFile(jsonFilePath)
    if (jsonData) {
        writeCsvFile(csvFilePath, jsonData)
    } else {
        println "Failed to convert JSON to CSV."
    }
}

// **Calling the function**
def jsonFilePath = "resources/dev-HK_license.json"
def csvFilePath = "resources/dev-HK_license.csv"
convertJsonToCsv(jsonFilePath, csvFilePath)
