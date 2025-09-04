import groovy.json.JsonSlurper
import groovy.transform.Field

@Field def logger
@Field def adminVerboseLogging
@Field def environmentRegisterURL = "https://cer.hsbc-11383538-hapkonghk-dev.dev.gcp.cloud.hk.hsbc"
@Field def tokenURL = "https://cmb-i82b-dsp-pprod-eu.systems.uk.hsbc:8443/dsp/rest-sts/DSP_i82B/i82B_tokenTranslator?action=translate"
@Field def token
@Field def snow

def call(Map config) {
    this.logger = config.logger
    this.adminVerboseLogging = config.adminVerboseLogging
    return this
}

// ===================================================================
// Generate Token
// ===================================================================
def generateToken() {
    try {
        withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL', passwordVariable: 'PASSWORD', usernameVariable: 'USER')]) {
            echo "Token generation - Start"

            def requestJSON = """{
                "input_token_state": {
                    "token_type": "CREDENTIAL",
                    "username": "${USER.toString()}",
                    "password": "${PASSWORD.toString()}"
                },
                "output_token_state": {
                    "token_type": "JWT"
                }
            }"""

            def contentType = "-H \"Content-Type: application/json;charset=UTF-8\""
            def fileName = "response.json"
            def command = """curl -s -o ${fileName} -w "%{http_code}" ${contentType} -X POST --data '${requestJSON.replaceAll("'", "'\\\\''")}' "${tokenURL}" """

            def statusCode = sh(script: command, returnStdout: true).trim()
            if (statusCode != "200") {
                echo "ERR - Token generation failed. HTTP ${statusCode}"
                token = "error"
            } else {
                def response = readFile(fileName)
                def jsonResponse = new JsonSlurper().parseText(response)
                token = jsonResponse.issued_token
                echo "Token generation - Success"
            }
        }
    } catch (Exception e) {
        echo "ERR - Token generation call failed with unknown error: ${e.message}"
        token = "error"
    }
    return token
}

// ===================================================================
// Get Central Environment Register Data
// ===================================================================
def getRecordFromKongCER(def engagementID, def workspace) {
    token = generateToken()
    def returnVal = ""
    try {
        echo "Get Record from Central Environment Register - Started"

        dir(env.WORKSPACE) {
            def contentType = "-H \"Content-Type: application/json;charset=UTF-8\""
            def url = "${environmentRegisterURL}/cer/get/kong/data?engagementID=${engagementID}&workspace=${workspace}"
            def fileName = "cer_response.json"

            def command = """curl -s -o ${fileName} -w "%{http_code}" ${contentType} -H "X-HSBC-E2E-Trust-Token: ${token}" -X GET "${url}" """

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
