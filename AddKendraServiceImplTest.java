package com.hsbc.hap.cdr.service.impl;

import com.hsbc.hap.cdr.service.HapCDRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HapCDRServiceImpl implements HapCDRService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HapCDRServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> validateWorkspaceForEngagement(String engagementId, String workspace) {
        Map<String, Object> libDetails = new HashMap<>();
        libDetails.put("logs", "HAP Database Validation Started");

        try {
            // Validate Engagement ID
            String query = "SELECT engagement_id, gbgf FROM engagement_target WHERE engagement_id = ?";
            List<Map<String, Object>> engagementDataList = jdbcTemplate.queryForList(query, engagementId);

            if (engagementDataList.isEmpty()) {
                libDetails.put("errors", "Engagement ID not validated");
                libDetails.put("logs", libDetails.get("logs") + " | Engagement ID not found");
                return libDetails;
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
                return libDetails;
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

            // Fetch CP Admin API URL
            query = """
                    SELECT cp_admin_api_url 
                    FROM cp_master 
                    WHERE (region, environment) IN 
                        (SELECT a.region, b.environment 
                         FROM engagement_target a 
                         JOIN workspace_target b 
                         ON a.engagement_id = b.engagement_id 
                         WHERE a.engagement_id = ? AND b.workspace = ?)
                    """;
            try {
                String cpUrl = jdbcTemplate.queryForObject(query, new Object[]{engagementId, workspace}, String.class);
                libDetails.put("cp_url", cpUrl);
                libDetails.put("logs", libDetails.get("logs") + " | Received CP_ADMIN URL: " + cpUrl);
            } catch (EmptyResultDataAccessException e) {
                libDetails.put("cp_url", null);
                libDetails.put("logs", libDetails.get("logs") + " | CP Admin URL not found");
            }

            // Fetch DMZ Load Balancer
            query = """
                    SELECT load_balancer 
                    FROM dmz_lb_master 
                    WHERE environment = (SELECT environment FROM workspace_target WHERE engagement_id = ? LIMIT 1)
                    AND region = (SELECT region FROM engagement_target WHERE engagement_id = ? LIMIT 1)
                    """;
            try {
                String dmzLb = jdbcTemplate.queryForObject(query, new Object[]{engagementId, engagementId}, String.class);
                libDetails.put("dmz_lb", dmzLb);
                libDetails.put("logs", libDetails.get("logs") + " | Received DMZ Load Balancer: " + dmzLb);
            } catch (EmptyResultDataAccessException e) {
                libDetails.put("dmz_lb", null);
                libDetails.put("logs", libDetails.get("logs") + " | DMZ Load Balancer not found");
            }

            // Fetch DP Host
            query = """
                    SELECT dp_host 
                    FROM dp_master 
                    WHERE region = (SELECT region FROM engagement_target WHERE engagement_id = ? LIMIT 1)
                    AND environment = (SELECT environment FROM workspace_target WHERE workspace = ? LIMIT 1)
                    """;
            try {
                List<Map<String, Object>> dpHostData = jdbcTemplate.queryForList(query, engagementId, workspace);

                if (!dpHostData.isEmpty()) {
                    libDetails.put("dpHost", dpHostData.get(0).get("dp_host"));
                    LOGGER.info("DP Host fetched for Engagement ID {}: {}", engagementId, dpHostData.get(0).get("dp_host"));
                } else {
                    libDetails.put("logs", libDetails.get("logs") + " | DP Host not found");
                    LOGGER.warn("DP Host not found for Engagement ID {}", engagementId);
                }
            } catch (EmptyResultDataAccessException e) {
                libDetails.put("dpHost", null);
                libDetails.put("logs", libDetails.get("logs") + " | DP Host not found");
                LOGGER.warn("DP Host not found for Engagement ID {}", engagementId);
            }

            libDetails.put("success", "true");
            return libDetails;

        } catch (Exception e) {
            libDetails.put("errors", "Database error: " + e.getMessage());
            libDetails.put("logs", libDetails.get("logs") + " | Error: " + e.getMessage());
            LOGGER.error("Database error occurred: {}", e.getMessage(), e);
            return libDetails;
        }
    }
        }
