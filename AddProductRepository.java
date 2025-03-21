@Override
public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
    Map<String, Object> libDetails = new HashMap<>();
    libDetails.put("logs", "HAP Database Validation Started");

    try {
        // Validate Engagement ID
        String query = "SELECT engagement_id, gbgf FROM engagement_target WHERE engagement_id = ?";
        List<Map<String, Object>> engagementDataList = jdbcTemplate.queryForList(query, engagementId);

        if (engagementDataList.isEmpty()) {
            libDetails.put("errors", "Engagement ID not validated");
            libDetails.put("logs", libDetails.get("logs") + " | Engagement ID not found");
            return Collections.singletonList(libDetails);
        }

        Map<String, Object> engagementData = engagementDataList.get(0);
        libDetails.put("gbgf", engagementData.get("gbgf"));
        libDetails.put("logs", libDetails.get("logs") + " | engagement_id " + engagementId + " validated");

        // Validate Workspace
        query = "SELECT workspace, dp_host_url FROM workspace_target WHERE engagement_id = ? AND workspace = ?";
        List<Map<String, Object>> workspaceDataList = jdbcTemplate.queryForList(query, engagementId, workspace);

        if (workspaceDataList.isEmpty()) {
            libDetails.put("errors", "Workspace not validated");
            libDetails.put("logs", libDetails.get("logs") + " | Workspace not found");
            return Collections.singletonList(libDetails);
        }

        Map<String, Object> workspaceData = workspaceDataList.get(0);
        libDetails.put("workspace", workspace);
        libDetails.put("dpHost", workspaceData.get("dp_host_url"));
        libDetails.put("logs", libDetails.get("logs") + " | workspace " + workspace + " validated");

        // Fetch Mandatory Plugins
        query = "SELECT mandatory_plugin FROM engagement_plugin WHERE engagement_id = ?";
        List<String> mandatoryPlugins = jdbcTemplate.queryForList(query, String.class, engagementId);
        libDetails.put("mandatoryPlugins", mandatoryPlugins);
        libDetails.put("logs", libDetails.get("logs") + " | Fetched mandatory plugins");

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

        libDetails.put("success", "true");
        return Collections.singletonList(libDetails);

    } catch (Exception e) {
        libDetails.put("errors", "An unexpected error occurred");
        libDetails.put("logs", libDetails.getOrDefault("logs", "") + " | Error: " + e.getMessage());
        return Collections.singletonList(libDetails);
    }
    }
