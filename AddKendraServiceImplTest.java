@Field def logger = new Logger()

def call() {
    node {
        try {
            stage('Print Parameters') {
                logger.info("Email ID: ${params.Email_Id}")
            }

            stage('Send Email') {
                // Hardcoded values
                def offlineNodes = "20099424, AI-ML-BOX, AINode-Offline-Example"
                def onlineNodes = "Built-In Node, cm-linux-20098327, cm-windows-25048493"

                def emailBody = """
                    <h3>Pipeline Execution Details</h3>
                    <h3>Node Availability</h3>
                    <p>Below are the details of Jenkins nodes:</p>

                    <table border="1" style="border-collapse: collapse; width: 100%;">
                        <tr>
                            <th style="padding: 8px; text-align: left;">Node Type</th>
                            <th style="padding: 8px; text-align: left;">Nodes</th>
                        </tr>
                        <tr>
                            <td style="padding: 8px;">Offline Nodes</td>
                            <td style="padding: 8px;">${offlineNodes}</td>
                        </tr>
                        <tr>
                            <td style="padding: 8px;">Online Nodes</td>
                            <td style="padding: 8px;">${onlineNodes}</td>
                        </tr>
                    </table>
                """

                emailext(
                    to: "${params.Email_Id}",
                    subject: "Pipeline Execution Report",
                    body: emailBody
                )
            }
        } catch (Exception e) {
            logger.error("Pipeline failed: ${e.message}")
            throw e
        }
    }
}
