pipeline {
    agent any

    stages {
        stage('Generate Token') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL_UAT', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        // Example: Call token API with username + password
                        def tokenResponse = sh(
                            script: """curl -s -X POST "https://your-auth-server.com/oauth/token" \
                                -H "Content-Type: application/json" \
                                -d '{"username": "${USERNAME}", "password": "${PASSWORD}", "grant_type": "password"}'""",
                            returnStdout: true
                        ).trim()

                        echo "Token Response: ${tokenResponse}"

                        // Extract token if JSON format
                        env.AUTH_TOKEN = sh(
                            script: "echo '${tokenResponse}' | jq -r .access_token",
                            returnStdout: true
                        ).trim()

                        echo "Generated Token: ${env.AUTH_TOKEN}"
                    }
                }
            }
        }

        stage('Call GET API') {
            steps {
                script {
                    def response = sh(
                        script: """curl -s -X GET "http://localhost:8080/cluster/test123/api/v1/data?managementId=HAP_CCO_40004&namespace=hap_kc_doc" \
                            -H "Content-Type: application/json" \
                            -H "Authorization: Bearer ${env.AUTH_TOKEN}" """,
                        returnStdout: true
                    ).trim()

                    echo "API Response: ${response}"
                }
            }
        }
    }
}
