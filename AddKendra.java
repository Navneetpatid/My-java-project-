def customHealthcheckFromFile(String defaultStatusCode = "200") {
    // File path for URLs and parameters
    def urlFilePath = "resources/url.txt"

    // Read the file line by line (each line should have: URL expectedStatusCode)
    def urlsAndCodes = new File(urlFilePath).readLines()

    // Process each URL with its custom or default expected status code
    urlsAndCodes.each { line ->
        def parts = line.split("\\s+") // Split by space or tab
        def url = parts[0]
        def expectedStatusCode = parts.size() > 1 ? parts[1] : defaultStatusCode // Use custom or default

        sh """
        response_data=\$(curl -sL -k -w '%{http_code}' ${url} -o /dev/null)
        if [ "\$response_data" == "${expectedStatusCode}" ]; then
            echo "${url} Passed with response code: \$response_data" >> email_data.txt
        else
            echo "${url} Failed with response code: \$response_data (Expected: ${expectedStatusCode})" >> email_data.txt
        fi
        """
    }

    // Print results
    cat email_data.txt
}
