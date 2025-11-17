This change is being implemented to enhance and streamline the HAP Deployment Service by removing unnecessary HAP DS validation, standardizing naming through the ITADS rename, improving operational efficiency with UAT ServiceNow integration, strengthening security via the HAP Kong Global Hashivault release, and simplifying observability processes by removing redundant user authorization for OTEL instrumentation during delete actions.pipeline {
    agent any
    stages {
        stage('Call API') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'SNOW_CREDENTIAL_UAT', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh '''
                        curl -v -k --request POST \
                          "https://cmb-ib2b-dsp.pprod-eu.systems.uk.hsbc.com:8443/dsp/rest-stss/DSP-1B2B/tokenTranslator?action=translate" \
                          --header "Content-Type: application/json" \
                          --cookie "AMToken=AAJTSQACMDA..." \
                          --data "{
                            \\"input_token_state\\": {
                              \\"token_type\\": \\"CREDENTIAL\\",
                              \\"username\\": \\"$USER\\",
                              \\"password\\": \\"$PASS\\"
                            },
                            \\"output_token_state\\": {
                              \\"token_type\\": \\"JWT\\"
                            }
                          }"
                    '''
                }
            }
        }
    }
}

