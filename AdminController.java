response = sh(
    script: """
        set +x;
        curl --silent -k -b cookie.txt --location --request GET https://${CP}/admin/license/report
    """,
    returnStdout: true
)

echo "https://${CP}/admin/license/report"
echo "getworkspaceservices response starts"

echo "${response}"

// Define the filename
String filename = "${filename}_license.json"

// Write the response to a JSON file
writeFile file: filename, text: response
echo "getworkspaceservices response ends"

// Read the file
File file = new File(filename)
if (file.exists()) {
    String fileContent = file.text
    echo "Read file executed successfully"
    echo "File Content:\n${fileContent}"
} else {
    echo "File not found: ${filename}"
}

return 0;
