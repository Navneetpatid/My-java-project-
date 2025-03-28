package com.hsbc.hap.cdr;

import com.hsbc.hap.cdr.dao.CpMasterDetailsDao;
import com.hsbc.hap.cdr.dao.EngagementTargetKongDao;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HapCDRServiceImpl implements HapCDRService {

    @Override
    public List<Map<String, Object>> validateWorkspaceForEngagement(String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        // Initialize all fields in the response
        response.put("success", false); // Default to false, will be updated if validation passes
        response.put("gbgf", null);
        response.put("workspace", null);
        response.put("dpHost", null);
        response.put("mandatoryPlugins", new ArrayList<>());
        response.put("cp_url", null);
        response.put("dmz_lb", null);
        response.put("logs", "");
        response.put("errors", "");

        logs.append("HAP Database Validation Started");

        // Validate Engagement
        EngagementTargetKong engagement = engagementTargetKongDao.findById(engagementId).orElse(null);
        if (engagement == null) {
            errors.append("Engagement ID not validated | ");
            logs.append("Engagement ID not found | ");
        } else {
            response.put("gbgf", engagement.getGbgf());
            logs.append("Engagement ID validated | ");
        }

        // Validate Workspace
        WorkspaceTargetDetails workspaceTarget = workspaceTargetDetailsDao
                .findById_EngagementIdAndId_Workspace(engagementId, workspace);
        if (workspaceTarget == null) {
            errors.append("Workspace not validated | ");
            logs.append("Workspace not found | ");
        } else {
            response.put("workspace", workspaceTarget.getId().getWorkspace());
            response.put("dpHost", workspaceTarget.getDpHost());
            logs.append("Workspace validated | ");
        }

        // Fetch Mandatory Plugins
        List<EngagementPluginDetails> plugins = engagementPluginDetailsDao.findById_EngagementId(engagementId);
        List<String> mandatoryPlugins = new ArrayList<>();
        if (plugins != null && !plugins.isEmpty()) {
            for (EngagementPluginDetails plugin : plugins) {
                mandatoryPlugins.add(plugin.getMandatoryPlugin());
            }
            response.put("mandatoryPlugins", mandatoryPlugins);
            logs.append("Mandatory plugins fetched | ");
        } else {
            errors.append("No mandatory plugins found | ");
            logs.append("No mandatory plugins found | ");
        }

        // Fetch CP Admin API URL
        if (workspaceTarget != null) {
            CpMaster cpMaster = cpMasterDetailsDao.findById_RegionAndId_Environment(
                    workspaceTarget.getEnvironment(), workspaceTarget.getEnvironment());
            if (cpMaster != null) {
                response.put("cp_url", cpMaster.getCpAdminApiUrl());
                logs.append("CP Admin API URL fetched | ");
            } else {
                errors.append("CP Admin API URL not found | ");
                logs.append("CP Admin API URL not found | ");
            }
        } else {
            errors.append("Workspace not validated, cannot fetch CP Admin API URL | ");
            logs.append("Workspace not validated, cannot fetch CP Admin API URL | ");
        }

        // Fetch DMZ Load Balancer
        if (workspaceTarget != null) {
            DmzLbMaster dmzLbMaster = dmzLbMasterDao.findByEnvironmentAndRegion(
                    workspaceTarget.getEnvironment(), workspaceTarget.getRegion());
            if (dmzLbMaster != null) {
                response.put("dmz_lb", dmzLbMaster.getLoadBalancer());
                logs.append("DMZ Load Balancer fetched | ");
            } else {
                errors.append("DMZ Load Balancer not found | ");
                logs.append("DMZ Load Balancer not found | ");
            }
        } else {
            errors.append("Workspace not validated, cannot fetch DMZ Load Balancer | ");
            logs.append("Workspace not validated, cannot fetch DMZ Load Balancer | ");
        }

        // Finalize response
        response.put("logs", logs.toString());
        response.put("errors", errors.toString());
        response.put("success", errors.length() == 0); // Set success to true if no errors

        // Wrap the response map in a List
        List<Map<String, Object>> responseList = new ArrayList<>();
        responseList.add(response);

        return responseList;
    }
                }
