public KongApiResponse getCERData(String engagementId, String region, String platform, String environment) {
    KongApiResponse responseDTO = new KongApiResponse();

    // Fetch cp_master data by region
    List<CpMaster> cpMastersByRegion = cpMasterDetailsDao.findById_Region(region);

    // Fetch cp_master data by platform and environment
    List<CpMaster> cpMastersByPlatformAndEnv = cpMasterDetailsDao.findById_PlatformAndId_Environment(platform, environment);

    // Combine results safely
    List<CpMaster> combinedCpMasters = new ArrayList<>();
    
    if (cpMastersByRegion != null) {
        combinedCpMasters.addAll(cpMastersByRegion);
    }
GET http://localhost:8080/api/kong/getData?engagementId=999&region=EU&platform=Windows&environment=Staging
    if (cpMastersByPlatformAndEnv != null) {
        combinedCpMasters.addAll(cpMastersByPlatformAndEnv);
    }

    if (!combinedCpMasters.isEmpty()) {
        responseDTO.setCpMaster(combinedCpMasters);
    } else {
        responseDTO.setErrors("No data found in cp_master for given parameters");
    }

    // Fetch engagement plugin data
    List<EngagementPluginDetail> engagementPlugins = engagementPluginDetailsDao.findByEngagementId(engagementId);
    responseDTO.setEngagementPlugins(engagementPlugins != null ? engagementPlugins : new ArrayList<>());

    return responseDTO;
}
