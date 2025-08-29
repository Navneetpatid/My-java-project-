pipeline {
    agent any

    parameters {
        string(name: 'ENGAGEMENT_ID', defaultValue: '', description: 'Enter Engagement ID')
        string(name: 'WORKSPACE', defaultValue: '', description: 'Enter Workspace name')
    }

    stages {
        stage('Generate ServiceNow Token') {
            steps {
                script {
                    def cerObj = CER([logger:this, adminVerboseLogging:true])
                    def token = cerObj.generateToken()

                    if (token == "error") {
                        error "❌ Token generation failed. Check logs."
                    } else {
                        echo "✅ Token generated successfully: ${token}"
                    }
                }
            }
        }

        stage('Fetch Record from CER') {
            steps {
                script {
                    def cerObj = CER([logger:this, adminVerboseLogging:true])

                    // 🔹 Use Jenkins parameters dynamically
                    def engagementId = params.ENGAGEMENT_ID
                    def workspace = params.WORKSPACE

                    echo "📌 Fetching CER Record for engagementId=${engagementId}, workspace=${workspace}"

                    def record = cerObj.getRecordFromKongCER(engagementId, workspace)

                    if (record == "error") {
                        error "❌ Failed to fetch record from CER. Check logs."
                    } else {
                        echo "📦 Record fetched successfully: ${record}"
                        
                        // 🔹 Save response file (cer_response.json) as build artifact
                        archiveArtifacts artifacts: 'cer_response.json', allowEmptyArchive: true
                    }
                }
            }
        }
    }
}
