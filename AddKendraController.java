

// Function to load a JSON file
def loadJsonFile(filePath) {
    try {
        def fileContent = new File(filePath).text  // Read file content
        def jsonData = new JsonSlurper().parseText(fileContent)  // Parse JSON
        return jsonData
    } catch (Exception e) {
        println "Error reading JSON file: ${e.message}"
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            // Extract environment name from file name
            def environment = new File(filePath).getName().replace(".json", "")

            // Extract relevant fields
            def servicesCount = jsonData.services_count
            def rbacUsers = jsonData.rbac_users
            def kongVersion = jsonData.kong_version
            def dbVersion = jsonData.db_version
            def uname = jsonData.system_info.uname
            def hostname = jsonData.system_info.hostname
            def cores = jsonData.system_info.cores
            def workspacesCount = jsonData.workspaces_count
            def licenseKey = jsonData.containsKey("license") ? jsonData.license.license_key : "N/A"

            // Define CSV headers and values
            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                           "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion,
                          uname, hostname, cores, workspacesCount, licenseKey]

            // Generate CSV content
            def csvContent = headers.join(",") + "\n" + values.join(",")

            // Write CSV file
            def csvFilePath = filePath.replace(".json", ".csv")
            new File(csvFilePath).text = csvContent
            println "CSV file created successfully: ${csvFilePath}"
        } else {
            println "Error: JSON data is null or incorrect format"
        }
    } catch (Exception e) {
        println "Error converting JSON to CSV: ${e.message}"
    }
}

// Function to process multiple JSON files and generate CSV files
def processJson() {
    def jsonFilePaths = [
        "dev-HK_license.json", "dev-UK_license.json", 
        "ppd-HK_license.json", "ppd-UK_license.json"
    ]

    jsonFilePaths.each { filePath ->
        def jsonData = loadJsonFile(filePath)
        if (jsonData == null) {
            println "JSON file loading failed for: ${filePath}"
        } else {
            println "Successfully loaded JSON file: ${filePath}"
            convertJsonToCsv(jsonData, filePath)
        }
    }
}

// Main execution
processJson()

// Write output to a file
String filename = "mycsvnew.csv"
String myFinalOutput = "CSV conversion completed."
println "Filename for writing: ${filename}";
println "Filename for content file: ${myFinalOutput}";

// Save final output
new File(filename).text = myFinalOutput
println "Output written to: ${filename}"

// Read file and print response
String responseBody = new File(filename).text
println "Response body: ${responseBody}"
println "Read file executed successfully"

// Send email with the generated CSV file
println "Sending Email to Recipients: ${filename}"

emailext(
    to: "navneet.patidar@noexternalmail.hsbc.com",
    subject: "License Report",
    attachLog: false,
    attachmentsPattern: "${filename}",
    body: "Hi,\n\nPlease find attached the latest license report.\n\nBest regards."
)

println "Email sent successfully!"
