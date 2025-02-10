import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets

def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            def environment = new File(filePath).getName().replace(".json", "")

            // Manually extract values (excluding "counters")
            def servicesCount = jsonData.services_count
            def rbacUsers = jsonData.rbac_users
            def kongVersion = jsonData.kong_version
            def dbVersion = jsonData.db_version
            def uname = jsonData.system_info.uname
            def hostname = jsonData.system_info.hostname
            def cores = jsonData.system_info.cores
            def workspacesCount = jsonData.workspaces_count
            def licenseKey = jsonData.license_key

            // **Create CSV Content**
            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                           "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname,
                          cores, workspacesCount, licenseKey]

            def csvContent = new StringBuilder()
            csvContent.append(headers.join(",")).append("\n")
            csvContent.append(values.join(",")).append("\n")

            // **SAVE CSV FILE**
            def csvFilePath = filePath.replace(".json", ".csv")  // Name CSV same as JSON 
            Files.write(Paths.get(csvFilePath), csvContent.toString().getBytes(StandardCharsets.UTF_8))
            println "CSV file saved as: ${csvFilePath}"

            // **STATUS NOTIFICATION**
            sendStatusNotification("SUCCESS", "CSV file generated successfully: ${csvFilePath}", csvFilePath)
        }
    } catch (Exception e) {
        println "Error processing JSON: ${e.message}"
        sendStatusNotification("FAILED", "Error processing JSON: ${e.message}", null)
    }
}

// **Function to Send Status Notification with Email**
def sendStatusNotification(String status, String message, String attachmentPath) {
    println "[STATUS: ${status}] - ${message}"

    if (status == "SUCCESS") {
        emailext subject: "CSV Generation Successful",
                 body: "The CSV file was successfully generated.\n\n${message}",
                 to: "recipient@example.com",
                 attachFiles: attachmentPath ? [attachmentPath] : []
    } else {
        emailext subject: "CSV Generation Failed",
                 body: "An error occurred while processing JSON.\n\n${message}",
                 to: "recipient@example.com"
    }
}
