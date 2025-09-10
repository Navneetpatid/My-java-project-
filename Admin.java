@Library('License_test@licensepoc3') _
pipeline {
    agent any
    parameters {
        text(name: 'UPDATE_PAYLOAD', defaultValue: '''[
  {
    "tableName": "TABLE_1",
    "query": "UPDATE table1 SET col1=? WHERE id=?",
    "parameters": ["value1", "123"]
  },
  {
    "tableName": "TABLE_2",
    "query": "DELETE FROM table2 WHERE created_at<?",
    "parameters": ["2025-01-01"]
  }
]''',
        description: 'Enter JSON array with tableName, query, and parameters'
        )
    }

    environment {
        TOKEN = credentials('SNOW_CREDENTIAL_UAT') // Jenkins Credential ID
    }

    stages {
        stage('Generate ServiceNow Token') {
            steps {
                script {
                    env.API_TOKEN = "${TOKEN}"
                }
            }
        }

        stage('Build Update Payload') {
            steps {
                script {
                    // Parse JSON input
                    def updates = readJSON text: params.UPDATE_PAYLOAD

                    echo "Generated Update Payload: ${groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(updates))}"

                    // Call your update function dynamically
                    def update = CERUpdate(config:[logger:this, adminVerboseLogging:true])
                    def result = update.updateShpData(updates)
                }
            }
        }
    }
}
