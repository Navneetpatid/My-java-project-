def processJson() {
    // Fetch JSON files dynamically from the Jenkins workspace
    def workspacePath = new File("${env.WORKSPACE}")  // Jenkins workspace path
    def jsonFiles = workspacePath.listFiles().findAll { it.name.endsWith(".json") }

    jsonFiles.each { file ->
        def jsonData = loadJsonFile(file.absolutePath)
        if (jsonData == null) {
            println("JSON file loading failed for: ${file.name}")
        } else {
            println("Successfully loaded JSON file: ${file.name}")
            convertJsonToCsv(jsonData, file.name)
        }
    }

    // Define the output CSV filename in the workspace
    String filename = "${env.WORKSPACE}/mycsvnew.csv";
    println("Filename for writing: ${filename}");

    println("Filename for content file: ${myFinalOutput}");
    writeFile file: "${filename}", text: "${myFinalOutput}";

    // Read the generated CSV file
    String responseBody = readFile encoding: 'UTF-8', file: "${filename}";
    println("Response body: ${responseBody}");
    println("Read file executed successfully");

    // Send an email with the CSV file attached
    println("Sending Email to Recipients with file ${filename}");
    String message = "Hi Team,\n\nThis is an auto-generated mail.\nPlease find the attached report for Workspace, Services, Routes, and Plugins.\nFor any queries, please reach out to the Team.\n";

    emailext(
        to: "navneet.patidar@noexternalmail.hsbc.com",
        subject: "Admin API Pipeline Report",
        body: message,
        attachLog: true,
        attachmentsPattern: "${filename}"
    )
}

// Function to load JSON file content
def loadJsonFile(String filePath) {
    try {
        return new groovy.json.JsonSlurper().parse(new File(filePath))
    } catch (Exception e) {
        println("Error parsing JSON: ${e.message}")
        return null
    }
}
