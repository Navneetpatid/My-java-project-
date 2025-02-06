import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths

// Function to load JSON from a file
def loadJsonFile(String filePath) {
    try {
        def fileContent = new File(filePath).text
        return new JsonSlurper().parseText(fileContent)
    } catch (Exception e) {
        println("Error reading JSON file: ${e.message}")
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, String csvFilePath) {
    try {
        def csvFile = new File(csvFilePath)
        csvFile.withWriter { writer ->
            def headers = []
            def rows = []

            if (jsonData instanceof Map && jsonData.containsKey("counters")) {
                jsonData.counters.each { item ->
                    if (headers.isEmpty()) {
                        headers = item.keySet().toList()
                        writer.writeLine(headers.join(","))
                    }
                    def row = headers.collect { key -> item[key] ?: "" }
                    rows << row.join(",")
                }
                rows.each { writer.writeLine(it) }
                println("CSV file created successfully: ${csvFilePath}")
            } else {
                println("Error: Unexpected JSON structure. Expected 'counters' key.")
            }
        }
    } catch (Exception e) {
        println("Error converting JSON to CSV: ${e.message}")
    }
}

// Main function to process JSON and generate CSV
def processJson() {
    def jsonFilePath = "dev-HK_license.json"
    def csvFilePath = "resources/dev-HK_license.csv"

    def jsonData = loadJsonFile(jsonFilePath)
    if (jsonData == null) {
        println("JSON file loading failed.")
        return
    }

    println("Successfully loaded JSON file: ${jsonFilePath}")
    println("JSON Data: ${jsonData}") // Debugging output

    // Convert JSON to CSV
    convertJsonToCsv(jsonData, csvFilePath)
}

// Run the process
processJson()
