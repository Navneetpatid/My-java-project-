public KongApiResponse getCERData(String engagementId, String region, String platform, String environment) {
    KongApiResponse responseDTO = new KongApiResponse();

    // Fetch cp_master data by region
    List<CpMaster> cpMastersByRegion = cpMasterDetailsDao.findById_Region(region);

    // Fetch cp_master data by platform and environment
    List<CpMaster> cpMastersByPlatformAndEnv = cpMasterDetailsDao.findById_PlatformAndId_Environment(platform, environment);

    // Combine results (Optional: Handle duplicates or conflicts)
    Set<CpMaster> uniqueCpMasters = new HashSet<>();
    uniqueCpMasters.addAll(cpMastersByRegion);
    uniqueCpMasters.addAll(cpMastersByPlatformAndEnv);

    if (!uniqueCpMasters.isEmpty()) {
        responseDTO.setCpMaster(new ArrayList<>(uniqueCpMasters));
    } else {
        responseDTO.setErrors("No data found in cp_master for given parameters");
    }

    // Fetch engagement plugin data
    List<EngagementPluginDetail> engagementPlugins = engagementPluginDetailsDao.findByEngagementId(engagementId);
    responseDTO.setEngagementPlugins(engagementPlugins);

    return responseDTO;
}
