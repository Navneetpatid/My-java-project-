@Service
@Transactional
public class HapCERServiceImpl implements HapCERService {
    
    private final CpMasterDetailsDao cpMasterDao;
    private final EngagementTargetRepository engagementRepo;
    private final WorkspaceTargetRepository workspaceRepo;
    
    @Autowired
    public HapCERServiceImpl(CpMasterDetailsDao cpMasterDao,
                           EngagementTargetRepository engagementRepo,
                           WorkspaceTargetRepository workspaceRepo) {
        this.cpMasterDao = cpMasterDao;
        this.engagementRepo = engagementRepo;
        this.workspaceRepo = workspaceRepo;
    }
    
    @Override
    public Map<String, Object> getCertagagementData(String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        List<String> logs = new ArrayList<>();
        
        try {
            // Primary method - direct query
            Optional<String> apiUrlOpt = cpMasterDao.findCpAdminApiUrl(engagementId, workspace);
            
            if (apiUrlOpt.isPresent()) {
                response.put("cp_admin_api_url", apiUrlOpt.get());
                response.put("source", "direct_query");
            } else {
                // Fallback method
                Optional<EngagementTarget> engagement = engagementRepo.findById(engagementId);
                Optional<WorkspaceTarget> workspaceTarget = workspaceRepo.findById(
                    new WorkspaceTargetId(engagementId, workspace));
                
                if (engagement.isPresent() && workspaceTarget.isPresent()) {
                    Optional<CpMaster> cpMaster = cpMasterDao.findById(
                        new CpMasterId(
                            engagement.get().getRegion(),
                            "default", // or get platform from somewhere
                            workspaceTarget.get().getEnvironment()
                        ));
                    
                    cpMaster.ifPresent(m -> {
                        response.put("cp_admin_api_url", m.getCpAdminApiUrl());
                        response.put("last_updated", m.getUpdatedDate());
                        response.put("source", "fallback_query");
                    });
                }
                
                if (!response.containsKey("cp_admin_api_url")) {
                    logs.add("CP Admin API URL not found through any method");
                }
            }
            
            if (!logs.isEmpty()) {
                response.put("logs", logs);
            }
            
            return response;
            
        } catch (Exception e) {
            response.put("error", "Service error: " + e.getMessage());
            return response;
        }
    }
                    }
