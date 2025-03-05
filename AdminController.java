// Read the file correctly with debugging
try {
    // Print the expected filename
    echo "Attempting to read file: ${filename}"

    // Ensure the full path is correct
    def filePath = "${WORKSPACE}/${filename}_license.json"
    echo "Checking file at: ${filePath}"

    def file = new File(filePath)
    
    if (file.exists()) {
        // Read the file content
        String fileContent = file.getText('UTF-8')

        // Print success and content
        echo "Read file executed successfully"
        echo "File Content:\n${fileContent}"
    } else {
        // Print error if file does not exist
        echo "File not found: ${filePath}"
    }
} catch (Exception e) {
    // Catch any errors and print
    echo "Error reading file: ${e.getMessage()}"
}
