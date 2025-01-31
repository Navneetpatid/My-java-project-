import java.net.HttpURLConnection
import java.net.URL
import javax.mail.*
import javax.mail.internet.*

// Function to perform the health check
def healthCheck() {
    def file = new File("resources/URL.txt")
    def urls = file.readLines()
    def results = []

    urls.each { url ->
        try {
            URL site = new URL(url)
            HttpURLConnection connection = (HttpURLConnection) site.openConnection()
            connection.setRequestMethod("GET")
            connection.setConnectTimeout(5000)
            connection.connect()
            int statusCode = connection.getResponseCode()
            if (statusCode == 200) {
                results << "${url} - PASSES (Status: ${statusCode})"
            } else {
                results << "${url} - FAILED (Status: ${statusCode})"
            }
        } catch (Exception e) {
            results << "${url} - ERROR: ${e.message}"
        }
    }

    def resultFile = new File("health_results.txt")
    resultFile.text = results.join("\n")
    return results.join("\n")
}

// Function to send email notification
def sendEmail(reportContent) {
    def emailHost = "smtp.your-email-provider.com" // Replace with your SMTP host
    def emailPort = "587" // Replace with the correct port
    def username = "your-email@example.com" // Replace with your email address
    def password = "your-email-password" // Replace with your email password
    def recipient = "recipient@example.com" // Replace with the recipient's email address

    Properties props = new Properties()
    props.put("mail.smtp.host", emailHost)
    props.put("mail.smtp.port", emailPort)
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password)
        }
    })

    try {
        Message message = new MimeMessage(session)
        message.setFrom(new InternetAddress(username))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
        message.setSubject("Health Check Report")
        message.setText("Here is the health check report:\n\n${reportContent}")

        Transport.send(message)
        println "Email sent successfully."
    } catch (MessagingException e) {
        println "Failed to send email: ${e.message}"
    }
}

// Main code execution
def reportContent = healthCheck()
sendEmail(reportContent)
