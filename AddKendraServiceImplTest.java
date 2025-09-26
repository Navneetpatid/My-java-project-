import groovy.json.*
import groovy.lang.*
import jenkins.model.Jenkins
@Field def logger = new Logger()

def call() {
    node {
        try {
            stage('Print Parameters') {
                logger.info("Email ID: ${params.Email_Id}")
            }

            stage('Send Email') {
                // --- Dynamic Jenkins node discovery ---
                def offlineNodes = Jenkins.instance.computers.findAll { !it.online }
                def onlineNodes  = Jenkins.instance.computers.findAll { it.online }

                // Convert to list of names
                def offlineNodeList = offlineNodes.collect { it.displayName.trim() }
                def onlineNodeList  = onlineNodes.collect { it.displayName.trim() }

                // --- Build email body ---
                def emailBody = """
                <h3>Node Availability</h3>
                <p>Below are the details of Jenkins nodes:</p>
                <table border="1" style="border-collapse: collapse; width: 100%;">
                  <tr>
                    <th style="padding: 8px; text-align: left;">Node Type</th>
                    <th style="padding: 8px; text-align: left;">Node</th>
                    <th style="padding: 8px; text-align: left;">Status</th>
                  </tr>
                """

                // Add offline nodes
                offlineNodeList.each { nodeName ->
                    emailBody += """
                      <tr>
                        <td style="padding: 8px;">Agent</td>
                        <td style="padding: 8px;">${nodeName}</td>
                        <td style="padding: 8px; color: red;">Failed</td>
                      </tr>
                    """
                }

                // Add online nodes
                onlineNodeList.each { nodeName ->
                    emailBody += """
                      <tr>
                        <td style="padding: 8px;">Agent</td>
                        <td style="padding: 8px;">${nodeName}</td>
                        <td style="padding: 8px; color: green;">Success</td>
                      </tr>
                    """
                }

                emailBody += "</table>"

                // --- Send email ---
                emailext(
                    to: "${params.Email_Id}",
                    subject: "Jenkins Node Availability Report",
                    body: emailBody,
                    mimeType: 'text/html'
                )
            }
        } catch (Exception e) {
            logger.error("Error occurred: ${e.message}")
            throw e
        }
    }
                    }
