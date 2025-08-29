pipeline {
    agent any

    parameters {
        string(name: 'ENGAGEMENT_ID', defaultValue: 'HAP-COO-40004', description: 'Enter Engagement ID')
        string(name: 'WORKSPACE_NAME', defaultValue: 'hap-hk-clusterTest123', description: 'Enter Workspace')
    }

    stages {
        stage('Generate ServiceNow Token & Fetch Record') {
            steps {
                script {
                    def cerObj = CER([logger:this, adminVerboseLogging:true])
                    def token = cerObj.generateToken()

                    if (token == "error") {
                        error "❌ Token generation failed. Check logs."
                    } else {
                        echo "✅ Token generated successfully: ${token}"
                    }

                    // 🔹 Use dynamic parameters from Jenkins UI
                    def engagementId = params.ENGAGEMENT_ID
                    def workspace = params.WORKSPACE_NAME

                    def record = cerObj.getRecordFromKongCER(engagementId, workspace)

                    if (record == "") {
                        error "❌ Failed to fetch record from CER"
                    } else {
                        echo "📦 Record fetched successfully: ${record}"
                    }
                }
            }
        }
    }
}
