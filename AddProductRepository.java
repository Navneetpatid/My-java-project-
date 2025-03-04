import groovy.json.JsonOutput
import groovy.json.JsonSlurper

// Ensure directory exists
sh "mkdir -p ./resources"

try {
    // **Step 1: Save JSON Data to File**
    def jsonData = [
        "date": rundate,
        "environment": envlist,
        "cp": CP,
        "workspaces": WorkSpacesList,
        "report": contentFile
    ]

    def jsonString = JsonOutput.prettyPrint(JsonOutput.toJson(jsonData))
    String jsonFilename = "./resources/report_data.json"

    writeFile file: jsonFilename, text: jsonString
    echo "JSON data successfully written to: ${jsonFilename}"

    // **Step 2: Convert JSON Response to CSV**
    def jsonResponse = new JsonSlurper().parseText(response)
    String csvFilename = "./resources/report_data.csv"

    def headers = ["Date", "Environment", "CP", "Workspace_Name", "Service_Count",
                   "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version", 
                   "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]

    def csvContent = new StringBuilder()
    csvContent.append(headers.join(",") + "\n")

    jsonResponse.each { item ->
        WorkSpacesList.each { workspace ->
            def row = [
                rundate,
                envlist.join("|"),
                CP,
                workspace,
                "N/A", // Placeholder for Service Count (modify as needed)
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
