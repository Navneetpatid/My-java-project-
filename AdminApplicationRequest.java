import groovy.json.JsonSlurper
import groovy.json.JsonOutput

// Define file paths
String jsonFilePath = "${env.WORKSPACE}/data.json"
String csvFilePath = "${env.WORKSPACE}/output.csv"

// Read JSON file
echo "Reading JSON file from: ${jsonFilePath}"
def jsonFile = new File(jsonFilePath)
if (!jsonFile.exists()) {
    error "JSON file not found at ${jsonFilePath}"
}

def jsonData = new JsonSlurper().parseText(jsonFile.text)

// Convert JSON to CSV
def csvContent = new StringBuilder()
def keys = jsonData[0].keySet().join(",")  // Extract headers
csvContent.append(keys).append("\n")

jsonData.each { row ->
    csvContent.append(row.values().join(",")).append("\n")
}

// Write CSV file
echo "Writing CSV file to: ${csvFilePath}"
new File(csvFilePath).text = csvContent.toString()

// Read the generated CSV file
String responseBody = new File(csvFilePath).text
echo "CSV File Content:\n${responseBody}"

// Send an email with the CSV file attached
echo "Sending Email with CSV file..."
emailext(
    to: "navneet.patidar@noexternalmail.hsbc.com",
    subject: "Admin API Pipeline Report",
    body: """Hi Team,

This is an auto-generated email.
Please find the attached report.

Regards,
Automated System""",
    attachmentsPattern: csvFilePath
)

echo "Process completed successfully!"
