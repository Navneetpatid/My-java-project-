def addKongData(def engagementID, def mandatory_plugin, def region, def network_region,
                def dp_platform, def bgpf, def workspace, def environment, def dp_host_url) {
    def token = generateToken()
    def returnVal = ""
    try {
        echo "Add Kong Data API - Started"

        dir(env.WORKSPACE) {
            def contentType = "Content-Type: application/json;charset=UTF-8"
            def fileName = "add_kong_response.json"
            def url = "${environmentRegisterURL}/cer/add/kong/data"

            // Build JSON payload
            def jsonPayload = """{
                "engagementId": "${engagementID}",
                "mandatory_plugin": "${mandatory_plugin}",
                "region": "${region}",
                "network_region": "${network_region}",
                "dp_platform": "${dp_platform}",
                "bgpF": "${bgpf}",
                "workspace": "${workspace}",
                "environment": "${environment}",
                "dp_host_url": "${dp_host_url}"
            }"""

            // Save request body to file
            writeFile file: "kong_request.json", text: jsonPayload

            // Run curl
            def command = """curl -s -o ${fileName} -w "%{http_code}" \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X POST "${url}" \
                --data @kong_request.json"""

            if (adminVerboseLogging) {
                echo "Executing: ${command}"
            }

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "ERR - Service call to Add Kong Data API failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "ERR - Add Kong Data API call failed with unknown error: ${e.message}"
        returnVal = "error"
    }
    return returnVal
                  }
