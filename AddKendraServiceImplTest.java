def generateToken() {
    def token = "error"
    try {
        withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL_UAT', 
                                          passwordVariable: 'PASSWORD', 
                                          usernameVariable: 'USER')]) {
            
            def requestJSON = """
            {
              "input_token_state": {
                "token_type": "CREDENTIAL",
                "username": "${USER}",
                "password": "${PASSWORD}"
              },
              "output_token_state": {
                "token_type": "jwt"
              }
            }
            """
            
            def fileName = "response.json"
            def url = "http://your-api-url.com/auth"   // 🔹 replace with actual URL

            // curl runs silently (-s), no printing request
            def command = """
                curl -s -o ${fileName} -w "%{http_code}" \
                -H "Content-Type: application/json;charset=UTF-8" \
                -X POST "${url}" \
                --data '${requestJSON.replaceAll("\\s+", " ")}'
            """

            def statusCode = sh(script: command, returnStdout: true).trim()
            
            if (statusCode != "200") {
                echo "ERR - Token generation failed. HTTP ${statusCode}"
            } else {
                def response = readFile(fileName)
                def jsonResponse = new groovy.json.JsonSlurper().parseText(response)
                token = jsonResponse.issued_token
                echo "Token generation - Success"   // ✅ only success message
            }
        }
    } catch (Exception e) {
        echo "ERR - Token generation call failed with unknown error"
        token = "error"
    }
    return token
}
