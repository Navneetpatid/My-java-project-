import java.nio.file.*

String filename = "mycsv.csv"
String filePath = "./Report/${filename}"

if (Files.exists(Paths.get(filePath))) {
    println "File exists: ${filePath}"
    
    // Send Email
    emailext(
        to: "navneet.patidar@noexternalmail.hsbc.com",
        subject: "License Report",
        attachmentsPattern: filePath,
        body: "Please find the attached report.",
        mimeType: "text/html"
    )
    println "Email sent successfully."
} else {
    println "File not found: ${filePath}. Email not sent."
        }
