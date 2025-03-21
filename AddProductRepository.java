public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
    Map<String, Object> libDetails = new HashMap<>();
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | Fetched mandatory plugins");

    // Fetch CP Admin API URL
    String cpAdminApiQuery = """
        SELECT cp_admin_api_url 
        FROM cp_master 
        WHERE (region, environment) IN (
            SELECT region, environment 
            FROM engagement_target a 
            JOIN workspace_target b 
            ON a.engagement_id = b.engagement_id 
            WHERE a.engagement_id = ? AND b.workspace = ?
        )
        LIMIT 1
    """;

    try {
        String cpAdminApiUrl = jdbcTemplate.queryForObject(cpAdminApiQuery, new Object[]{engagementId, workspace}, String.class);
        libDetails.put("cpAdminApiUrl", cpAdminApiUrl);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | Received CP_ADMIN_API_URL: " + cpAdminApiUrl);
        LOGGER.info("CP Admin API URL fetched for Engagement ID {}: {}", engagementId, cpAdminApiUrl);
    } catch (EmptyResultDataAccessException e) {
        libDetails.put("cpAdminApiUrl", null);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | CP Admin API URL not found");
        LOGGER.warn("CP Admin API URL not found for Engagement ID {}", engagementId);
    }

    libDetails.put("success", "true");
    return Collections.singletonList(libDetails);
}
