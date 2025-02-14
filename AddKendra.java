import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostRequestExample {
    public static void main(String[] args) {
        try {
            // API URL
            String urlString = "https://itid-fwk-api-kong-test.ikp301x.cloud.hk.hsbc/cto-ea-sn-change/api/v1/hsbcc/itsm";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // **Fix: Set DoOutput before writing data**
            conn.setDoOutput(true);

            // Setting request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic g+Ojx5b3VyIHNlcnZpY2U6cGFzc3dvcmQ=");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Custom", "<your businessUnit>|<your businessAppId>");

            // JSON payload
            String jsonInputString = "{"
                    + "\"assigned_to\": \"45330159@hsbc.com\","
                    + "\"assignment_group\": \"HTSA-MULESOFT-INTEGRATION-TECHNOLOGY\","
                    + "\"backout_plan\": \"Implement a test backout plan\","
                    + "\"business_service\": \"HAP-MULESOFT-HK-IF10-UATF\","
                    + "\"category\": \"Software\","
                    + "\"cmdb_ci\": \"\","
                    + "\"comments\": \"Created a test Change Request via API\","
                    + "\"description\": \"Test Change Request via API\","
                    + "\"end_date\": \"2025-07-20 11:00:00\","
                    + "\"implementation_plan\": \"This is the implementation plan\","
                    + "\"justification\": \"This Change request is required to test the API\","
                    + "\"requestor\": \"45212039@hsbc.com\","
                    + "\"risk_impact_analysis\": \"Risk impact analysis test\","
                    + "\"service_offering\": \"UDF App Support\","
                    + "\"short_description\": \"Test change request short description\","
                    + "\"start_date\": \"2025-07-20 08:00:00\","
                    + "\"state\": \"-5\","
                    + "\"test_plan\": \"Test plan for test change request\","
                    + "\"type\": \"normal\","
                    + "\"u_backout_capability\": \"10\","
                    + "\"u_backout_duration\": \"23\","
                    + "\"u_nature_of_change\": \"20\""
                    + "}";

            // **Fix: Write data after setting doOutput(true)**
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Getting response
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
