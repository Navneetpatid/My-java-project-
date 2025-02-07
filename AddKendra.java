import groovy.json.JsonSlurper

def loadJsonFile(filePath) {
    try {
        def fileContent = libraryResource(filePath)  // Load file content
        def jsonData = new JsonSlurper().parseText(fileContent)  // Parse JSON
        return jsonData
    } catch (Exception e) {
        echo "Error reading JSON file: ${e.message}"
        return null
    }
}

def processJson() {
    def jsonFilePaths = ["dev-HK_license.json", "dev-UK_license.json"]  // List of JSON files
    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData == null) {
            println("JSON file loading failed for: ${filePath}")
        } else {
            println("Successfully loaded JSON file: ${filePath}")
            println("JSON Data: ${jsonData}")  // Debugging output
        }
    }
}

// Call function
processJson()
