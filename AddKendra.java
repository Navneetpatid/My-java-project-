def processJson() {
    String filename = "mycsv.csv"
    String directory = "./output"  // Change this to a valid writable directory
    File dir = new File(directory)

    if (!dir.exists()) {
        dir.mkdirs()  // Create directory if it doesn't exist
    }

    String filePath = directory + "/" + filename  // Construct full path
    println "File path: ${filePath}"

    if (myFinalOutput?.trim()) {  // Ensure myFinalOutput is not null or empty
        println "debug23"
        try {
            File file = new File(filePath)
            file.text = myFinalOutput  // Writing the file
            println "debug24"
            println "CSV file written successfully: ${filePath}"
        } catch (Exception e) {
            println "Error writing CSV file: ${e.message}"
        }
    } else {
        println "Error: myFinalOutput is empty or null!"
    }
}
