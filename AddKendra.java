// Function to load and read the URL file from resources
def loadUrlFile(fileName) {
    try {
        // Fetch the file from the resources folder
        println("Loading URL file: ${fileName}")
        def fileContent = libraryResource(fileName)

        // Write the content to a local file
        writeFile file: fileName, text: fileContent
        println("File ${fileName} successfully written to workspace.")

        // Read the file line by line and return as a list
        return readFile(fileName).split('\n')
    } catch (Exception e) {
        println("Error reading or loading file ${fileName}: ${e.message}")
        return []
    }
}

// Function to perform HTTP health check for URLs
def customHealthcheckFromFile() {
    def urlFileName = 'URL.txt' // File name in resources
    def defaultStatusCode = 200 // Default HTTP status code to validate against

    // Load URLs from the file
    def urlLines = loadUrlFile(urlFileName)
    if (urlLines.isEmpty()) {
        println("No URLs found in the file. Skipping health check.")
        return
    }

    println("Starting health check for URLs...")

    // Process each URL line
    urlLines.each { line ->
        if (line?.trim()) { // Skip empty lines
            try {
                def parts = line.trim().split("\\s+") // Split line into parts
                def url = parts[0] // First part is the URL
                def expectedStatusCode = parts.size() > 1 ? parts[1].toInteger() : defaultStatusCode

                // Perform HTTP status check using curl
                def response = "curl -sL -o /dev/null -w '%{http_code}' ${url}".execute().text.trim()
                if (response == expectedStatusCode.toString()) {
                    println("${url} passed with response code: ${response}")
                } else {
                    println("${url} failed. Expected: ${expectedStatusCode}, Got: ${response}")
                }
            } catch (Exception e) {
                println("Error processing line '${line}': ${e.message}")
            }
        } else {
            println("Skipping empty or invalid line in URL file.")
        }
    }

    println("Health check completed.")
}

// Jenkins stage to execute the custom health check
stage("URL Health Check") {
    customHealthcheckFromFile()
}
