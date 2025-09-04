def getRecordFromKongSHP(def engagementID, def namespace, def cluster) {
    def token = generateToken()
    def returnVal = ""
    try {
        echo "Get Record from SHP Data API - Started"

        dir(env.WORKSPACE) {
            def contentType = "Content-Type: application/json;charset=UTF-8"
            def fileName = "shp_response.json"
            def url = "${environmentRegisterURL}/cer/get/shp/data?engagementId=${engagementID}&namespace=${namespace}&cluster=${cluster}"

            def command = """curl -s -w "%{http_code}" -o ${fileName} \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X GET "${url}" """

            if (adminVerboseLogging) {
                echo "Executing: ${command}"
            }

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "ERR - Service call to SHP Data API failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "ERR - SHP Data API call failed with unknown error: ${e.message}"
        returnVal = "error"
    }
    return returnVal
            }
