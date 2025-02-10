try {
    throw new Exception("Error while fetching gcloud output response");
} catch (Exception e) {
    println("Error while fetching gcloud output response" + e.getMessage());
}

String filename = "mycsv.csv";
println "Filename for writing: ${filename}";
println "Filename for content file: ${contentFile}";
writeFile file: "./Report/${filename}", text: "${contentFile}";

println "Write file executed successfully - send mail list ${email_reciever}";

String responseBody = readFile encoding: 'UTF-8', file: "./Report/${filename}";
println "Response body: ${responseBody}";
println "Read file executed successfully";

String tempworkspaceName1 = "${workspace_name}";
String tempworkspaceName = tempworkspaceName1.replace("-DEV", "");

println "Sending Email to Recipients ${filename}";

String message = """Hi Team,

This is an auto-generated mail for ${tempworkspaceName}.

Please find the attached report for Workspace, Services, Routes, and Plugins.

For any queries, please reach out to Team.
""";

def html_body = sh(script: "cat test-output/emailable-report.html", returnStdout: true).trim();
message += "\n\nThanks\n";

emailext(
    to: "${email_reciever}",
    subject: "Admin API Pipeline Report - ${tempworkspaceName}",
    attachmentsPattern: "./Report/${filename}",
    body: "${html_body}",
    mimeType: 'text/html'
);

def CP() {
    credentials {
        usernamePassword {
            credentialsId: kongDeploymentAccountCredID
        }
    }
                                    }
