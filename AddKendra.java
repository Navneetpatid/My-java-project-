pipeline {
    agent any

    stages {
        stage('Get Token') {
            steps {
                script {
                    // Use Jenkins credentials
                    withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL_UAT',
                                                     usernameVariable: 'SNOW_USER',
                                                     passwordVariable: 'SNOW_PASS')]) {
                        def response = sh(
                            script: """
                                curl --silent --request POST \\
                                  'https://cmp-lb2b.dsp.prod.eu.systems.uk.hsbc:8443/dsp/rest-sts/DSP-1B2B/tokenTranslator?action=translate' \\
                                  --header 'Content-Type: application/json' \\
                                  --header 'cookie: AMToken=AAlTSOQMDIABHR5C...' \\
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

                        echo "Full Response: ${response}"

                        // Parse JSON (requires Pipeline Utility Steps plugin)
                        def json = readJSON text: response
                        def token = json?.token ?: "No token found"
                        echo "Generated Token: ${token}"
                    }
                }
            }
        }
    }
}
