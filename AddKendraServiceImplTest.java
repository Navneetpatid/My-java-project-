package com.hsbc.hap.cdr.service.impl;

import com.hsbc.hap.cdr.service.HapCDRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class HapCDRServiceImpl implements HapCDRService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HapCDRServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
        List<Map<String, Object>> responseList = new ArrayList<>();
        Map<String, Object> libDetails = new HashMap<>();
        
        libDetails.put("logs", "HAP Database Validation Started");
        LOGGER.info("HAP Database Validation Started for Engagement ID: {}", engagementId);

        try {
            // Validate Engagement ID
            String query = "SELECT engagement_id FROM engagement_target WHERE engagement_id = ?";
            List<Map<String, Object>> engagementData = jdbcTemplate.queryForList(query, engagementId);

            if (engagementData.isEmpty()) {
                libDetails.put("errors", "Engagement ID not validated");
                libDetails.put("logs", libDetails.get("logs") + " | Engagement ID not found");
                LOGGER.error("Engagement ID {} not found", engagementId);
                responseList.add(libDetails);
                return responseList;
            }

            LOGGER.info("Engagement ID {} validated", engagementId);

            // Fetch all workspaces for the engagement
            query = "SELECT workspace FROM workspace_target WHERE engagement_id = ?";
            List<Map<String, Object>> workspaceData = jdbcTemplate.queryForList(query, engagementId);

            List<String> workspaceList = new ArrayList<>();
            for (Map<String, Object> row : workspaceData) {
                workspaceList.add((String) row.get("workspace"));
            }
            libDetails.put("workspace", workspaceList);

            // Validate Specific Workspace
            query = "SELECT workspace FROM workspace_target WHERE engagement_id = ? AND workspace = ?";
            List<Map<String, Object>> specificWorkspace = jdbcTemplate.queryForList(query, engagementId, workspace);

            if (specificWorkspace.isEmpty()) {
                libDetails.put("errors", "Workspace not validated");
                libDetails.put("logs", libDetails.get("logs") + " | Workspace not found");
                LOGGER.error("Workspace {} not found for Engagement ID {}", workspace, engagementId);
                responseList.add(libDetails);
                return responseList;
            }

            LOGGER.info("Workspace {} validated for Engagement ID {}", workspace, engagementId);

            // Fetch DP Host
query = "SELECT dp_host FROM dp_master WHERE region = (SELECT region FROM engagement_target WHERE engagement_id = ?) AND environment = (SELECT environment FROM workspace_target WHERE workspace = ?)";
List<Map<String, Object>> dpHostData = jdbcTemplate.queryForList(query, engagementId, workspace);

if (!dpHostData.isEmpty()) {
    libDetails.put("dpHost", dpHostData.get(0).get("dp_host"));
    LOGGER.info("DP Host fetched for Engagement ID {}: {}", engagementId, dpHostData.get(0).get("dp_host"));
} else {
    libDetails.put("logs", libDetails.get("logs") + " | DP Host not found");
    LOGGER.warn("DP Host not found for Engagement ID {}", engagementId);
}
            // Fetch GBGF Data
            query = "SELECT gbgf FROM engagement_target WHERE engagement_id = ?";
            List<Map<String, Object>> gbgfData = jdbcTemplate.queryForList(query, engagementId);

            if (!gbgfData.isEmpty()) {
                libDetails.put("gbgf", gbgfData.get(0).get("gbgf"));
                LOGGER.info("GBGF data fetched for Engagement ID {}: {}", engagementId, gbgfData.get(0).get("gbgf"));
            } else {
                libDetails.put("logs", libDetails.get("logs") + " | GBGF data not found");
                LOGGER.warn("GBGF data not found for Engagement ID {}", engagementId);
            }

            // Fetch DMZ Load Balancer Data
            query = "SELECT load_balancer FROM dmz_lb_master WHERE environment = (SELECT environment FROM workspace_target WHERE workspace = ?) AND region = (SELECT region FROM engagement_target WHERE engagement_id = ?)";
            List<Map<String, Object>> dmzLbData = jdbcTemplate.queryForList(query, workspace, engagementId);

            if (!dmzLbData.isEmpty()) {
                libDetails.put("dmz_lb", dmzLbData.get(0).get("load_balancer"));
                LOGGER.info("DMZ Load Balancer data fetched for Engagement ID {}: {}", engagementId, dmzLbData.get(0).get("load_balancer"));
            } else {
                libDetails.put("logs", libDetails.get("logs") + " | DMZ Load Balancer data not found");
                LOGGER.warn("DMZ Load Balancer data not found for Engagement ID {}", engagementId);
            }

            // Fetch Mandatory Plugins
            query = "SELECT mandatory_plugin FROM engagement_plugin WHERE engagement_id = ?";
            List<Map<String, Object>> pluginData = jdbcTemplate.queryForList(query, engagementId);

            List<String> pluginList = new ArrayList<>();
            for (Map<String, Object> row : pluginData) {
                pluginList.add((String) row.get("mandatory_plugin"));
            }
            libDetails.put("mandatoryPlugins", pluginList);
            LOGGER.info("Mandatory plugins fetched: {}", pluginList);

            // Fetch CP Admin API URL
            query = "SELECT cp_admin_api_url FROM cp_master WHERE (region, environment) IN " +
                    "(SELECT a.region, b.environment FROM engagement_target a " +
                    "JOIN workspace_target b ON a.engagement_id = b.engagement_id " +
                    "WHERE a.engagement_id = ? AND b.workspace = ?)";
            List<Map<String, Object>> cpUrlData = jdbcTemplate.queryForList(query, engagementId, workspace);

            if (!cpUrlData.isEmpty()) {
                libDetails.put("cp_url", cpUrlData.get(0).get("cp_admin_api_url"));
                LOGGER.info("CP Admin API URL fetched: {}", cpUrlData.get(0).get("cp_admin_api_url"));
            } else {
                libDetails.put("logs", libDetails.get("logs") + " | CP Admin API URL not found");
                LOGGER.warn("CP Admin API URL not found for Engagement ID {}", engagementId);
            }

            libDetails.put("success", "true");
            libDetails.put("logs", libDetails.get("logs") + " | Validation successful");
            responseList.add(libDetails);

        } catch (Exception e) {
            LOGGER.error("Error occurred during validation: ", e);
            libDetails.put("errors", "Database error occurred");
            libDetails.put("logs", libDetails.get("logs") + " | Database error: " + e.getMessage());
            responseList.add(libDetails);
        }

        return responseList;
    }
                }
