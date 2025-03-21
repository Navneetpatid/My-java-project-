@Override
public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
    Map<String, Object> libDetails = new HashMap<>();
    StringBuilder logMessage = new StringBuilder();
    StringBuilder errorMessage = new StringBuilder();

    try {
        // Default values
        libDetails.put("success", "true");
        libDetails.put("gbgf", "");
        libDetails.put("workspace", "");
        libDetails.put("dpHost", "");
        libDetails.put("mandatoryPlugins", new ArrayList<>());
        libDetails.put("cp_admin_api_url", "");
        libDetails.put("dp_host_url", "");

        // Validate Engagement ID
        String query = "SELECT engagement_id, gbgf FROM engagement_target WHERE engagement_id = ?";
        List<Map<String, Object>> engagementDataList = jdbcTemplate.queryForList(query, engagementId);

        if (engagementDataList.isEmpty()) {
            errorMessage.append("Engagement ID not validated | ");
            logMessage.append("Engagement ID not found | ");
        } else {
            Map<String, Object> engagementData = engagementDataList.get(0);
            libDetails.put("gbgf", engagementData.getOrDefault("gbgf", ""));
        }

        // Validate Workspace
        query = "SELECT workspace, dp_host_url FROM workspace_target WHERE engagement_id = ? AND workspace = ?";
        List<Map<String, Object>> workspaceDataList = jdbcTemplate.queryForList(query, engagementId, workspace);

        if (workspaceDataList.isEmpty()) {
            errorMessage.append("Workspace not validated | ");
            logMessage.append("Workspace not found | ");
        } else {
            Map<String, Object> workspaceData = workspaceDataList.get(0);
            libDetails.put("workspace", workspace);
            libDetails.put("dpHost", workspaceData.getOrDefault("dp_host_url", ""));
        }

        // Fetch Mandatory Plugins
        query = "SELECT mandatory_plugin FROM engagement_plugin WHERE engagement_id = ?";
        List<String> mandatoryPlugins = jdbcTemplate.queryForList(query, String.class, engagementId);
        libDetails.put("mandatoryPlugins", mandatoryPlugins.isEmpty() ? new ArrayList<>() : mandatoryPlugins);

        // Fetch DP Host URL
        String dpHostQuery = "SELECT dp_host_url FROM workspace_target WHERE engagement_id = ? AND workspace = ? LIMIT 1";
        try {
            String dpHostUrl = jdbcTemplate.queryForObject(dpHostQuery, new Object[]{engagementId, workspace}, String.class);
            libDetails.put("dp_host_url", dpHostUrl != null ? dpHostUrl : "");
            if (dpHostUrl == null) logMessage.append("DP Host URL not found | ");
        } catch (EmptyResultDataAccessException e) {
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
            libDetails.put("cp_admin_api_url", cpAdminApiUrl != null ? cpAdminApiUrl : "");
            if (cpAdminApiUrl == null) logMessage.append("CP Admin API URL not found | ");
        } catch (EmptyResultDataAccessException e) {
            logMessage.append("CP Admin API URL not found | ");
        }

        // Remove trailing " | " from logs and errors
        if (logMessage.length() > 0) {
            libDetails.put("logs", logMessage.substring(0, logMessage.length() - 3));
        }

        if (errorMessage.length() > 0) {
            libDetails.put("errors", errorMessage.substring(0, errorMessage.length() - 3));
            libDetails.put("success", "false"); // Mark as failed when errors exist
        }

        return Collections.singletonList(libDetails);

    } catch (Exception e) {
        libDetails.put("success", "false");
        libDetails.put("errors", "An unexpected error occurred");
        libDetails.put("logs", "Error: " + e.getMessage());
        return Collections.singletonList(libDetails);
    }
                }
