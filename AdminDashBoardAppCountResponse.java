@Service
public class HapCERServiceImpl implements HapCERService {

    @Autowired
    private OpMasterDetailsDao opMasterDetailsDao;
    
    @Autowired
    private EngagementTargetKongDao engagementTargetKongDao;
    
    @Autowired
    private WorkspaceTargetDao workspaceTargetDao; // Assuming this exists

    @Override
    public Map<String, String> getCpAdminApiUrl(String engagementId, String workspace) {
        Map<String, String> response = new HashMap<>();
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        // Fetch WorkspaceTarget to get environment
        WorkspaceTarget workspaceTarget = workspaceTargetDao.findByWorkspace(workspace);
        if (workspaceTarget == null) {
            errors.append("Workspace target not found. ");
            response.put("error", errors.toString());
            return response;
        }

        String environment = workspaceTarget.getEnvironment();

        // Fetch EngagementTarget to get region
        Optional<EngagementTargetKong> engagementTargetOpt = engagementTargetKongDao.findByEngagementId(engagementId);
        if (!engagementTargetOpt.isPresent()) {
            errors.append("Engagement target not found. ");
            response.put("error", errors.toString());
            return response;
        }

        String region = engagementTargetOpt.get().getRegion();
        if (region == null || region.isEmpty()) {
            errors.append("Region is empty or null. ");
            response.put("error", errors.toString());
            return response;
        }

        // Fetch CP Admin API URL using engagementId & workspace
        Optional<String> cpAdminApiUrlOpt = opMasterDetailsDao.findCpAdminApiUrl(engagementId, workspace);
        if (!cpAdminApiUrlOpt.isPresent()) {
            errors.append("CP Admin API URL not found. ");
            logs.append("CP Admin API URL not found. ");
            response.put("error", errors.toString());
            response.put("log", logs.toString());
            return response;
        }

        String cpAdminApiUrl = cpAdminApiUrlOpt.get();
        if (cpAdminApiUrl != null && !cpAdminApiUrl.isEmpty()) {
            response.put("cp_admin_api_url", cpAdminApiUrl);
            logs.append("CP Admin API URL fetched successfully. ");
            response.put("log", logs.toString());
        } else {
            errors.append("CP Admin API URL is empty or null. ");
            logs.append("CP Admin API URL is empty or null. ");
            response.put("error", errors.toString());
            response.put("log", logs.toString());
        }

        return response;
    }
}
