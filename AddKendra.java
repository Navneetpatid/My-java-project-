import hudson.FilePath

def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            def environment = filePath.tokenize('/').last().replace(".json", "")

            // Extract values
            def servicesCount = jsonData.services_count ?: "N/A"
            def rbacUsers = jsonData.rbac_users ?: "N/A"
            def kongVersion = jsonData.kong_version ?: "N/A"
            def dbVersion = jsonData.db_version ?: "N/A"
            def uname = jsonData.system_info?.uname ?: "N/A"
            def hostname = jsonData.system_info?.hostname ?: "N/A"
            def cores = jsonData.system_info?.cores ?: "N/A"
            def workspacesCount = jsonData.workspaces_count ?: "N/A"
            def licenseKey = jsonData.license_key ?: "N/A"

            // CSV Headers & Values
            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                           "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname,
                          cores, workspacesCount, licenseKey]

            def csvContent = headers.join(",") + "\n" + values.join(",") + "\n"

            // **Write CSV Using Jenkins Workspace API**
            def csvFilePath = filePath.replace(".json", ".csv")
            def workspace = new FilePath(new File(csvFilePath))
            workspace.write(csvContent, "UTF-8")

            println "CSV file saved as: ${csvFilePath}"

            // **Send Status Notification**
            sendStatusNotification("SUCCESS", "CSV file generated successfully: ${csvFilePath}", csvFilePath)
        }
    } catch (Exception e) {
        println "Error processing JSON: ${e.message}"
        sendStatusNotification("FAILED", "Error processing JSON: ${e.message}", null)
    }
}

// **Email Notification with Attachment**
def sendStatusNotification(String status, String message, String attachmentPath) {
    println "[STATUS: ${status}] - ${message}"

    if (status == "SUCCESS") {
        emailext subject: "CSV Generation Successful",
                 body: "CSV file generated successfully.\n\n${message}",
                 to: "recipient@example.com",
                 attachmentsPattern: attachmentPath ? attachmentPath : ""
    } else {
        emailext subject: "CSV Generation Failed",
                 body: "An error occurred while processing JSON.\n\n${message}",
                 to: "recipient@example.com"
    }
                }
