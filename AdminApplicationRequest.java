@Service
@Transactional
public class HapCERServiceImpl implements HapCERService {
    
    private final CpMasterRepository cpMasterRepo;
    private final EngagementTargetRepository engagementRepo;
    private final WorkspaceTargetRepository workspaceRepo;
    
    @Autowired
    public HapCERServiceImpl(CpMasterRepository cpMasterRepo,
                           EngagementTargetRepository engagementRepo,
                           WorkspaceTargetRepository workspaceRepo) {
        this.cpMasterRepo = cpMasterRepo;
        this.engagementRepo = engagementRepo;
        this.workspaceRepo = workspaceRepo;
    }
    
    @Override
    public Map<String, Object> getCerEngagementData(String engagementId, String workspaceId) {
        Map<String, Object> response = new HashMap<>();
        List<String> logs = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        try {
            // Validate engagement exists
            Optional<EngagementTarget> engagementOpt = engagementRepo.findById(engagementId);
            if (engagementOpt.isEmpty()) {
                errors.add("Engagement not found");
                response.put("success", false);
                response.put("errors", errors);
                return response;
            }
            
            // Validate workspace exists
            Optional<WorkspaceTarget> workspaceOpt = workspaceRepo.findByWorkspaceId(workspaceId);
            if (workspaceOpt.isEmpty()) {
                errors.add("Workspace not found");
                } else {
                WorkspaceTarget workspace = workspaceOpt.get();
                
                // Fetch CP Admin API URL
                Optional<String> apiUrlOpt = cpMasterRepo.findCpAdminApiUrl(
                    engagementOpt.get().getRegion(),
                    workspace.getEnvironment()
                );
                
                apiUrlOpt.ifPresentOrElse(
                    url -> {
                        response.put("cp_admin_api_url", url);
                        logs.add("CP Admin API URL fetched successfully");
                    },
                    () -> errors.add("CP Admin API URL not found for given region/environment")
                );
            }
            
            // Build response
            response.put("success", errors.isEmpty());
            if (!logs.isEmpty()) response.put("logs", logs);
            if (!errors.isEmpty()) response.put("errors", errors);
            
            return response;
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("errors", List.of("Service error: " + e.getMessage()));
            return response;
        }
    }
                                 }
