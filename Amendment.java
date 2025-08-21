pipeline {
    agent any

    stages {
        stage('Generate Token') {
            steps {
                script {
                    // Call your token API
                    def tokenResponse = sh(
                        script: '''
                        curl -s -X POST "http://localhost:8080/auth/token" \
                        -H "Content-Type: application/json" \
                        -d '{"username":"myuser","password":"mypassword"}'
                        ''',
                        returnStdout: true
                    ).trim()

                    // Extract token from JSON response (assumes {"token":"xyz"})
                    def token = sh(
                        script: """echo '${tokenResponse}' | jq -r '.token'""",
                        returnStdout: true
                    ).trim()

                    echo "Generated Token: ${token}"

                    // Save in env for next stage
                    env.AUTH_TOKEN = token
                }
            }
        }

        stage('Call GET API') {
            steps {
                script {
                    sh """
                    curl -s -X GET "http://localhost:8080/api/v1/data?managementId=HAP_CCO_40004&namespace=hap_kc_db" \
                    -H "Content-Type: application/json" \
                    -H "X-HSBC-TEST: Value" \
                    -H "Trust-Token: ${env.AUTH_TOKEN}"
                    """
                }
            }
        }
    }
}
