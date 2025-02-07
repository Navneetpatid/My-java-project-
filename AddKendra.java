def convertJsonToCsv(jsonData, filePath) {
    try {
        if (jsonData) {
            // Extract environment name from file path
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

            // Define headers and values
            def headers = ["Environment", "Services_Count", "RBAC_Users", "Kong_Version", "DB_Version",
                           "Uname", "Hostname", "Cores", "Workspaces_Count", "License_Key"]
            println headers.join(",")

            def values = [environment, servicesCount, rbacUsers, kongVersion, dbVersion, uname, hostname,
                          cores, workspacesCount, licenseKey]
            println values.join(",")

        } else {
            echo "Error: JSON data is null or incorrect format"
        }
    } catch (Exception e) {
        echo "Error processing JSON: ${e.message}"
    }
}
