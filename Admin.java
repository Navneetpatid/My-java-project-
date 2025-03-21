@Service
public class EngagementService {

    @Autowired
    private EngagementRepository engagementRepository;

    public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        StringBuilder logMessage = new StringBuilder();
        StringBuilder errorMessage = new StringBuilder();

        // Default values
        response.put("success", "true");
        response.put("gbgf", "");
        response.put("workspace", "");
        response.put("dpHost", "");
        response.put("mandatoryPlugins", new ArrayList<>());
        response.put("cp_admin_api_url", "");
        response.put("dp_host_url", "");
        response.put("errors", ""); // Default empty error field

        // Fetch Engagement Data
        Map<String, Object> engagementData = engagementRepository.getEngagementData(engagementId);
        if (engagementData == null) {
            errorMessage.append("Engagement ID not validated | ");
            logMessage.append("Engagement ID not found | ");
            response.put("success", "false");  
        } else {
            response.put("gbgf", engagementData.getOrDefault("gbgf", ""));
        }

        // Fetch Workspace Data
        Map<String, Object> workspaceData = engagementRepository.getWorkspaceData(engagementId, workspace);
        if (workspaceData == null) {
            errorMessage.append("Workspace not validated | ");
            logMessage.append("Workspace not found | ");
            response.put("success", "false");  
        } else {
            response.put("workspace", workspace);
            response.put("dpHost", workspaceData.getOrDefault("dp_host_url", ""));
        }

        // Fetch Mandatory Plugins
        List<String> mandatoryPlugins = engagementRepository.getMandatoryPlugins(engagementId);
        response.put("mandatoryPlugins", mandatoryPlugins.isEmpty() ? new ArrayList<>() : mandatoryPlugins);

        // Fetch DP Host URL
        String dpHostUrl = engagementRepository.getDpHostUrl(engagementId, workspace);
        response.put("dp_host_url", dpHostUrl != null ? dpHostUrl : "");
        if (dpHostUrl == null) logMessage.append("DP Host URL not found | ");

        // Fetch CP Admin API URL
        String cpAdminApiUrl = engagementRepository.getCpAdminApiUrl(engagementId, workspace);
        response.put("cp_admin_api_url", cpAdminApiUrl != null ? cpAdminApiUrl : "");
        if (cpAdminApiUrl == null) logMessage.append("CP Admin API URL not found | ");

        // Trim logs and errors
        response.put("logs", logMessage.length() > 0 ? logMessage.substring(0, logMessage.length() - 3) : "");
        response.put("errors", errorMessage.length() > 0 ? errorMessage.substring(0, errorMessage.length() - 3) : "");

        return Collections.singletonList(response);
    }
            }
