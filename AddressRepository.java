stage("Finalizing & Emailing Report") {
    try {
        String filename = "Report_Detail.csv"
        String reportDir = "./Report"
        String reportPath = "${reportDir}/${filename}"

        // Ensure directory exists
        sh "mkdir -p ${reportDir}"

        // CSV Header
        String csvContent = "Workspace Name, Service Count, CP, License Key, RBAC Users, Request Count, Kong Version, DB Version\n"

        // Read and parse API response
        def jsonResponse = readFile encoding: 'UTF-8', file: "./Response.json"
        def responseData = new groovy.json.JsonSlurper().parseText(jsonResponse)

        // Append workspace details
        csvContent += "${workspace_name}, ${responseData.services_count}, ${CP}, ${responseData.license_key}, " +
                      "${responseData.rbac_users}, ${responseData.request_count}, ${responseData.kong_version}, ${responseData.db_version}\n"

        // Append bucket request counts
        csvContent += "Bucket, Request Count\n"
        responseData.counters.each { counter ->
            csvContent += "${counter.bucket}, ${counter.request_count}\n"
        }

        // Write CSV file
        writeFile file: reportPath, text: csvContent
        echo "CSV file created successfully: ${reportPath}"

        // Read CSV to verify
        String responseBody = readFile encoding: 'UTF-8', file: reportPath
        echo "Report Content:\n${responseBody}"

        // Process workspace name
        String tempworkspacename1 = workspace_name.replace("-DEV", "")
        echo "Sending Email for: ${tempworkspacename1}"

        // Email message
        String message = """
        Hi Team,

        This is an auto-generated mail for ${tempworkspacename1}.
        Please find the attached report for Workspace, Services, Routes, and Plugins.

        Thanks
        """

        // Send email with CSV attachment
        emailext(
            to: "navneet.patidar@noexternalmail.hsbc.com",
            subject: "Admin API Pipeline Report - ${tempworkspacename1}",
            attachLog: false,
            attachmentsPattern: "${reportPath}",
            body: message
        )

        echo "Email sent successfully!"
    } catch (Exception e) {
        echo "Error sending email: ${e.getMessage()}"
    }
}
