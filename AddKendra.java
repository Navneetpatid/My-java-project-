// Function to load JSON file
def loadJsonFile(String filePath) {
    try {
        def fileContent = libraryResource(filePath)
        def jsonData = new JsonSlurper().parseText(fileContent)
        return jsonData
    } catch (Exception e) {
        echo "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(Map jsonData, String csvFilePath) {
    try {
        def csvFile = new File(csvFilePath)
        csvFile.withWriter { writer ->
            def headers = []
            def rows = []
            
            if (jsonData.containsKey("counters")) {
                jsonData.counters.eachWithIndex { item, index ->
                    if (index == 0) {
                        headers = item.keySet() as List
                        writer.writeLine(headers.join(','))
                    }
                    def row = headers.collect { key -> item.get(key, '') }
                    writer.writeLine(row.join(','))
                }
                echo "CSV file created successfully: ${csvFilePath}"
            } else {
                echo "Error: JSON structure missing 'counters' key"
            }
        }
    } catch (Exception e) {
        echo "Error converting JSON to CSV: ${e.message}"
    }
}

// Main processing function
def processJson() {
    def jsonFilePath = "dev-HK_license.json"
    def csvFilePath = "resources/dev-HK_license.csv"
    
    def jsonData = loadJsonFile(jsonFilePath)
    if (!jsonData) {
        echo "JSON file loading failed"
        return
    }
    
    echo "Successfully loaded JSON file: ${jsonFilePath}"
    convertJsonToCsv(jsonData, csvFilePath)
}

// Execute the process
processJson()
