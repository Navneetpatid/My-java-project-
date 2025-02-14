import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class URLChecker {
    public static void main(String[] args) {
        try {
            // API URL
            String urlString = "https://itid-fwk-api-kong-test.ikp301x.cloud.hk.hsbc/cto-ea-sn-change/api/v1/hsbcc/itsm/change";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            conn.setRequestMethod("GET");

            // Replace with valid credentials
            String username = "your_username";
            String password = "your_password";
            String encodedAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);

            // Replace with actual business details
            conn.setRequestProperty("X-Custom", "yourBusinessUnit|yourBusinessAppId");

            // Get response code
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Close connection
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
