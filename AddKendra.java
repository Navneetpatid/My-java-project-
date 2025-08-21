pipeline {
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
