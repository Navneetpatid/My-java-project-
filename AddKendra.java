pipeline {
    agent any

    environment {
        // Secure way: store token in Jenkins credentials
        API_TOKEN = credentials('SNOW_CREDENTIAL_UAT')
    }

    stages {
        stage('Call GET API') {
            steps {
                script {
                    sh """
                        curl --request GET \
                        "https://cluster1-test:1138/cdp/v1alpha/datamanagementid/HAPCQA0004?namespace=hap.hk" \
                        --header "Content-Type: application/json" \
                        --header "X-HSBC-E2E-Trust-Token: ${API_TOKEN}"
                    """
                }
            }
        }
    }
}
