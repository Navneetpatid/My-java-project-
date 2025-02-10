def processJson() {
    def jsonFileNames = [
        "dev-HK_license.json", "dev-UK_license.json",
        "ppd-HK_license.json", "ppd-UK_license.json"
    ]

    jsonFileNames.each { fileName ->
        try {
            def fileContent = libraryResource(fileName) // Using Jenkins libraryResource
            if (fileContent) {
                def jsonData = new JsonSlurper().parseText(fileContent)
                convertJsonToCsv(jsonData, fileName)
            } else {
                println "Error: Unable to load resource file '${fileName}'"
            }
        } catch (Exception e) {
            println "Error processing JSON resource '${fileName}': ${e.message}"
        }
    }
}
