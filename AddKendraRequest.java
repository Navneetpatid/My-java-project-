import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CurlRunner {

    public static void main(String[] args) {
        try {
            String url = "https://controls.uat.eq.gbm.cloud.hk.hsbc/jon-snow/api/v1/servicenow/hsbcc/itsm/change";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("accept", "application/json");

            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder responseContent = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine);
            }
            in.close();

            // Parse JSON using Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseContent.toString());

            JsonNode changeRequests = root.path("result").path("change_requests");

            if (changeRequests.isArray()) {
                for (JsonNode cr : changeRequests) {
                    String parent = cr.path("parent").asText();
                    String reason = cr.path("reason").asText(null);
                    String type = cr.path("type").asText();
                    String approvalHistory = cr.path("approval_history").asText();
                    String number = cr.path("number").asText();
                    String cmdbCi = cr.path("u_affected_cis").asText();

                    System.out.println("=== Change Request ===");
                    System.out.println("Parent: " + parent);
                    System.out.println("Reason: " + reason);
                    System.out.println("Type: " + type);
                    System.out.println("Approval History: " + approvalHistory);
                    System.out.println("Number: " + number);
                    System.out.println("CMDB CI: " + cmdbCi);
                    System.out.println("======================");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
