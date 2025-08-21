pipeline {
    agent any

    stages {
        stage('Get Token') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL_UAT',
                                                      usernameVariable: 'SNOW_USER',
                                                      passwordVariable: 'SNOW_PASS')]) {

                        // Write JSON request body to a temp file
                        writeFile file: 'request.json', text: """
                        {
                            "input_token_state": {
                                "token_type": "CREDENTIAL",
                                "username": "${SNOW_USER}",
                                "password": "${SNOW_PASS}"
                            },
                            "output_token_state": {
                                "token_type": "JWT"
                            }
                        }
                        """

                        // Execute curl with JSON file as input
                        def response = sh(
                            script: """
                                curl --silent --fail --show-error --request POST \\
                                'https://cmp-lb2b.dsp.prod.eu.systems.uk.hsbc:8443/dsp/rest-sts/DSP-1B2B/tokenTranslator?action=translate' \\
                                --header 'Content-Type: application/json' \\
                                --header 'Authorization: AJDJSDJCMODABJHS7GUAMDPXYAAQUZENAAJAwQJ0MAX01JXW01JLC06AX101JOT5F1IMWMToJUMWNTf0AJZddr6ZS16nZbG' \\
                                --data @request.json
                            """,
                            returnStdout: true
                        ).trim()

                        echo "Raw Response: ${response}"

                        // If response is JSON, extract token
                        def json = readJSON text: response
                        def token = json?.token ?: "No token found"
                        echo "Generated Token: ${token}"

                        // Save token for later stages
                        env.API_TOKEN = token
                    }
                }
            }
        }

        stage('Use Token') {
            steps {
                echo "Using Token: ${env.API_TOKEN}"
            }
        }
    }
}
