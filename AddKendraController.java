pipeline {
    agent any

    parameters {
        string(name: 'ENGAGEMENT_ID', defaultValue: 'HAP-COO-40004', description: 'Enter Engagement ID')
        string(name: 'WORKSPACE', defaultValue: 'hap-hk-clusterTest123', description: 'Enter Workspace')
    }

    environment {
        API_URL = "http://192.168.1.50:8080/cer/get/snp/data"
        TOKEN   = credentials('SNOW_CREDENTIAL_UAT')   // Jenkins Credential ID
    }

    stages {
        stage('Generate ServiceNow Token') {
            steps {
                script {
                    echo "🔑 Using Jenkins credentials to generate token..."
                    // Example: if TOKEN is username:password, we can encode or pass directly
                    env.API_TOKEN = "${TOKEN}"   // save to env for later curl
                }
            }
        }

        stage('Fetch Record from CER') {
            steps {
                script {
                    echo "📥 Fetching CER Record for engagementId=${params.ENGAGEMENT_ID}, workspace=${params.WORKSPACE}"

                    // Call API with curl
                    sh """
                        curl --fail --silent --show-error \\
                          --request GET "${API_URL}?engagementId=${params.ENGAGEMENT_ID}&namespace=${params.WORKSPACE}" \\
                          --header "Content-Type: application/json" \\
                          --header "X-HSBC-E2E-Trust-Token: ${env.API_TOKEN}" \\
                          -o cer_response.json
                    """

                    echo "✅ CER record saved as cer_response.json"
                    archiveArtifacts artifacts: 'cer_response.json', fingerprint: true
                }
            }
        }
    }
}
