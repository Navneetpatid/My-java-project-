import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurlEquivalentService {

    private static final Logger logger = LoggerFactory.getLogger(CurlEquivalentService.class);
    private final RestTemplate restTemplate;

    public CurlEquivalentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchChangeList() {
        String url = "https://controls.uat.eq.gbm.cloud.hk.hsbc/jon-snow/api/v1/servicenow/hsbcc/itsm/change";

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Response received successfully");
            return response.getBody();
        } else {
            logger.error("Failed to fetch data. Status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to fetch change list");
        }
    }
}
