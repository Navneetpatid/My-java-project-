@Service
public class EngagementService {

    private final EngagementRepository engagementRepository;
    private final Logger logger = LoggerFactory.getLogger(EngagementService.class);

    @Autowired
    public EngagementService(EngagementRepository engagementRepository) {
        this.engagementRepository = engagementRepository;
    }

    public EngagementValidationResponse validateWorkspaceForEngagement(String engagementId, String workspace) {
        EngagementValidationResponse response = new EngagementValidationResponse();

        try {
            validateEngagement(engagementId, response);
            
            if (response.isSuccess()) {  // Only proceed if engagement is valid
                validateWorkspace(engagementId, workspace, response);
                
                if (response.isSuccess()) {  // Only proceed if workspace is valid
                    fetchAdditionalData(engagementId, workspace, response);
                }
            }

        } catch (Exception e) {
            handleException(response, e);
            logger.error("Error validating workspace for engagement {}: {}", engagementId, e.getMessage(), e);
        }

        return response;
    }

    private void validateEngagement(String engagementId, EngagementValidationResponse response) {
        try {
            Optional<Map<String, Object>> engagementData = Optional.ofNullable(
                engagementRepository.getEngagementData(engagementId));
            
            if (engagementData.isEmpty()) {
                response.setSuccess(false)
                       .appendError("Engagement ID not validated")
                       .appendLog("Engagement ID not found");
            } else {
                response.setGbgf(engagementData.get().getOrDefault("gbgf", "").toString());
            }
        } catch (DataAccessException e) {
            response.setSuccess(false)
                   .appendError("Database error while fetching engagement data")
                   .appendLog("Failed to fetch engagement data");
            logger.warn("Database error fetching engagement {}: {}", engagementId, e.getMessage());
        }
    }

    private void validateWorkspace(String engagementId, String workspace, EngagementValidationResponse response) {
        try {
            Optional<Map<String, Object>> workspaceData = Optional.ofNullable(
                engagementRepository.getWorkspaceData(engagementId, workspace));
            
            if (workspaceData.isEmpty()) {
                response.setSuccess(false)
                       .appendError("Workspace not validated")
                       .appendLog("Workspace not found");
            } else {
                response.setWorkspace(workspace)
                       .setDpHost(workspaceData.get().getOrDefault("dp_host_url", "").toString());
            }
        } catch (DataAccessException e) {
            response.setSuccess(false)
                   .appendError("Database error while fetching workspace data")
                   .appendLog("Failed to fetch workspace data");
            logger.warn("Database error fetching workspace {}/{}: {}", engagementId, workspace, e.getMessage());
        }
    }

    private void fetchAdditionalData(String engagementId, String workspace, EngagementValidationResponse response) {
        fetchMandatoryPlugins(engagementId, response);
        fetchDpHostUrl(engagementId, workspace, response);
        fetchCpAdminApiUrl(engagementId, workspace, response);
    }

    private void fetchMandatoryPlugins(String engagementId, EngagementValidationResponse response) {
        try {
            List<String> plugins = Optional.ofNullable(engagementRepository.getMandatoryPlugins(engagementId))
                                         .orElse(Collections.emptyList());
            response.setMandatoryPlugins(plugins);
        } catch (DataAccessException e) {
            response.appendLog("Failed to fetch mandatory plugins");
            logger.warn("Error fetching plugins for engagement {}: {}", engagementId, e.getMessage());
        }
    }

    private void fetchDpHostUrl(String engagementId, String workspace, EngagementValidationResponse response) {
        try {
            String url = engagementRepository.getDpHostUrl(engagementId, workspace);
            response.setDpHostUrl(Optional.ofNullable(url).orElse(""));
            if (url == null) {
                response.appendLog("DP Host URL not found");
            }
        } catch (DataAccessException e) {
            response.appendLog("Failed to fetch DP Host URL");
            logger.warn("Error fetching DP Host URL for {}/{}: {}", engagementId, workspace, e.getMessage());
        }
    }

    private void fetchCpAdminApiUrl(String engagementId, String workspace, EngagementValidationResponse response) {
        try {
            String url = engagementRepository.getCpAdminApiUrl(engagementId, workspace);
            response.setCpAdminApiUrl(Optional.ofNullable(url).orElse(""));
            if (url == null) {
                response.appendLog("CP Admin API URL not found");
            }
        } catch (DataAccessException e) {
            response.appendLog("Failed to fetch CP Admin API URL");
            logger.warn("Error fetching CP Admin API URL for {}/{}: {}", engagementId, workspace, e.getMessage());
        }
    }

    private void handleException(EngagementValidationResponse response, Exception e) {
        response.setSuccess(false)
               .appendError("Internal Server Error: " + e.getMessage())
               .appendLog("Exception occurred while processing request");
    }
    }
