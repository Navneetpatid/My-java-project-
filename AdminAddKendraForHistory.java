public class MapCERServiceImpl implements MapCERService {
    public List<Map<String, Object>> getCerEngagementData(String engagementId, String wo) {
        // Fetch CP Admin API URL
        if (workspaceTarget != null) {
            CpMaster cpMaster = cpMasterDetailsDao.findById_RegionAndId_Environment(
                workspaceTarget.getRegion(),  // Use getRegion() instead of getEnvironment() twice
                workspaceTarget.getEnvironment());
            
            if (cpMaster != null) {
                response.put("Cp_admin_api_url", cpMaster.getCp_admin_api_url());
                logs.append("CP Admin API URL fetched | ");
            } else {
                errors.append("CP Admin API URL not found | ");
                logs.append("CP Admin API URL not found | ");
            }
        } else {
            errors.append("Workspace not validated, cannot fetch CP Admin API URL | ");
            logs.append("Workspace not validated_cannot fetch CP Admin API URL | ");
        }
        
        // Fetch DMZ Load Balancer
        if (workspaceTarget != null) {
            DmzLbMaster dmzlbMaster = dmzlbMasterDao.findByEnvironmentAndRegion(
                workspaceTarget.getEnvironment(), 
                workspaceTarget.getPosition());
        }
    }
}
