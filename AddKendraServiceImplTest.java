import Logger   // make sure Logger.groovy is available in vars/ or src/

def call() {
    node {
        try {
            stage('Print Parameters') {
                Logger.info("Engagement ID: ${params.engagementid}")
                Logger.info("EMI ID: ${params.emid}")
                Logger.info("URL: ${params.URL}")
                Logger.info("Email ID: ${params.Email_Id}")
            }

            stage('Split and Print URL') {
                def parts = splitUrl(params.URL)
                Logger.debug("Splitting URL: ${params.URL}")
                Logger.info("URL Parts:")
                parts.each { part ->
                    Logger.trace("Part => ${part}")
                }
            }

            stage('Send Email') {
                def emailBody = """
                    <h3>Pipeline Execution Details</h3>
                    <ul>
                        <li><b>ENGAGEMENT_ID:</b> ${params.engagementid}</li>
                        <li><b>MANDATORY_PLUGIN:</b> ${params.emid}</li>
                        <li><b>REGION:</b> ${params.URL}</li>
                    </ul>
                """

                emailext(
                    to: "${params.Email_Id}",
                    subject: "Pipeline Execution Report : ${params.engagementid}",
                    body: emailBody,
                    mimeType: 'text/html'
                )

                Logger.info("Email sent successfully to ${params.Email_Id}")
            }

        } catch (Exception e) {
            Logger.error("Error while running TestPipeline: ${e.message}")
            error("Pipeline failed: ${e.message}")
        }
    }
}

def splitUrl(String url) {
    return url.tokenize(".")
    }
