// Function to load and read the JSON file
def loadJsonFile(fileName) {
    try {
        def filePath = "resources/${fileName}"
        def jsonFile = new File(filePath)
        echo("Checking JSON file at: ${jsonFile.absolutePath}")

        if (!jsonFile.exists() || jsonFile.length() == 0) {
            echo("File not found or empty: ${jsonFile.absolutePath}")
            return null
        }

        // Parse JSON
        def jsonData = new groovy.json.JsonSlurper().parse(jsonFile)
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
        echo "JSON file loading failed."
        return
    }

    // Convert JSON to CSV or perform operations
    echo "Successfully loaded JSON file: ${fileName}"
    echo "JSON Data: ${jsonData}"  // Debugging output

    // Call a function to convert JSON to CSV (if needed)
    convertJsonToCsv(jsonData, "resources/dev-HK_license.csv")
}

// Function to convert JSON to CSV (Dummy function, implement logic)
def convertJsonToCsv(jsonData, csvFilePath) {
    try {
        def csvFile = new File(csvFilePath)
        csvFile.text = "id,name,value\n"  // Dummy CSV Header

        jsonData.each { item ->
            csvFile << "${item.id},${item.name},${item.value}\n"
        }

        echo "Converted JSON to CSV at: ${csvFile.absolutePath}"
    } catch (Exception e) {
        echo "Failed to convert JSON to CSV: ${e.message}"
    }
}

// Start the process
processJson()
