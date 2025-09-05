def updateShpData(def updatesList) {
    def token = generateToken()
    def returnVal = ""
    try {
        echo "Update SHP Data API - Started"

        dir(env.WORKSPACE) {
            def contentType = "Content-Type: application/json;charset=UTF-8"
            def fileName = "update_shp_response.json"
            def url = "${environmentRegisterURL}/cer/data/update"

            // Convert updatesList (list of maps) to JSON
            def jsonPayload = groovy.json.JsonOutput.prettyPrint(
                groovy.json.JsonOutput.toJson(updatesList)
            )

            // Save request body to file
            writeFile file: "update_shp_request.json", text: jsonPayload

            // Run curl
            def command = """curl -s -o ${fileName} -w "%{http_code}" \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X POST "${url}" \
                --data @update_shp_request.json"""

            if (adminVerboseLogging) {
                echo "Executing: ${command}"
            }

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "ERR - Service call to Update SHP Data API failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "ERR - Update SHP Data API call failed: ${e.message}"
        returnVal = "error"
    }
    return returnVal
        }
def updates = [
    [
        tableName: "engagement_target_shp",
        query: "UPDATE engagement_target_shp SET ein_id = ? WHERE engagement_id = ?",
        parameters: ["123529", "HAP-COO-40009"]
    ],
    [
        tableName: "eke_platform_details",
        query: "UPDATE eke_platform_details SET environment = ? WHERE namespace = ?",
        parameters: ["ppd", "hap-hdc-cto-113"]
    ]
]

def result = updateShpData(updates)
echo "Final Result: ${result}"
