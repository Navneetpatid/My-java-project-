@Library('your-shared-library') _   // <-- your library name in Jenkins

pipeline {
    agent any

    stages {
        stage('Generate ServiceNow Token') {
            steps {
                script {
                    def token = CER.generateToken()

                    if (token == "error") {
                        error "❌ Token generation failed. Check logs."
                    } else {
                        echo "✅ Token generated successfully: ${token}"
                    }
                }
            }
        }
    }
}
