import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostRequestExample {
    public static void main(String[] args) {
        try {
            // API URL
            String urlString = "https://itid-fwk-api-kong-test.ikp301x.cloud.hk.hsbc/cto-ea-sn-change/api/v1/hsbcc/itsm/change";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Setting request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic g+Ojx5b3VyIHNlcnZpY2UgYWNjb3VudCBwYXNzd29yZD4=");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Custom", "<your businessUnit>|<your businessAppId>");
            conn.setDoOutput(true);

            // JSON payload
            String jsonInputString = "{"
                    + "\"assigned_to\": \"<your psid>@hsbc.com\","
                    + "\"assignment_group\": \"<your Assignment group 2>\","
                    + "\"backout_plan\": \"Implement a test backout plan\","
                    + "\"business_service\": \"UDF\","
                    + "\"category\": \"Network\","
                    + "\"cmdb_ci\": \"<your cmdb_ci>\","
                    + "\"comments\": \"Created a test Change Request via API\","
                    + "\"description\": \"Test Change Request via API\","
                    + "\"end_date\": \"2024-07-20 11:00:00\","
                    + "\"implementation_plan\": \"This is the implementation plan\","
                    + "\"justification\": \"This Change request is required to test the API\","
                    + "\"requestor\": \"<your psid>@hsbc.com\","
                    + "\"risk_impact_analysis\": \"Risk impact analysis test\","
                    + "\"service_offering\": \"UDF App Support\","
                    + "\"short_description\": \"Test change request short description\","
                    + "\"start_date\": \"2024-07-20 08:00:00\","
                    + "\"state\": \"-5\","
                    + "\"test_plan\": \"Test plan for test change request\","
                    + "\"type\": \"normal\","
                    + "\"u_backout_capability\": \"10\","
                    + "\"u_backout_duration\": \"23\","
                    + "\"u_nature_of_change\": \"20\","
                    + "\"u_post_implementation_verification_capability\": \"10\","
                    + "\"u_publicity_andor_regulatory_awareness\": \"20\","
                    + "\"u_post_implementation_verification_capability_textbox\": \"test\","
                    + "\"u_backout_capability_textbox\": \"test\","
                    + "\"u_publicity_andor_regulatory_textbox\": \"This is the Publicity and Regulatory Textbox text\","
                    + "\"u_technical_impact_details\": \"10\","
                    + "\"u_change_purpose\": \"new_features\""
                    + "}";

            // Sending request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Getting response
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response if successful
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                }
            } else {
                System.out.println("Failed to send request. Response Code: " + responseCode);
            }

            // Close the connection
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
