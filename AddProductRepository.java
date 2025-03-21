// Fetch CP Admin API URL dynamically
String cpAdminApiQuery = """
    SELECT cm.cp_admin_api_url 
    FROM cp_master cm
    WHERE EXISTS (
        SELECT 1 
        FROM engagement_target et
        JOIN workspace_target wt ON et.engagement_id = wt.engagement_id
        WHERE et.engagement_id = ? 
        AND wt.workspace = ?
        AND et.region = cm.region 
        AND wt.environment = cm.environment
    )
    LIMIT 1
""";

try {
    String cpAdminApiUrl = jdbcTemplate.queryForObject(cpAdminApiQuery, new Object[]{engagementId, workspace}, String.class);

    if (cpAdminApiUrl != null) {
        libDetails.put("cp_admin_api_url", cpAdminApiUrl);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | Received CP_ADMIN_API_URL: " + cpAdminApiUrl);
        LOGGER.info("CP Admin API URL fetched for Engagement ID {}: {}", engagementId, cpAdminApiUrl);
    } else {
        libDetails.put("cp_admin_api_url", null);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | CP Admin API URL not found");
        LOGGER.warn("CP Admin API URL not found for Engagement ID {}", engagementId);
    }
} catch (EmptyResultDataAccessException e) {
    libDetails.put("cp_admin_api_url", null);
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | CP Admin API URL not found");
    LOGGER.warn("CP Admin API URL not found for Engagement ID {}", engagementId);
}
