import java.nio.file.*

println "Read file executed successfully"

String filename = "mycsv.csv"
Path filePath = Paths.get(System.getProperty("user.dir"), filename) // Creates file in current working directory

if (myFinalOutput?.trim()) {  // Ensure myFinalOutput is not null or empty
    try {
        Files.write(filePath, myFinalOutput.getBytes()) // Write content to file
        println "CSV file written successfully: ${filePath}"

        // Verify if the file exists before sending email
        if (Files.exists(filePath)) {
            println "File exists: ${filePath}"

            // Send Email
            emailExt(
                to: "navneet.patidar@noexternalmail.hsbc.com",
                subject: "License Report",
                attachmentsPattern: filePath.toString(),
                body: "Please find the attached report.",
                mimeType: "text/html"
            )
            println "Email sent successfully with attachment: ${filePath}"
        } else {
            println "Error: File not found!"
        }
    } catch (Exception e) {
        println "Error writing CSV file: ${e.message}"
    }
} else {
    println "Error: myFinalOutput is empty or null!"
                }
