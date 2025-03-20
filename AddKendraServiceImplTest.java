public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
    Map<String, Object> libDetails = new HashMap<>();

    // Fetch DP Host
    String dpHostQuery = """
        SELECT dm.dp_host 
        FROM dp_master dm
        JOIN engagement_target et ON et.engagement_id = ?
        JOIN workspace_target wt ON wt.workspace = ?
        WHERE dm.region = et.region 
        AND dm.environment = wt.environment 
        LIMIT 1
    """;

    try {
        String dpHost = jdbcTemplate.queryForObject(dpHostQuery, new Object[]{engagementId, workspace}, String.class);
        libDetails.put("dpHost", dpHost);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DP Host fetched: " + dpHost);
        LOGGER.info("DP Host fetched for Engagement ID {}: {}", engagementId, dpHost);
    } catch (EmptyResultDataAccessException e) {
        libDetails.put("dpHost", null);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DP Host not found");
        LOGGER.warn("DP Host not found for Engagement ID {}", engagementId);
    }

    // Fetch DMZ Load Balancer
    String dmzLbQuery = """
        SELECT dlb.load_balancer 
        FROM dmz_lb_master dlb
        JOIN workspace_target wt ON wt.environment = dlb.environment
        JOIN engagement_target et ON et.region = dlb.region
        WHERE et.engagement_id = ?
        AND wt.workspace = ?
        LIMIT 1
    """;

    try {
        String dmzLb = jdbcTemplate.queryForObject(dmzLbQuery, new Object[]{engagementId, workspace}, String.class);
        libDetails.put("dmz_lb", dmzLb);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DMZ LB fetched: " + dmzLb);
        LOGGER.info("DMZ Load Balancer fetched for Engagement ID {}: {}", engagementId, dmzLb);
    } catch (EmptyResultDataAccessException e) {
        libDetails.put("dmz_lb", null);
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DMZ LB not found");
        LOGGER.warn("DMZ LB not found for Engagement ID {}", engagementId);
    }

    libDetails.put("success", "true");
    return Collections.singletonList(libDetails);
}
