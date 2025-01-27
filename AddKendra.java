def passedUrls = []
def failedUrls = []

urlLines.each { line ->
    try {
        def parts = line.trim().split("\\s+") // Split line into parts
        def url = parts[0]                   // First part is the URL
        def expectedStatusCode = parts.size() > 1 ? parts[1].toInteger() : defaultStatusCode
        def response = httpRequest(
            url: url
        )
        def actualStatusCode = response.status as Integer

        if (actualStatusCode == expectedStatusCode) {
            echo "${url} - SUCCESS (Response Code: ${actualStatusCode})"
            passedUrls << url
            sh "echo '${url} Passed' >> email_data.txt"
        } else {
            echo "${url} - FAILED (Expected: ${expectedStatusCode}, Actual: ${actualStatusCode})"
            failedUrls << url
            sh "echo '${url} FAILED (Response Code: ${actualStatusCode})' >> email_data.txt"
        }
    } catch (hudson.AbortException e) {
        echo "${line} - ERROR: ${e.message}"
        failedUrls << line // Add the entire line if the URL failed due to an error
    }
}

// Write the summary to email
def emailBody = "Summary:\n\nPassed URLs:\n"
emailBody += passedUrls.join("\n") + "\n\nFailed URLs:\n"
emailBody += failedUrls.join("\n")

writeFile file: 'email_summary.txt', text: emailBody
echo "Email summary written to email_summary.txt"
