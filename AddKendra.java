import java.text.SimpleDateFormat

@Field String rundate

def call(Map config) {
    this.config = config

    node {
        // Logger and other initializations
        logger = new Logger()
        pushMiEventToSplunk = new PushMiEventToSplunk(logger)
        miEvent = new MIEvent()
        splunkRequestBody = new SplunkRequestBody()

        // Pipeline stages
        stage('Checkout') {
            steps {
                script {
                    // Clean workspace
                    cleanWs()

                    // Checkout the repository (if applicable)
                    gitCheckout()

                    // Define the file path
                    def resourcesDir = "${env.WORKSPACE}/resources"
                    def emailDataFile = "${resourcesDir}/email_data.txt"

                    // Ensure the resources folder and file exist
                    if (!fileExists(resourcesDir)) {
                        error "Resources directory not found: ${resourcesDir}"
                    }
                    if (!fileExists(emailDataFile)) {
                        error "File not found: ${emailDataFile}"
                    }

                    // Append status to email_data.txt
                    sh "echo 'DATE ENVIRONMENT CP DATAPLANE STATUS' >> ${emailDataFile}"

                    // List contents of resources folder for debugging
                    sh "ls -l ${resourcesDir}"

                    // Read and print the file content
                    def fileContent = readFile(emailDataFile)
                    echo "File Content:\n${fileContent}"
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Example health check method (replace with actual logic)
                    performHealthCheckFromResources('email_data.txt', 200)
                }
            }
        }

        post {
            always {
                echo "Pipeline execution completed."
            }
        }
    }
}
