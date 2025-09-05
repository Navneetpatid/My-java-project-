def addKongData(def engagementID, def mandatory_plugin, def region, def network_region,
                def dp_platform, def bgpp, def workspace, def environment, def dp_host_url) {
    def token = generateToken()
    def returnVal = ""
    try {
        echo "Add Kong Data API - Started"

        dir(env.WORKSPACE) {
            def contentType = "Content-Type: application/json;charset=UTF-8"
            def fileName = "add_kong_response.json"
            def url = "${environmentRegisterURL}/cer/add/kong/data"

            // JSON payload
            def jsonPayload = """{
                "engagementId": "${engagementID}",
                "mandatory_plugin": "${mandatory_plugin}",
                "region": "${region}",
                "network_region": "${network_region}",
                "dp_platform": "${dp_platform}",
                "bgpp": "${bgpp}",
                "workspace": "${workspace}",
                "environment": "${environment}",
                "dp_host_url": "${dp_host_url}"
            }"""

            writeFile file: "kong_request.json", text: jsonPayload

            def command = """curl -s -w "%{http_code}" -o ${fileName} \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X POST "${url}" \
                -d @kong_request.json """

            if (adminVerboseLogging) {
                echo "Executing: ${command}"
            }

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "ERR - Service call to Add Kong Data API failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "ERR - Add Kong Data API call failed with unknown error: ${e.message}"
        returnVal = "error"
    }
    return returnVal
        }

@Library('license_test@licensrepo3')
pipeline {
    agent any
    parameters {
        string(name: 'ENGAGEMENT_ID', defaultValue: 'HAP-COO-40088', description: 'Enter Engagement ID')
        string(name: 'MANDATORY_PLUGIN', defaultValue: 'rate-limiting', description: 'Enter Mandatory Plugin')
        string(name: 'REGION', defaultValue: 'HK', description: 'Enter Region')
        string(name: 'NETWORK_REGION', defaultValue: 'DRN', description: 'Enter Network Region')
        string(name: 'DP_PLATFORM', defaultValue: 'GCP', description: 'Enter Data Platform')
        string(name: 'BGPF', defaultValue: 'COO IT', description: 'Enter BGPF')
        string(name: 'WORKSPACE', defaultValue: 'HAP-COO-40088-DEV', description: 'Enter Workspace')
        string(name: 'ENVIRONMENT', defaultValue: 'DEV', description: 'Enter Environment')
        string(name: 'DP_HOST_URL', defaultValue: 'kdp02uk-dev.ikp01r.cloud.uk.hsbc', description: 'Enter DP Host URL')
    }

    environment {
        TOKEN = credentials('SNOW_CREDENTIAL_ID')   // Jenkins Credential ID
    }

    stages {
        stage('Generate ServiceNow Token') {
            steps {
                script {
                    echo "Using ENGAGEMENT_ID: ${params.ENGAGEMENT_ID}"
                    echo "Using WORKSPACE: ${params.WORKSPACE}"
                    echo "Using ENVIRONMENT: ${params.ENVIRONMENT}"
                    echo "Ready to call Add Kong Data API with all parameters."
                }
            }
        }
    }
}

def result = kong.addKongData(
    params.ENGAGEMENT_ID,
    params.MANDATORY_PLUGIN,
    params.REGION,
    params.NETWORK_REGION,
    params.DP_PLATFORM,
    params.BGPF,
    params.WORKSPACE,
    params.ENVIRONMENT,
    params.DP_HOST_URL
)
