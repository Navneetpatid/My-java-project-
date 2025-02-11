if (myFinalOutput?.trim()) {  // Ensure myFinalOutput is not null or empty
    try {
        println "Attempting to write to file: mycsvfile.csv"
        writeFile file: 'mycsvfile.csv', text: myFinalOutput  // Writing the file
        println "CSV file written successfully: mycsvfile.csv"

        // Verify if the file exists before sending an email
        def filePath = new File("mycsvfile.csv")
        if (filePath.exists()) {
            println "File exists: ${filePath.absolutePath}"
        } else {
            println "Error: File was not created successfully."
        }
    } catch (Exception e) {
        println "Error writing file: ${e.message}"
        e.printStackTrace()
    }
} else {
    println "Skipping file write: myFinalOutput is null or empty."
            }
