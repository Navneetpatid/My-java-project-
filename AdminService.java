pipeline {
    agent any

    parameters {
        string(name: 'USER_ID', defaultValue: '123', description: 'User ID for API')
        string(name: 'DATA_VALUE', defaultValue: 'testdata', description: 'Data to be sent')
    }

    stages {
        stage('Checkout Pipeline Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/your-org/pipeline-repo.git'
            }
        }

        stage('Checkout CER Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/your-org/cer-repo.git'
            }
        }

        stage('Generate Token') {
            steps {
                script {
                    def response = sh(
                        script: """
                          curl -s -X POST https://api.cer.com/auth/token \
                          -H "Content-Type: application/json" \
                          -d '{"username":"yourUser","password":"yourPass"}'
                        """,
                        returnStdout: true
                    ).trim()

                    def json = readJSON text: response
                    env.AUTH_TOKEN = json.token
                    echo "Generated Token: ${env.AUTH_TOKEN}"
                }
            }
        }

        stage('Call API with Parameters') {
            steps {
                script {
                    def response = sh(
                        script: """
                          curl -s -X POST https://api.cer.com/data/save \
                          -H "Content-Type: application/json" \
                          -H "Authorization: Bearer ${env.AUTH_TOKEN}" \
                          -d '{
                                "userId": "${params.USER_ID}",
                                "value": "${params.DATA_VALUE}"
                              }'
                        """,
                        returnStdout: true
                    ).trim()

                    echo "API Response: ${response}"
                }
            }
        }
    }
}
