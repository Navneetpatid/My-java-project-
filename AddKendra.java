import java.nio.file.Files
import java.nio.file.Paths

def processJson() {
    jsonFilePaths.each { filePath ->
        println "Processing JSON file: ${filePath}"

        // Define CSV filename and path
        String filename = "mycsv.csv"
        String filePath = "./" + filename  // Adjust path as needed

        // Check if `myFinalOutput` has valid content
        if (myFinalOutput?.trim()) {
            // Write the CSV file
            new File(filePath).text = myFinalOutput
            println "CSV file written successfully: ${filePath}"
        } else {
            println "Error: myFinalOutput is empty or null!"
            return
        }
    }

    // Verify if file exists before sending email
    String csvFilePath = "./mycsv.csv"
    if (Files.exists(Paths.get(csvFilePath))) {
        println "File exists: ${csvFilePath}"

        // Send Email
        emailExt(
            to: "navneet.patidar@noexternalmail.hsbc.com",
            subject: "License Report",
            attachmentsPattern: csvFilePath,
            body: "Please find the attached report.",
            mimeType: "text/html"
        )
    } else {
        println "Error: File not found at ${csvFilePath}"
    }
}
