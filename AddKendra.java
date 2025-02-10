import java.nio.file.Files
import java.nio.file.Paths

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

            if (myFinalOutput.isEmpty()) {
                // Define headers
                def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                               "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
                myFinalOutput += headers.join(",") + "\n"
            }

            // Append data
            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname,
                          cores, workspacesCount, licenseKey]
            myFinalOutput += values.join(",") + "\n"

            // **SAVE CSV FILE**
            def csvFilePath = filePath.replace(".json", ".csv")  // Name CSV same as JSON 
            Files.write(Paths.get(csvFilePath), myFinalOutput.getBytes("UTF-8"))
            println "CSV file saved as: ${csvFilePath}"

            // **STATUS NOTIFICATION**
            sendStatusNotification("SUCCESS", "CSV file generated successfully: ${csvFilePath}")
        }
    } catch (Exception e) {
        println "Error processing JSON: ${e.message}"
        sendStatusNotification("FAILED", "Error processing JSON: ${e.message}")
    }
}

// **Function to Send Status Notification**
def sendStatusNotification(String status, String message) {
    println "[STATUS: ${status}] - ${message}"

    // If in Jenkins, use `emailext`
    if (status == "SUCCESS") {
        emailext subject: "CSV Generation Successful",
                 body: "The CSV file was successfully generated.\n\n${message}",
                 to: "recipient@example.com"
    } else {
        emailext subject: "CSV Generation Failed",
                 body: "An error occurred while processing JSON.\n\n${message}",
                 to: "recipient@example.com"
    }
        }
