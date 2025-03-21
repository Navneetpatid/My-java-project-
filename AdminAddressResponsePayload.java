import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/retrieve")
    public ApiResponse getData(
            @RequestParam String region,
            @RequestParam String platform,
            @RequestParam String environment,
            @RequestParam String engagementId) {
        return dataService.getData(region, platform, environment, engagementId);
    }
}
