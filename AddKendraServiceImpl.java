def generateToken() {
    withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL', passwordVariable: 'PASSWORD', usernameVariable: 'USER')]) {
        echo "Token generation - Start"

        def requestJSON = """{
            "input_token_state": {
                "token_type": "CREDENTIAL",
                "username": "${USER}",
                "password": "${PASSWORD}"
            },
            "output_token_state": {
                "token_type": "JWT"
            }
        }"""

        def fileName = "response.json"
        def url = tokenURL
        def contentType = "-H 'Content-Type: application/json;charset=UTF-8'"

        // Build curl command
        def command = """curl -s -o ${fileName} -w "%{http_code}" ${contentType} -X POST -d '${requestJSON}' "${url}" """

        // Execute
        def statusCode = sh(script: command, returnStdout: true).trim()

        if (statusCode != "200") {
            echo "ERR - Token generation failed. HTTP ${statusCode}"
            return "error"
        } else {
            def response = readFile(file: fileName)
            def jsonResponse = readJSON text: response
            if (jsonResponse?.issued_token) {
                echo "Token generated successfully"
                return jsonResponse.issued_token
            } else {
                echo "ERR - No token found in response"
                return "error"
            }
        }
    }
                }
