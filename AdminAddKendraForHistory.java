import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public Map<String, Object> getApiResponse(String region, String platform, String environment, String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true); // Default success status
        response.put("errors", ""); // Default empty error message
        response.put("logs", ""); // Default empty logs

        try {
            // Retrieve data from repository
            String cpUrl = dataRepository.findCpUrl(region, platform, environment);
            List<String> mandatoryPlugins = dataRepository.findMandatoryPlugins(engagementId);
            List<String> workspaces = dataRepository.findWorkspace(engagementId);
            String dpHost = dataRepository.findDpHost(engagementId, workspace);
            String gbgf = dataRepository.findGbgf(engagementId);
            String dmzLb = dataRepository.findDmzLb(engagementId);

            // Handle null values & add log messages
            response.put("cp_url", cpUrl != null ? cpUrl : logAndReturn("cp_url Not Found", response));
            response.put("mandatoryPlugins", mandatoryPlugins != null && !mandatoryPlugins.isEmpty() ? mandatoryPlugins : logAndReturn("mandatoryPlugins Not Found", response));
            response.put("workspace", workspaces != null && !workspaces.isEmpty() ? workspaces : logAndReturn("workspace Not Found", response));
            response.put("dpHost", dpHost != null ? dpHost : logAndReturn("dpHost Not Found", response));
            response.put("gbgf", gbgf != null ? gbgf : logAndReturn("gbgf Not Found", response));
            response.put("dmz_lb", dmzLb != null ? dmzLb : logAndReturn("dmz_lb Not Found", response));

        } catch (Exception e) {
            response.put("success", false);
            response.put("errors", e.getMessage());
            response.put("logs", response.get("logs") + " Error occurred while fetching data.");
        }

        return response;
    }

    private String logAndReturn(String logMessage, Map<String, Object> response) {
        response.put("logs", response.get("logs") + logMessage + "; ");
        return null;
    }
        }
