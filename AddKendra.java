import groovy.json.JsonSlurper

// Function to load JSON from a file
def loadJsonFile(fileName) {
    try {
        def filePath = new File("resources", fileName)  // Ensures proper path handling
        def jsonFile = new File(filePath.absolutePath)

        echo("Checking JSON file at: ${jsonFile.absolutePath}")

        if (!jsonFile.exists() || jsonFile.length() == 0) {
            echo("File not found or empty: ${jsonFile.absolutePath}")
            return null
        }

        // Read JSON as text and parse
        def jsonText = jsonFile.text
        def jsonData = new JsonSlurper().parseText(jsonText)
        
        return jsonData
    } catch (Exception e) {
        echo("Error reading JSON file: ${e.message}")
        return null
    }
}

// Function to process JSON
def processJson() {
    def fileName = "dev-HK_license.json"
    def jsonData = loadJsonFile(fileName)

    if (jsonData == null) {
        echo("JSON file loading failed.")
        return
    }

    echo("Successfully loaded JSON file: ${fileName}")
    echo("JSON Data: ${jsonData}")  // Debugging output

    // Convert JSON to CSV
    def csvFilePath = "resources/dev-HK_license.csv"
    convertJsonToCsv(jsonData, csvFilePath)
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, csvFilePath) {
    try {
        def csvFile = new File(csvFilePath)
        csvFile.text = "id,name,value\n"  // Dummy CSV header

        jsonData.each { item ->
            csvFile << "${item.id},${item.name},${item.value}\n"
        }

        echo("Converted JSON to CSV at: ${csvFile.absolutePath}")
    } catch (Exception e) {
        echo("Failed to convert JSON to CSV: ${e.message}")
    }
}

// Call the processJson function
processJson()
