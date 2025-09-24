def call() {
    stage('Print Parameters') {
        echo "Engagement ID: ${params.engagementid}"
        echo "EIM ID: ${params.eimid}"
        echo "URL: ${params.URL}"
        echo "Email ID: ${params.Email_Id}"
    }

    stage('Split and Print URL') {
        script {
            def parts = params.URL.tokenize('/')
            echo "URL Parts:"
            parts.each { part ->
                echo part
            }
        }
    }

    stage('Send Email') {
        def emailBody = """
            <h3>Pipeline Execution Details</h3>
            <ul>
                <li><b>ENGAGEMENT_ID:</b> ${params.engagementid}</li>
                <li><b>EIM_ID:</b> ${params.eimid}</li>
                <li><b>URL:</b> ${params.URL}</li>
            </ul>
        """

        emailext(
            to: "${params.Email_Id}",
            subject: "Pipeline Execution Report - ${params.engagementid}",
            body: emailBody,
            mimeType: 'text/html'
        )
    }
}
