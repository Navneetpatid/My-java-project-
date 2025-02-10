stage('Notification') {
    steps {
        script {
            def csvFilePath = "output.csv"  // Ensure this matches your generated CSV filename

            emailext subject: "CSV Report Notification",
                     body: "Hello,\n\nThe CSV report has been generated. Please find the attachment.\n\nBest regards.",
                     to: "recipient@example.com",
                     attachFiles: csvFilePath,
                     mimeType: 'text/csv'
        }
    }
}
