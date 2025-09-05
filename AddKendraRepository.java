def addShpData(def engagementId, def einId, def platform, def bgpF, def namespace,
               def cluster, def environment, def project, def region, def proxy,
               def deployUtilityName, def helmChartNexusUrl, def gcpNode) {
    def token = generateToken()
    def returnVal = ""
    try {
        echo "Add SHP Data API - Started"

        dir(env.WORKSPACE) {
            def contentType = "Content-Type: application/json;charset=UTF-8"
            def fileName = "add_shp_response.json"
            def url = "${environmentRegisterURL}/cer/add/shp/data"

            // Build JSON payload exactly like Postman
            def jsonPayload = """{
                "engagementId": "${engagementId}",
                "einId": "${einId}",
                "platform": "${platform}",
                "bgpF": "${bgpF}",
                "namespace": "${namespace}",
                "cluster": "${cluster}",
                "environment": "${environment}",
                "project": "${project}",
                "region": "${region}",
                "proxy": "${proxy}",
                "deploy_utility_name": "${deployUtilityName}",
                "helm_chart_nexus_url": "${helmChartNexusUrl}",
                "gcpNode": "${gcpNode}"
            }"""

            // Save request body to file
            writeFile file: "shp_request.json", text: jsonPayload

            // Run curl
            def command = """curl -s -o ${fileName} -w "%{http_code}" \
                -H "${contentType}" \
                -H "X-HSBC-E2E-Trust-Token: ${token}" \
                -X POST "${url}" \
                --data @shp_request.json"""

            if (adminVerboseLogging) {
                echo "Executing: ${command}"
            }

            def statusCode = sh(script: command, returnStdout: true).trim()

            if (statusCode != "200") {
                echo "ERR - Service call to Add SHP Data API failed. HTTP ${statusCode}"
                returnVal = "error"
            } else {
                def response = readFile(fileName)
                echo "Response: ${response}"
                returnVal = response
            }
        }
    } catch (Exception e) {
        echo "ERR - Add SHP Data API call failed with unknown error: ${e.message}"
        returnVal = "error"
    }
    return returnVal
}

@Library('License_test@licenspec03') _ pipeline { agent any parameters { string(name: 'ENGAGEMENT_ID', defaultValue: 'HAP-COO-40010', description: 'Enter Engagement ID') string(name: 'EIN_ID', defaultValue: '1237', description: 'Enter EIN ID') string(name: 'PLATFORM', defaultValue: 'IKP', description: 'Enter Platform') string(name: 'BGPF', defaultValue: 'COO IT', description: 'Enter BGPF') string(name: 'NAMESPACE', defaultValue: 'hap-hk-cto-113', description: 'Enter Namespace') string(name: 'CLUSTER', defaultValue: 'ClusterTest123', description: 'Enter Cluster') string(name: 'ENVIRONMENT', defaultValue: 'DEV', description: 'Enter Environment') string(name: 'PROJECT', defaultValue: 'testProject1245', description: 'Enter Project') string(name: 'REGION', defaultValue: 'UK', description: 'Enter Region') string(name: 'PROXY', defaultValue: '1345', description: 'Enter Proxy') string(name: 'DEPLOY_UTILITY_NAME', defaultValue: 'deploy123', description: 'Enter Deploy Utility Name') string(name: 'HELM_CHART_NEXUS_URL', defaultValue: 'https://anypoint.mulesoft.com', description: 'Enter Helm Chart Nexus URL') string(name: 'GCP_NODE', defaultValue: 'node1234', description: 'Enter GCP Node') }

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

    stage('Fetch Add SHP Data') {
        steps {
            script {
                def result = kong.addShpData(
                    params.ENGAGEMENT_ID,
                    params.EIN_ID,
                    params.PLATFORM,
                    params.BGPF,
                    params.NAMESPACE,
                    params.CLUSTER,
                    params.ENVIRONMENT,
                    params.PROJECT,
                    params.REGION,
                    params.PROXY,
                    params.DEPLOY_UTILITY_NAME,
                    params.HELM_CHART_NEXUS_URL,
                    params.GCP_NODE
                )
            }
        }
    }
}

}

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
