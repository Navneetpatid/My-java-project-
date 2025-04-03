@Service
public class HapCERServiceImpl implements HapCERService {

    private final EngagementTargetRepository engagementRepo;
    private final WorkspaceTargetRepository workspaceRepo;
    private final DmzLbMasterRepository dmzLbMasterRepo;
    
    @Autowired
    public HapCERServiceImpl(EngagementTargetRepository engagementRepo,
                           WorkspaceTargetRepository workspaceRepo,
                           DmzLbMasterRepository dmzLbMasterRepo) {
        this.engagementRepo = engagementRepo;
        this.workspaceRepo = workspaceRepo;
        this.dmzLbMasterRepo = dmzLbMasterRepo;
    }

    @Override
    public Map<String, Object> getCerEngagementData(String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        boolean success = false;

        try {
            // 1. Safely get engagement and workspace
            Optional<EngagementTarget> engagementOpt = engagementRepo.findById(engagementId);
            Optional<WorkspaceTarget> workspaceTargetOpt = workspaceRepo
                .findByEngagementIdAndWorkspace(engagementId, workspace);

            if (workspaceTargetOpt.isPresent() && engagementOpt.isPresent()) {
                WorkspaceTarget workspaceTarget = workspaceTargetOpt.get();
                EngagementTarget engagement = engagementOpt.get();
                
                // 2. Case-insensitive search
                Optional<DmzLbMaster> dmzLbMasterOpt = dmzLbMasterRepo
                    .findByEnvironmentIgnoreCaseAndRegionIgnoreCase(
                        workspaceTarget.getEnvironment(),
                        engagement.getRegion()
                    );

                if (dmzLbMasterOpt.isPresent()) {
                    DmzLbMaster dmzLbMaster = dmzLbMasterOpt.get();
                    response.put("dmz_lb", dmzLbMaster.getLoadBalancer());
                    logs.append("DMZ Load Balancer fetched | ");
                    success = true;
                } else {
                    errors.append("DMZ Load Balancer not found | ");
                    logs.append("DMZ Load Balancer not found | ");
                }
            } else {
                if (!engagementOpt.isPresent()) {
                    errors.append("Engagement not found | ");
                    logs.append("Engagement validation failed | ");
                }
                if (!workspaceTargetOpt.isPresent()) {
                    errors.append("Workspace not validated | ");
                    logs.append("Workspace validation failed | ");
                }
            }
        } catch (Exception e) {
            errors.append("System error: ").append(e.getMessage()).append(" | ");
            logs.append("Error occurred: ").append(e.getMessage()).append(" | ");
        }

        response.put("logs", logs.toString());
        response.put("errors", errors.toString());
        response.put("success", success);
        
        return response;
    }
            }
