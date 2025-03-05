// Define the filename
String filename = "${filename}_license.json"

// Write the response to a JSON file
writeFile file: filename, text: response
echo "getworkspaceservices response ends"

// Read the file correctly
try {
    def file = new File(filename)
    if (file.exists()) {
        String fileContent = file.text
        echo "Read file executed successfully"
        echo "File Content:\n${fileContent}"
    } else {
        echo "File not found: ${filename}"
    }
} catch (Exception e) {
    echo "Error reading file: ${e.getMessage()}"
}

return 0;
