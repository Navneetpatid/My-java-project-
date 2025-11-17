1) HAP DS rename to ITADS

Renamed HAP Deployment Service to ITADS to standardize naming and remove outdated validation references.
JIRA: https://alm-jira.systems.uk.hsbc/jira/browse/CTOEPEPENG-14999


---

2) UAT SNOW Integration

Added improved integration with UAT ServiceNow, enabling smoother workflow automation and better operational efficiency.


---

3) ITAP Kong Global Release – HashiVault

Integrated HashiVault with ITAP Kong Global Release to strengthen security and improve secret management across environments.


---

4) HAP Kong Fixes due to deck version

Applied required fixes in the Kong gateway to align with the updated deck version, ensuring compatibility and stable deployments.


---

5) Removal of User Authorization for OTEL delete action

Removed unnecessary user authorization checks during OTEL instrumentation delete operations, streamlining cleanups and reducing manual steps.
ARP Job Link: https://alm-jenkins207.hc.cloud.uk.hsbc:8706/job/HAP_Service_Hosting/job/TestinePipeline/This change is being implemented to enhance and streamline the HAP Deployment Service by removing unnecessary HAP DS validation, standardizing naming through the ITADS rename, improving operational efficiency with UAT ServiceNow integration, strengthening security via the HAP Kong Global Hashivault release, and simplifying observability processes by removing redundant user authorization for OTEL instrumentation during delete actions.pipeline {
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


