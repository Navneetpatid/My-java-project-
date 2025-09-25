@Library('MuleSoft-Jenkins-Pipeline-v2.0') _

import com.hsbc.hap.aap.email.emailNotification

def call() {
    node {
        try {
            stage('Send Email') {
                // Build Email Body dynamically using prepareBody()
                def emailBody = emailNotification.prepareBody(
                    true,                                // purlGeneration (true if you want PURL section in email)
                    params.purl,                         // PURL
                    params.releaseNexusVersionUrl,       // Release Nexus Version
                    params.rollbackNexusVersionUrl       // Rollback Nexus Version
                )

                emailext(
                    to: "${params.Email_Id}",
                    subject: "Pipeline Execution Report : ${params.engagementid}",
                    body: emailBody,
                    mimeType: 'text/html'
                )

                logger.info("Email sent successfully to ${params.Email_Id}")
            }
        } catch (Exception e) {
            logger.error("Error while running TestPipeline: ${e.message}")
            error("Pipeline failed: ${e.message}")
        }
    }
}
