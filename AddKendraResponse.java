pipeline {
    agent any

    environment {
        // Your API URL
        API_URL = 'https://cer-hsbc-11383538-hapkongmk-dev.geo.cloud.uk.hsbc/...'
    }

    stages {
        stage('Generate Token') {
            steps {
                script {
                    // Call your shared library class/method
                    def tokenGen = new CERGroovy()
                    def token = tokenGen.generateToken()

                    // Print raw token for debugging
                    println("DEBUG: Generated Token = ${token}")

                    // Save token to file (safer)
                    writeFile file: 'apitoken.txt', text: token
                    archiveArtifacts artifacts: 'apitoken.txt', onlyIfSuccessful: true
                }
            }
        }
    }
}
