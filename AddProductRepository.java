// Fetch DP Host URL
String dpHostQuery = """
    SELECT dp_host_url
    FROM dp_master
    WHERE region = (SELECT region FROM engagement_target WHERE engagement_id = ?)
    AND environment = (SELECT environment FROM workspace_target WHERE workspace = ?)
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

// Fetch GBGF
String gbgfQuery = """
    SELECT gbgf
    FROM gbgf_master
    WHERE region = (SELECT region FROM engagement_target WHERE engagement_id = ?)
    AND environment = (SELECT environment FROM workspace_target WHERE workspace = ?)
    LIMIT 1
""";

try {
    String gbgf = jdbcTemplate.queryForObject(gbgfQuery, new Object[]{engagementId, workspace}, String.class);
    libDetails.put("gbgf", gbgf);
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | GBGF fetched: " + gbgf);
} catch (EmptyResultDataAccessException e) {
    libDetails.put("gbgf", null);
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | GBGF not found");
}

// Fetch DMZ Load Balancer
String dmzLbQuery = """
    SELECT dmzlb
    FROM dmz_lb_master
    WHERE region = (SELECT region FROM engagement_target WHERE engagement_id = ?)
    AND environment = (SELECT environment FROM workspace_target WHERE workspace = ?)
    LIMIT 1
""";

try {
    String dmzLb = jdbcTemplate.queryForObject(dmzLbQuery, new Object[]{engagementId, workspace}, String.class);
    libDetails.put("dmzlb", dmzLb);
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DMZ Load Balancer fetched: " + dmzLb);
} catch (EmptyResultDataAccessException e) {
    libDetails.put("dmzlb", null);
    libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | DMZ Load Balancer not found");
}
