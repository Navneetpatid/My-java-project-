public class MapCERServiceImpl implements MapCERService {

    private final EngagementTargetDao engagementTargetDao;
    private final EngagementPluginDao engagementPluginDao;
    private final WorkspaceTargetDao workspaceTargetDao;
    private final CpMasterDao cpMasterDao;

    public MapCERServiceImpl(EngagementTargetDao engagementTargetDao,
                           EngagementPluginDao engagementPluginDao,
                           WorkspaceTargetDao workspaceTargetDao,
                           CpMasterDao cpMasterDao) {
        this.engagementTargetDao = engagementTargetDao;
        this.engagementPluginDao = engagementPluginDao;
        this.workspaceTargetDao = workspaceTargetDao;
        this.cpMasterDao = cpMasterDao;
    }

    @Override
    public ShpGetResponse getShpEngagementData(String engagementId, String namespace, String cluster) {
        ShpGetResponse response = new ShpGetResponse();
        StringBuilder errors = new StringBuilder();
        StringBuilder logs = new StringBuilder();

        try {
            // 1. Fetch engagement target data
            EngagementTarget engagementTarget = engagementTargetDao.findById(engagementId)
                .orElseThrow(() -> new NotFoundException("Engagement not found"));
            
            response.setRegion(engagementTarget.getRegion());
            response.setNetworkRegion(engagementTarget.getNetworkRegion());
            response.setPlatform(engagementTarget.getDpPlatform());
            response.setGbgsf(engagementTarget.getDbgf());
            logs.append("Engagement target loaded for ID: ").append(engagementId);

            // 2. Fetch mandatory plugins
            List<EngagementPlugin> plugins = engagementPluginDao.findByEngagementId(engagementId);
            response.setMandatoryPlugins(plugins.stream()
                .map(EngagementPlugin::getMandatoryPlugin)
                .collect(Collectors.toList()));
            logs.append(" | Found ").append(plugins.size()).append(" plugins");

            // 3. Fetch workspace targets
            List<WorkspaceTarget> workspaces = workspaceTargetDao.findByEngagementId(engagementId);
            if (!workspaces.isEmpty()) {
                response.setWorkspaces(workspaces.stream()
                    .map(WorkspaceTarget::getWorkspace)
                    .collect(Collectors.toList()));
                response.setDpHostUrl(workspaces.get(0).getDpHostUrl());
                logs.append(" | Found ").append(workspaces.size()).append(" workspaces");
            }

            // 4. Fetch CP Master configuration
            cpMasterDao.findByRegionAndPlatformAndEnvironment(
                engagementTarget.getRegion(),
                engagementTarget.getDpPlatform(),
                "PRD" // Assuming production environment
            ).ifPresent(cpMaster -> {
                response.setCpAdminApiUrl(cpMaster.getCpAdminApiUrl());
            });

            response.setSuccess(true);
            response.setLogs(logs.toString());
            return response;

        } catch (NotFoundException e) {
            return buildErrorResponse("Engagement not found", e.getMessage());
        } catch (Exception e) {
            appendErrorAndLog(errors, logs, "SystemError", e.getMessage());
            response.setSuccess(false);
            response.setErrors(errors.toString());
            response.setLogs(logs.toString());
            return response;
        }
    }

    private ShpGetResponse buildErrorResponse(String error, String log) {
        ShpGetResponse response = new ShpGetResponse();
        response.setSuccess(false);
        response.setErrors(error);
        response.setLogs(log);
        return response;
    }

    private void appendErrorAndLog(StringBuilder errors, StringBuilder logs, 
                                 String prefix, String message) {
        String error = prefix + ": " + message;
        errors.append(error).append("\n");
        logs.append("ERROR - ").append(error).append("\n");
    }
                                              }
