def addShpData(def engagementId, def einId, def platform, def bgpF, def namespace,
               def cluster, def environment, def project, def region, def proxy,
               def deployUtilityName, def helmChartNexusUrl, def gcpNode) {
    def token = generateToken()
    def returnVal = ""
    try {
        echo "Add SHP Data API - Started"

        dir(env.WORKSPACE) {
            def contentType = "Content-Type: application/json;charset=UTF-8"
            def fileName = "add_shp_response.json"
            def url = "${environmentRegisterURL}/cer/add/shp/data"

            // Build JSON payload exactly like Postman
            def jsonPayload = """{
                "engagementId": "${engagementId}",
                "einId": "${einId}",
                "platform": "${platform}",
                "bgpF": "${bgpF}",
                "namespace": "${namespace}",
                "cluster": "${cluster}",
                "environment": "${environment}",
                "project": "${project}",
                "region": "${region}",
                "proxy": "${proxy}",
                "deploy_utility_name": "${deployUtilityName}",
                "helm_chart_nexus_url": "${helmChartNexusUrl}",
                "gcpNode": "${gcpNode}"
            }"""

            // Save request body to file
            writeFile file: "shp_request.json", text: jsonPayload

            // Run curl
            def command = """curl -s -o ${fileName} -w "%{http_code}" \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X POST "${url}" \
                --data @shp_request.json"""

            if (adminVerboseLogging) {
                echo "Executing: ${command}"
            }

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "ERR - Service call to Add SHP Data API failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "ERR - Add SHP Data API call failed with unknown error: ${e.message}"
        returnVal = "error"
    }
    return returnVal
}
