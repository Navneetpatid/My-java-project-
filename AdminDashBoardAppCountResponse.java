@Service
@Transactional
public class WorkspaceValidationServiceImpl implements WorkspaceValidationService {

    private final EngagementTargetRepository engagementTargetRepo;
    private final WorkspaceTargetRepository workspaceTargetRepo;
    private final EngagementPluginRepository engagementPluginRepo;
    private final CpMasterRepository cpMasterRepo;

    @Autowired
    public WorkspaceValidationServiceImpl(
            EngagementTargetRepository engagementTargetRepo,
            WorkspaceTargetRepository workspaceTargetRepo,
            EngagementPluginRepository engagementPluginRepo,
            CpMasterRepository cpMasterRepo) {
        this.engagementTargetRepo = engagementTargetRepo;
        this.workspaceTargetRepo = workspaceTargetRepo;
        this.engagementPluginRepo = engagementPluginRepo;
        this.cpMasterRepo = cpMasterRepo;
    }

    @Override
    public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
        Map<String, Object> libDetails = new HashMap<>();
        StringBuilder logMessage = new StringBuilder();
        StringBuilder errorMessage = new StringBuilder();

        try {
            // Default values
            libDetails.put("success", "true");
            libDetails.put("gbgf", "");
            libDetails.put("workspace", "");
            libDetails.put("dpHost", "");
            libDetails.put("mandatoryPlugins", new ArrayList<>());
            libDetails.put("cp_admin_api_url", "");
            libDetails.put("dp_host_url", "");

            // Validate Engagement ID
            Optional<EngagementTarget> engagementOpt = engagementTargetRepo.findById(engagementId);
            if (engagementOpt.isEmpty()) {
                errorMessage.append("Engagement ID not validated | ");
                logMessage.append("Engagement ID not found | ");
            } else {
                EngagementTarget engagement = engagementOpt.get();
                libDetails.put("gbgf", engagement.getGbgf() != null ? engagement.getGbgf() : "");
            }

            // Validate Workspace
            Optional<WorkspaceTarget> workspaceOpt = workspaceTargetRepo.findByEngagementIdAndWorkspace(engagementId, workspace);
            if (workspaceOpt.isEmpty()) {
                errorMessage.append("Workspace not validated | ");
                logMessage.append("Workspace not found | ");
            } else {
                WorkspaceTarget workspaceTarget = workspaceOpt.get();
                libDetails.put("workspace", workspace);
                libDetails.put("dpHost", workspaceTarget.getDpHostUrl() != null ? workspaceTarget.getDpHostUrl() : "");
            }

            // Fetch Mandatory Plugins
            List<EngagementPlugin> plugins = engagementPluginRepo.findByEngagementId(engagementId);
            List<String> mandatoryPlugins = plugins.stream()
                .map(EngagementPlugin::getMandatoryPlugin)
                .collect(Collectors.toList());
            libDetails.put("mandatoryPlugins", mandatoryPlugins.isEmpty() ? new ArrayList<>() : mandatoryPlugins);

            // Fetch DP Host URL
            workspaceOpt.ifPresent(ws -> {
                String dpHostUrl = ws.getDpHostUrl();
                libDetails.put("dp_host_url", dpHostUrl != null ? dpHostUrl : "");
                if (dpHostUrl == null) logMessage.append("DP Host URL not found | ");
            });

            // Fetch CP Admin API URL
            Optional<String> cpAdminApiUrlOpt = cpMasterRepo.findCpAdminApiUrlByEngagementAndWorkspace(engagementId, workspace);
            cpAdminApiUrlOpt.ifPresentOrElse(
                url -> libDetails.put("cp_admin_api_url", url),
                () -> logMessage.append("CP Admin API URL not found | ")
            );

            // Remove trailing " | " from logs and errors
            if (logMessage.length() > 0) {
                libDetails.put("logs", logMessage.substring(0, logMessage.length() - 3));
            }

            if (errorMessage.length() > 0) {
                libDetails.put("errors", errorMessage.substring(0, errorMessage.length() - 3));
                libDetails.put("success", "false"); // Mark as failed when errors exist
            }

            return Collections.singletonList(libDetails);

        } catch (Exception e) {
            libDetails.put("success", "false");
            libDetails.put("errors", "An unexpected error occurred");
            libDetails.put("logs", "Error: " + e.getMessage());
            return Collections.singletonList(libDetails);
        }
    }
                    }
