if (workspaceTarget != null) {
    String environment = workspaceTarget.getEnvironment();
    
    // Fetch EngagementTarget to get region
    Optional<EngagementTargetKong> engagementTargetOpt = engagementTargetDao.findByEngagementId(workspaceTarget.getId().getEngagementId());

    if (engagementTargetOpt.isPresent()) {
        String region = engagementTargetOpt.get().getRegion();
        
        if (region != null && !region.isEmpty()) {
            // Fetch CP Admin API URL using environment & region
            Optional<CpMaster> cpMasterOpt = cpMasterDetailsDao.findById_RegionAndId_Environment(region, environment);

            if (cpMasterOpt.isPresent()) {
                CpMaster master = cpMasterOpt.get();
                
                if (master.getCp_admin_api_url() != null && !master.getCp_admin_api_url().isEmpty()) {
                    response.put("cp_admin_api_url", master.getCp_admin_api_url());
                    logs.append("CP Admin API URL fetched successfully. ");
                } else {
                    errors.append("CP Admin API URL is empty or null. ");
                    logs.append("CP Admin API URL is empty or null. ");
                }
            } else {
                errors.append("No CP Admin API URL found for region: " + region + " and environment: " + environment);
                logs.append("CP Admin API URL not found.");
            }
        } else {
            errors.append("Region is null or empty for engagementId: " + workspaceTarget.getId().getEngagementId());
            logs.append("Region not found for engagementId.");
        }
    } else {
        errors.append("Engagement ID not found in engagement_target table.");
        logs.append("Engagement ID not found.");
    }
} else {
    errors.append("Workspace not validated, cannot fetch CP Admin API URL.");
    logs.append("Workspace not validated.");
}
