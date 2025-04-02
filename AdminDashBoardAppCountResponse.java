if (workspaceTarget != null) {
    // Fetch region from related table (assuming EngagementTarget contains region info)
    Optional<EngagementTarget> engagementTargetOpt = engagementTargetDao.findById(workspaceTarget.getId());
    
    if (engagementTargetOpt.isPresent()) {
        String region = engagementTargetOpt.get().getRegion();
        String environment = workspaceTarget.getEnvironment();
        
        // Fetch CP Admin API URL using environment & region
        Optional<CpMaster> cpMaster = cpMasterDetailsDao.findById_RegionAndId_Environment(region, environment);

        if (cpMaster.isPresent()) {
            CpMaster master = cpMaster.get();
            if (master.getCp_admin_api_url() != null && !master.getCp_admin_api_url().isEmpty()) {
                response.put("cp_admin_api_url", master.getCp_admin_api_url());
                logs.append("CP Admin API URL fetched successfully. ");
            } else {
                errors.append("CP Admin API URL is empty or null. ");
                logs.append("CP Admin API URL is empty or null. ");
            }
        } else {
            errors.append("CP Admin API URL not found for region: " + region + " and environment: " + environment);
            logs.append("CP Admin API URL not found for given region and environment. ");
        }
    } else {
        errors.append("Region not found for workspaceTarget.");
        logs.append("Region not found for workspaceTarget.");
    }
} else {
    errors.append("Workspace not validated, cannot fetch CP Admin API URL. ");
    logs.append("Workspace not validated, cannot fetch CP Admin API URL. ");
}
