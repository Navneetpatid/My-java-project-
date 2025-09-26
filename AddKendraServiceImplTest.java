def call() {
    node {
        try {
            stage('Send Email') {
                // Hardcoded values
                def offlineNodes = "20099424, AI-ML-BOX, AIML_hk125079141.hc.cloud.hk.hsbc, cm-linux-gbl"
                def onlineNodes  = "Built-In Node, cm-linux-20098327, cm-linux-20099424, cm-windows-25048493"

                def emailBody = """
                <h3>Node Availability</h3>
                <p>Below are the details of Jenkins nodes:</p>

                <table border="1" style="border-collapse: collapse; width: 100%;">
                    <tr>
                        <th style="padding: 8px; text-align: left;">Node Type</th>
                        <th style="padding: 8px; text-align: left;">Nodes</th>
                        <th style="padding: 8px; text-align: left;">Status</th>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Offline Nodes</td>
                        <td style="padding: 8px;">${offlineNodes}</td>
                        <td style="padding: 8px; color: red; font-weight: bold;">Failed</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Online Nodes</td>
                        <td style="padding: 8px;">${onlineNodes}</td>
                        <td style="padding: 8px; color: green; font-weight: bold;">Success</td>
                    </tr>
                </table>
                """

                emailext(
                    to: "${params.Email_Id}",
                    subject: "Pipeline offline/online Node Report",
                    body: emailBody,
                    mimeType: 'text/html'
                )
            }
        } catch (Exception e) {
            logger.error("Pipeline failed: ${e.message}")
            throw e
        }
    }
}
