import groovy.lang.*
import groovy.transform.Field

@Field def logger = new Logger()

def call() {
    node {
        try {
            stage('Print Parameters') {
                logger.info("Engagement ID: ${params.engagementid}")
                logger.info("Email ID: ${params.Email_Id}")
                logger.info("PURL: ${params.purl}")
                logger.info("Release Nexus Version URL: ${params.releaseNexusVersionUrl}")
                logger.info("Rollback Nexus Version URL: ${params.rollbackNexusVersionUrl}")
            }

            stage('Send Email') {
                // Build Email Body using prepareBody()
                def emailBody = prepareBody(
                    true,
                    params.purl,
                    params.releaseNexusVersionUrl,
                    params.rollbackNexusVersionUrl
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
            error("Pipeline failed : ${e.message}")
        }
    }
}

/**
 * Build the HTML body for email
 */
def prepareBody(def purlGeneration, def purl, def releaseNexusVersionUrl, def rollbackNexusVersionUrl) {
    def emailBody = ""

    if (purlGeneration) {
        emailBody += """
        <h3>AAP Execution Using HAP Deployment Service</h3>
        <p>Please use the below package ID PURL in the Release Task:</p>
        <table border="1" style="border-collapse: collapse; width: 100%;">
            <tr>
                <th style="padding: 8px; text-align: left;">Field</th>
                <th style="padding: 8px; text-align: left;">Value</th>
            </tr>
            <tr>
                <td style="padding: 8px;">PURL</td>
                <td style="padding: 8px;">${purl}</td>
            </tr>
        </table>

        <h3>Use the below values while running the Production Pipeline:</h3>
        <table border="1" style="border-collapse: collapse; width: 100%;">
            <tr>
                <th style="padding: 8px; text-align: left;">Field</th>
                <th style="padding: 8px; text-align: left;">Value</th>
            </tr>
            <tr>
                <td style="padding: 8px;">Release Nexus Version</td>
                <td style="padding: 8px;">${releaseNexusVersionUrl}</td>
            </tr>
            <tr>
                <td style="padding: 8px;">Rollback Nexus Version</td>
                <td style="padding: 8px;">${rollbackNexusVersionUrl}</td>
            </tr>
        </table>
        """
    }
    return emailBody
}
