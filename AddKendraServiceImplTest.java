@Override
public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
    Map<String, Object> libDetails = new HashMap<>();
    StringBuilder logMessage = new StringBuilder();

    try {
        // Validate Engagement ID
        String query = "SELECT engagement_id, gbgf FROM engagement_target WHERE engagement_id = ?";
        List<Map<String, Object>> engagementDataList = jdbcTemplate.queryForList(query, engagementId);

        if (engagementDataList.isEmpty()) {
            libDetails.put("errors", "Engagement ID not validated");
            libDetails.put("logs", "Engagement ID not found");
            return Collections.singletonList(libDetails);
        }

        Map<String, Object> engagementData = engagementDataList.get(0);
        libDetails.put("gbgf", engagementData.get("gbgf"));

        // Validate Workspace
        query = "SELECT workspace, dp_host_url FROM workspace_target WHERE engagement_id = ? AND workspace = ?";
        List<Map<String, Object>> workspaceDataList = jdbcTemplate.queryForList(query, engagementId, workspace);

        if (workspaceDataList.isEmpty()) {
            libDetails.put("errors", "Workspace not validated");
            libDetails.put("logs", "Workspace not found");
            return Collections.singletonList(libDetails);
        }

        Map<String, Object> workspaceData = workspaceDataList.get(0);
        libDetails.put("workspace", workspace);
        libDetails.put("dpHost", workspaceData.get("dp_host_url"));

        // Fetch Mandatory Plugins
        query = "SELECT mandatory_plugin FROM engagement_plugin WHERE engagement_id = ?";
        List<String> mandatoryPlugins = jdbcTemplate.queryForList(query, String.class, engagementId);
        libDetails.put("mandatoryPlugins", mandatoryPlugins);

        // Fetch DP Host URL
        String dpHostQuery = "SELECT dp_host_url FROM workspace_target WHERE engagement_id = ? AND workspace = ? LIMIT 1";
        try {
            String dpHostUrl = jdbcTemplate.queryForObject(dpHostQuery, new Object[]{engagementId, workspace}, String.class);
            libDetails.put("dp_host_url", dpHostUrl);
        } catch (EmptyResultDataAccessException e) {
            libDetails.put("dp_host_url", null);
            logMessage.append("DP Host URL not found | ");
        }

        // Fetch CP Admin API URL
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
            libDetails.put("cp_admin_api_url", cpAdminApiUrl);
            if (cpAdminApiUrl == null) {
                logMessage.append("CP Admin API URL not found | ");
            }
        } catch (EmptyResultDataAccessException e) {
            libDetails.put("cp_admin_api_url", null);
            logMessage.append("CP Admin API URL not found | ");
        }

        libDetails.put("success", "true");

        // Include logs only if there are missing values
        if (logMessage.length() > 0) {
            libDetails.put("logs", logMessage.substring(0, logMessage.length() - 3)); // Remove last " | "
        }

        return Collections.singletonList(libDetails);

    } catch (Exception e) {
        libDetails.put("errors", "An unexpected error occurred");
        libDetails.put("logs", "Error: " + e.getMessage());
        return Collections.singletonList(libDetails);
    }
            }
