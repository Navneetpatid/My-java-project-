pipeline {
    agent any

    environment {
        API_BASE = "https://your-api-server.com"
        CREDS = credentials('api-credentials')  // Jenkins stored credentials
    }

    stages {
        stage('Generate Token') {
            steps {
                script {
                    def response = sh(script: """
                        curl -s -X POST "$API_BASE/auth" \
                        -H 'Content-Type: application/json' \
                        -d '{"username": "${CREDS_USR}", "password": "${CREDS_PSW}"}'
                    """, returnStdout: true).trim()

                    def json = readJSON text: response
                    TOKEN = json.token
                    echo "✅ Token generated: ${TOKEN}"
                }
            }
        }

        stage('Add API') {
            steps {
                script {
                    sh """
                        curl -X POST "$API_BASE/add" \
                        -H "Authorization: Bearer $TOKEN" \
                        -H "Content-Type: application/json" \
                        -d '{"name":"Test User","email":"test@example.com"}'
                    """
                }
            }
        }

        stage('Update API') {
            steps {
                script {
                    sh """
                        curl -X PUT "$API_BASE/update/123" \
                        -H "Authorization: Bearer $TOKEN" \
                        -H "Content-Type: application/json" \
                        -d '{"email":"updated@example.com"}'
                    """
                }
            }
        }

        stage('Get API') {
            steps {
                script {
                    sh """
                        curl -X GET "$API_BASE/get/123" \
                        -H "Authorization: Bearer $TOKEN"
                    """
                }
            }
        }
    }
            }
