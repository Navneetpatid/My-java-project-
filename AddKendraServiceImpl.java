package com.example.services;

import com.example.entities.*;
import com.example.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ValidationService {

    @Autowired
    private EngagementTargetRepository engagementTargetRepository;

    @Autowired
    private WorkspaceTargetDetailsRepository workspaceTargetDetailsRepository;

    @Autowired
    private EngagementPluginDetailsRepository engagementPluginDetailsRepository;

    @Autowired
    private CpMasterRepository cpMasterRepository;

    @Autowired
    private DmzLbMasterRepository dmzLbMasterRepository;

    public Map<String, Object> validateWorkspaceForEngagement(String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        logs.append("HAP Database Validation Started");

        // Validate Engagement
        EngagementTarget engagement = engagementTargetRepository.findById(engagementId).orElse(null);
        if (engagement == null) {
            errors.append("Engagement ID not validated | ");
            logs.append("Engagement ID not found | ");
            response.put("success", false);
        } else {
            response.put("gbgf", engagement.getGbgf());
            logs.append("Engagement ID validated | ");
        }

        // Validate Workspace
        WorkspaceTargetDetails workspaceTarget = workspaceTargetDetailsRepository
                .findById_EngagementIdAndId_Workspace(engagementId, workspace);
        if (workspaceTarget == null) {
            errors.append("Workspace not validated | ");
            logs.append("Workspace not found | ");
            response.put("success", false);
        } else {
            response.put("workspace", workspaceTarget.getId().getWorkspace());
            response.put("dpHost", workspaceTarget.getDpHost());
            logs.append("Workspace validated | ");
        }

        // Fetch Mandatory Plugins
        List<EngagementPluginDetails> plugins = engagementPluginDetailsRepository.findById_EngagementId(engagementId);
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
            response.put("mandatoryPlugins", new ArrayList<>());
        }

        // Fetch CP Admin API URL
        if (workspaceTarget != null) {
            CpMaster cpMaster = cpMasterRepository.findById_RegionAndId_Environment(
                    workspaceTarget.getEnvironment(), workspaceTarget.getEnvironment());
            if (cpMaster != null) {
                response.put("cp_url", cpMaster.getCpAdminApiUrl());
                logs.append("CP Admin API URL fetched | ");
            } else {
                errors.append("CP Admin API URL not found | ");
                logs.append("CP Admin API URL not found | ");
                response.put("cp_url", "");
            }
        } else {
            errors.append("Workspace not validated, cannot fetch CP Admin API URL | ");
            logs.append("Workspace not validated, cannot fetch CP Admin API URL | ");
            response.put("cp_url", "");
        }

        // Fetch DMZ Load Balancer
        if (workspaceTarget != null) {
            DmzLbMaster dmzLbMaster = dmzLbMasterRepository.findByEnvironmentAndRegion(
                    workspaceTarget.getEnvironment(), workspaceTarget.getRegion());
            if (dmzLbMaster != null) {
                response.put("dmz_lb", dmzLbMaster.getLoadBalancer());
                logs.append("DMZ Load Balancer fetched | ");
            } else {
                errors.append("DMZ Load Balancer not found | ");
                logs.append("DMZ Load Balancer not found | ");
                response.put("dmz_lb", "");
            }
        } else {
            errors.append("Workspace not validated, cannot fetch DMZ Load Balancer | ");
            logs.append("Workspace not validated, cannot fetch DMZ Load Balancer | ");
            response.put("dmz_lb", "");
        }

        // Finalize response
        response.put("logs", logs.toString());
        response.put("errors", errors.toString());
        response.put("success", errors.length() == 0);

        return response;
    }
}
