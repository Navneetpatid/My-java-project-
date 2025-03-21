// Fetch DP Host URL from workspace_target table
String dpHostQuery = """
    SELECT dp_host_url 
    FROM workspace_target 
    WHERE engagement_id = ? 
    AND workspace = ?
    LIMIT 1
""";

try {
    String dpHostUrl = jdbcTemplate.queryForObject(dpHostQuery, new Object[]{engagementId, workspace}, String.class);
    libDetails.put("dp_host_url", dpHostUrl);
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DP Host URL fetched: " + dpHostUrl);
} catch (EmptyResultDataAccessException e) {
    libDetails.put("dp_host_url", null);
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DP Host URL not found");
}
