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
+++++++++
    import groovy.json.JsonOutput

// ----------------------
// Function to generate token
// ----------------------
def generateToken() {
    return "dummy-token-12345" // Replace with real logic
}

// ----------------------
// Function to update SHP Data
// ----------------------
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
            def jsonPayload = JsonOutput.prettyPrint(JsonOutput.toJson(updatesList))

            // Save request body to file
            writeFile file: "update_shp_request.json", text: jsonPayload

            // Run curl
            def command = """curl -s -o ${fileName} -w "%{http_code}" \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X POST "${url}" \
                --data @update_shp_request.json"""

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "❌ ERR - Service call to Update SHP Data API failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "✅ Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "❌ ERR - Update SHP Data API call failed: ${e.message}"
        returnVal = "error"
    }
    return returnVal
}

// ----------------------
// PIPELINE
// ----------------------
pipeline {
    agent any

    parameters {
        string(name: 'ENGAGEMENT_ID', defaultValue: '123259', description: 'Enter Engagement ID')
        string(name: 'EIN_ID', defaultValue: 'HAP-COD-40009', description: 'Enter EIN ID')
        string(name: 'ENVIRONMENT', defaultValue: 'ppd', description: 'Enter Environment')
        string(name: 'NAMESPACE', defaultValue: 'hap-hdc-cicd-113', description: 'Enter Namespace')
    }

    stages {
        stage("Update SHP Data") {
            steps {
                script {
                    // ✅ Build updatesList dynamically from Jenkins parameters
                    def updates = [
                        [
                            tableName : "engagement_register_shp",
                            query     : "UPDATE engagement_register_shp SET ein_id = ? WHERE engagement_id = ?",
                            parameters: [params.ENGAGEMENT_ID, params.EIN_ID]
                        ],
                        [
                            tableName : "engagement_register_shp",
                            query     : "UPDATE engagement_register_shp SET environment = ? WHERE namespace = ?",
                            parameters: [params.ENVIRONMENT, params.NAMESPACE]
                        ]
                    ]

                    // Call function
                    def result = updateShpData(updates)
                    echo "🎯 Final result: ${result}"
                }
            }
        }
    }
            }
++++++++
    @Library('licenses_test@licensespoc3') _

pipeline {
    agent any

    parameters {
        string(name: 'TABLE_NAME', defaultValue: '', description: 'Enter Table Name')
        string(name: 'QUERY', defaultValue: '', description: 'Enter Update Query (use ? for params)')
        string(name: 'PARAMETERS', defaultValue: '', description: 'Enter Parameters (comma separated)')
    }

    stages {
        stage('Build Update Payload') {
            steps {
                script {
                    // Convert comma-separated params into List
                    def paramList = params.PARAMETERS.split(',')*.trim()

                    // Build updates list dynamically
                    def updates = [[
                        tableName : params.TABLE_NAME,
                        query     : params.QUERY,
                        parameters: paramList
                    ]]

                    echo "Generated Update Payload: ${updates}"

                    // Call backend function from CERUpdate.groovy
                    def result = updateShpData(updates)
                    echo "Final Result: ${result}"
                }
            }
        }
    }
}
