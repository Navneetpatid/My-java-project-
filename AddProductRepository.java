import groovy.json.JsonOutput
import groovy.json.JsonSlurper

// Ensure directory exists
sh "mkdir -p ./resources"

try {
    // **Step 1: Save JSON Data**
    def jsonData = [
        "date": rundate ?: "N/A",
        "environment": envlist ?: [],
        "cp": CP ?: "N/A",
        "workspaces": WorkSpacesList ?: [],
        "report": contentFile ?: "N/A"
    ]

    def jsonString = JsonOutput.prettyPrint(JsonOutput.toJson(jsonData))
    String jsonFilename = "./resources/report_data.json"

    writeFile file: jsonFilename, text: jsonString
    echo "JSON data successfully written to: ${jsonFilename}"

    // **Step 2: Convert JSON Response to CSV**
    if (!response) {
        error("Error: 'response' variable is not defined or empty.")
    }

    def jsonResponse
    try {
        jsonResponse = new JsonSlurper().parseText(response)
    } catch (Exception e) {
        error("Error parsing JSON response: " + e.getMessage())
    }

    String csvFilename = "./resources/report_data.csv"

    def headers = ["Date", "Environment", "CP", "Workspace_Name", "Service_Count",
                   "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version", 
                   "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]

    def csvContent = new StringBuilder()
    csvContent.append(headers.join(",") + "\n")

    jsonResponse.each { item ->
        WorkSpacesList.each { workspace ->
            def row = [
                rundate ?: "N/A",
                envlist ? envlist.join("|") : "N/A",
                CP ?: "N/A",
                workspace ?: "N/A",
                "N/A", // Placeholder for Service Count
                item.services_count ?: "N/A",
                item.rbac_users ?: "N/A",
                item.kong_version ?: "N/A",
                item.db_version ?: "N/A",
                item.system_info?.uname ?: "N/A",
                item.system_info?.hostname ?: "N/A",
                item.system_info?.cores ?: "N/A",
                item.workspaces_count ?: "N/A",
                item.license?.license_key ?: "N/A"
            ]
            csvContent.append(row.join(",") + "\n")
        }
    }

    writeFile file: csvFilename, text: csvContent.toString()
    echo "CSV data successfully written to: ${csvFilename}"

} catch (Exception e) {
    error("Error writing JSON or CSV data: " + e.getMessage())
        }
