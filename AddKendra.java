pipeline {
    agent any

    stages {
        stage('Get Token') {
            steps {
                script {
                    // Bind Jenkins username/password credentials
                    withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL_UAT',
                                                      usernameVariable: 'SNOW_USER',
                                                      passwordVariable: 'SNOW_PASS')]) {
                        def response = sh(
                            script: """
                                curl --silent --request POST \\
                                'https://cmp-lb2b.dsp.prod.eu.systems.uk.hsbc:8443/dsp/rest-sts/DSP-1B2B/tokenTranslator?action=translate' \\
                                --header 'Content-Type: application/json' \\
                                --header 'Authorization: AJDJSDJCMODABJHS7GUAMDPXYAAQUZENAAJAwQJ0MAX01JXW01JLC06AX101JOT5F1IMWMToJUMWNTf0AJZddr6ZS16nZbG' \\
                                --data '{
                                    "input_token_state": {
                                        "token_type": "CREDENTIAL",
                                        "username": "${SNOW_USER}",
                                        "password": "${SNOW_PASS}"
                                    },
                                    "output_token_state": {
                                        "token_type": "JWT"
                                    }
                                }'
                            """,
                            returnStdout: true
                        ).trim()

                        echo "Response: ${response}"

                        // Parse JSON response if it's in JSON format
                        def json = readJSON text: response
                        def token = json?.token ?: "No token found"
                        echo "Generated Token: ${token}"

                        // Save token in env var for later stages
                        env.API_TOKEN = token
                    }
                }
            }
        }

        stage('Use Token') {
            steps {
                echo "Using Token: ${env.API_TOKEN}"
                // Example: call another API with token
            }
        }
    }
}
