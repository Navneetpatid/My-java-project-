import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DeploymentService {

    @Autowired
    private WebClient webClient;

    public String getDeploymentDetails() {
        String url = "https://devops.uat.it.global.hsbc/deploycontrol/api/v1/deployments/firehose" +
                     "?deploymentToolId=c7f606cb-a5a8-45b4-8bae-67b2c6c94817" +
                     "&date[0]=2025-06-30T00%3A00%3A00.007" +
                     "&date[1]=2025-06-30T23%3A59%3A59.000Z";

        return webClient.get()
                .uri(url)
                .header("Authorization", "Basic NDUyODl0NDIjfZGV20lBaY2FraFVkcVLeHlLS2UQnZWbUhFZU9sa1Bi3BtZjNTNm1qa1I3RjA=")
                .header("accept", "application/json")
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking call for sync use
    }
}//next class code below
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}//next class code 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
