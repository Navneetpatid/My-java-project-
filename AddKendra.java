import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths

// Function to load JSON file
def loadJsonFile(String fileName) {
    try {
        def file = new File(fileName)
        if (!file.exists()) {
            println("Error: JSON file not found - ${fileName}")
            return null
        }
        def jsonText = file.text
        return new JsonSlurper().parseText(jsonText)
    } catch (Exception e) {
        println("Error reading JSON file: ${e.message}")
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, String csvFilePath) {
    try {
        def csvFile = new File(csvFilePath)
        def headers = []
        def rows = []

        if (jsonData instanceof Map && jsonData.containsKey("counters")) {
            jsonData.counters.each { item ->
                if (headers.isEmpty()) {
                    headers = item.keySet().toList()
                    csvFile.append(headers.join(",") + "\n")
                }
                def row = headers.collect { key -> item[key] ?: "" }
                rows << row.join(",")
            }
            csvFile.append(rows.join("\n") + "\n")
            println("CSV file created successfully: ${csvFilePath}")
        } else {
            println("Error: Unexpected JSON structure.")
        }
    } catch (Exception e) {
        println("Error converting JSON to CSV: ${e.message}")
    }
}

// Main execution function
def processJson() {
    def fileName = "dev-HK_license.json"
    def jsonData = loadJsonFile(fileName)

    if (jsonData == null) {
        println("JSON file loading failed.")
        return
    }

    println("Successfully loaded JSON file: ${fileName}")
    println("JSON Data: ${jsonData}") // Debugging output

    // Convert JSON to CSV
    def csvFilePath = "resources/dev-HK_license.csv"
    convertJsonToCsv(jsonData, csvFilePath)
}

// Execute the script
processJson()
