import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.nio.file.*;
import java.io.IOException;

public class PostRequestExample {
    public static void main(String[] args) {
        String url = "https://your-api-endpoint.com"; // Replace with actual API URL
        String jsonPayload = readJsonFromFile("data.json"); // File containing JSON

        if (jsonPayload != null) {
            sendPostRequest(url, jsonPayload);
        }
    }

    public static String readJsonFromFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return null;
        }
    }

    public static void sendPostRequest(String url, String jsonPayload) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error making POST request: " + e.getMessage());
        }
    }
}
