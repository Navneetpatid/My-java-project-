stage('Send Email') {
    // Hardcoded values
    def offlineNodes = ["20099424", "AI-ML-BOX", "AIML_hk125079141.hc.cloud.hk.hsbc", "cm-linux-gb125048486"]
    def onlineNodes = ["Built-In Node", "cm-linux-20098327", "cm-linux-20099424", "cm-windows-25048493"]

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

    // Add offline nodes with Failed status
    offlineNodes.each { node ->
        emailBody += """
            <tr>
                <td style="padding: 8px;">Offline Node</td>
                <td style="padding: 8px;">${node}</td>
                <td style="padding: 8px; color: red; font-weight: bold;">Failed</td>
            </tr>
        """
    }

    // Add online nodes with Success status
    onlineNodes.each { node ->
        emailBody += """
            <tr>
                <td style="padding: 8px;">Online Node</td>
                <td style="padding: 8px;">${node}</td>
                <td style="padding: 8px; color: green; font-weight: bold;">Success</td>
            </tr>
        """
    }

    // Close table
    emailBody += "</table>"

    emailext(
        to: "${params.Email_Id}",
        subject: "Pipeline Offline/Online Node Report",
        body: emailBody,
        mimeType: 'text/html'
    )
}
