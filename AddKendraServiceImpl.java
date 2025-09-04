def getRecordFromKongCER(def engagementID, def workspace) {
    def token = generateToken()
    def returnVal = ""
    try {
        echo "Get Record from Central Environment Register - Started"

        dir(env.WORKSPACE) {
            def contentType = "Content-Type: application/json;charset=UTF-8"
            def fileName = "cer_response.json"
            def url = "${environmentRegisterURL}/cer/get/kong/data?engagementId=${engagementID}&workspace=${workspace}"

            def command = """curl -s -w "%{http_code}" -o ${fileName} \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X GET "${url}" """

            if (adminVerboseLogging) {
                echo "Executing: ${command}"
            }

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "ERR - Service call to central environment register failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "ERR - CER call failed with unknown error: ${e.message}"
        returnVal = "error"
    }
    return returnVal
}
