stage('Send Email') {
    def emailBody = """
        <h3>Pipeline Execution Details</h3>
        
        <h3>Node Availability</h3>
        <p>Below are the details of Jenkins nodes:</p>
        
        <table border="1" style="border-collapse: collapse; width: 100%;">
            <tr>
                <th style="padding: 8px; text-align: left;">Offline Nodes</th>
                <td style="padding: 8px;">${params.offlineNodes}</td>
            </tr>
            <tr>
                <th style="padding: 8px; text-align: left;">Online Nodes</th>
                <td style="padding: 8px;">${params.onlineNodes}</td>
            </tr>
        </table>
        
        <br/>
        <h3>Use the below values while running the Production Pipeline:</h3>
        <table border="1" style="border-collapse: collapse; width: 100%;">
            <tr>
                <th style="padding: 8px; text-align: left;">Release Nexus Version</th>
                <td style="padding: 8px;">${params.releaseNexusVersionUrl}</td>
            </tr>
            <tr>
                <th style="padding: 8px; text-align: left;">Rollback Nexus Version</th>
                <td style="padding: 8px;">${params.rollbackNexusVersionUrl}</td>
            </tr>
        </table>
    """

    emailext(
        to: "${params.Email_Id}",
        subject: "Pipeline Execution Report : ${params.engagementid}",
        body: emailBody
    )
        }
