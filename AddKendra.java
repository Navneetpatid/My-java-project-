import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequestExample {
    public static void main(String[] args) {
        try {
            // API URL
            String urlString = "https://itid-fwk-api-kong-test.ikp301x.cloud.hk.hsbc/cto-ea-sn-change/api/v1/hsbcc/itsm/change";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Setting request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic g+Ojx5b3VyIHN1cnZpY2UgYWNjb29yd29yZD4=");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Custom", "<your businessUnit>|<your businessAppId>");

            // Get response code
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response if successful
            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the response
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("Failed to connect. Response Code: " + responseCode);
            }

            // Close connection
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
