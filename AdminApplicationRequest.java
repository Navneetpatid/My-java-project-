import groovy.json.JsonSlurper

// Main function to process JSON and send email
def processJsonAndSendEmail() {
    def workspacePath = new File("${env.WORKSPACE}")
    
    if (!workspacePath.exists() || !workspacePath.isDirectory()) {
        error "Workspace directory not found: ${env.WORKSPACE}"
    }

    // List all JSON files
    def jsonFiles = workspacePath.listFiles().findAll { it.name.endsWith(".json") }

    if (jsonFiles.isEmpty()) {
        println "No JSON files found in workspace."
        return
    }

    jsonFiles.each { file ->
        try {
            def jsonData = loadJsonFile(file.absolutePath)
            if (jsonData == null || jsonData.isEmpty()) {
                println "Skipping empty or invalid JSON file: ${file.name}"
            } else {
                println "Processing JSON file: ${file.name}"
                def csvFileName = file.name.replace(".json", ".csv")
                convertJsonToCsv(jsonData, csvFileName)
            }
        } catch (Exception e) {
            println "Error processing JSON file ${file.name}: ${e.message}"
        }
    }

    // Send email with generated CSV
    sendEmailWithCsv()
}

// Function to load JSON file
def loadJsonFile(String filePath) {
    def jsonFile = new File(filePath)
    if (!jsonFile.exists()) {
        println "File not found: ${filePath}"
        return null
    }
    
    try {
        return new JsonSlurper().parseText(jsonFile.text)
    } catch (Exception e) {
        println "Error parsing JSON file ${filePath}: ${e.message}"
        return null
    }
}

// Function to convert JSON to CSV
def convertJsonToCsv(jsonData, String csvFileName) {
    def csvFilePath = "${env.WORKSPACE}/${csvFileName}"
    def csvContent = new StringBuilder()

    if (jsonData.isEmpty()) {
        println "Skipping CSV conversion, JSON data is empty."
        return
    }

    // Extract environment name from file
    def environment = csvFileName.replace(".csv", "")

    // Extract required values
    def servicesCount = jsonData.services_count
    def rbacUsers = jsonData.rbac_users
    def kongVersion = jsonData.kong_version
    def dbVersion = jsonData.db_version
    def uname = jsonData.system_info.uname
    def hostname = jsonData.system_info.hostname
    def cores = jsonData.system_info.cores
    def workspacesCount = jsonData.workspaces_count
    def licenseKey = jsonData.containsKey("license") ? jsonData.license_key : jsonData.license

    // Write CSV headers if file is new
    if (!new File(csvFilePath).exists()) {
        def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                       "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
        csvContent.append(headers.join(",")).append("\n")
    }

    // Append row data
    def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname,
                  cores, workspacesCount, licenseKey]
    csvContent.append(values.join(",")).append("\n")

    // Write CSV file
    new File(csvFilePath).text += csvContent.toString()
    println "CSV file created: ${csvFilePath}"
}

// Function to send email with CSV attachment
def sendEmailWithCsv() {
    def csvFiles = new File("${env.WORKSPACE}").listFiles().findAll { it.name.endsWith(".csv") }
    
    if (csvFiles.isEmpty()) {
        println "No CSV files found to send."
        return
    }

    def attachmentPattern = csvFiles.collect { it.name }.join(",")
    
    emailext(
        to: "navneet.patidar@noexternalmail.hsbc.com",
        subject: "Admin API Pipeline Report",
        body: """Hi Team,

This is an auto-generated email.
Please find the attached report.

Regards,
Automated System""",
        attachmentsPattern: attachmentPattern
    )

    println "Email sent successfully with CSV attachment!"
}

// Execute the function
processJsonAndSendEmail()
