import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public Map<String, Object> getApiResponse(String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true); // Default success status
        response.put("errors", ""); // Default empty error message
        response.put("logs", ""); // Default empty logs

        try {
            // Retrieve data from repository
            String cpUrl = dataRepository.findCpUrl();
            List<String> mandatoryPlugins = dataRepository.findMandatoryPlugins(engagementId);
            List<String> workspaces = dataRepository.findWorkspace(engagementId);
            String dpHost = dataRepository.findDpHost(engagementId, workspace);
            String gbgf = dataRepository.findGbgf(engagementId);
            String dmzLb = dataRepository.findDmzLb(engagementId);
            String logs = dataRepository.findLogs(engagementId);

            // Handle null values & add log messages
            response.put("cp_url", validateAndLog(cpUrl, "cp_url", response));
            response.put("mandatoryPlugins", validateAndLog(mandatoryPlugins, "mandatoryPlugins", response));
            response.put("workspace", validateAndLog(workspaces, "workspace", response));
            response.put("dpHost", validateAndLog(dpHost, "dpHost", response));
            response.put("gbgf", validateAndLog(gbgf, "gbgf", response));
            response.put("dmz_lb", validateAndLog(dmzLb, "dmz_lb", response));
            response.put("logs", logs != null ? logs : ""); // Logs should only be for errors

        } catch (Exception e) {
            response.put("success", false);
            response.put("errors", e.getMessage());
            response.put("logs", response.get("logs") + " Error occurred while fetching data.");
        }

        return response;
    }

    private <T> Object validateAndLog(T value, String fieldName, Map<String, Object> response) {
        if (value == null || (value instanceof List && ((List<?>) value).isEmpty())) {
            response.put("logs", response.get("logs") + fieldName + " Not Found; ");
            return "Not Found";
        }
        return value;
    }
}
