pipeline {
    agent any

    environment {
        // Store token securely in Jenkins credentials
        API_TOKEN = credentials('SNOW_CREDENTIAL_UAT')
    }

    stages {
        stage('Call GET API') {
            steps {
                script {
                    sh """
                        curl --request GET \
                        "http://192.168.1.50:8080/cer/get/snp/data?engagementId=HAP-COO-40004&namespace=hap-hk-clusterTest123" \
                        --header "Content-Type: application/json" \
                        --header "X-HSBC-E2E-Trust-Token: ${API_TOKEN}"
                    """
                }
            }
        }
    }
}
