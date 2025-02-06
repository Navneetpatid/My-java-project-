import groovy.json.JsonSlurper

// Function to extract JSON data and print it in CSV format
def convertJsonToCsv(jsonData) {
    try {
        // Check if JSON contains the expected "counters" key
        if (jsonData instanceof Map && jsonData.containsKey("counters")) {
            def headers = ["bucket", "request_count"]
            println headers.join(",")  // Print CSV headers

            jsonData.counters.each { item ->
                if (item instanceof Map && item.containsKey("bucket") && item.containsKey("request_count")) {
                    def row = [item.bucket, item.request_count]
                    println row.join(",")  // Print CSV row
                }
            }
        } else {
            println("Error: Unexpected JSON structure. Expected 'counters' key.")
        }
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
def processJson() {
    def jsonFilePath = "dev-HK_license.json"  // Path to JSON file
    def jsonData = loadJsonFile(jsonFilePath)

    if (jsonData == null) {
        println("JSON file loading failed.")
        return
    }

    println("Successfully loaded JSON file: ${jsonFilePath}")

    // Convert JSON to CSV and print
    convertJsonToCsv(jsonData)
}

// Execute the function
processJson()
