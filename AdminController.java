package com.hsbc.hap.cer.service;

import com.hsbc.hap.cer.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MapCERServiceImpl implements MapCERService {

    // ... [previous DAO declarations and constructor remain the same]

    @Override
    public CerGetResponse getCerEngagementData(String engagementId, String workspace) {
        CerGetResponse response = new CerGetResponse();
        StringBuilder errors = new StringBuilder();
        StringBuilder logs = new StringBuilder();

        try {
            // Log initial parameters
            logs.append(String.format("Starting processing for engagementId: %s, workspace: %s; ", engagementId, workspace));

            // Validate Engagement
            Optional<EngagementTargetKong> engagementTargetOpt = engagementTargetKongDao.findByEngagementId(engagementId);
            if (!engagementTargetOpt.isPresent()) {
                String errorMsg = String.format("engagementId %s not found in engagement_target table", engagementId);
                return buildErrorResponse(errorMsg, errorMsg);
            }
            
            EngagementTargetKong engagementTarget = engagementTargetOpt.get();
            response.setBogT(engagementTarget.getBogT());
            logs.append(String.format("EngagementId %s validated with BogT: %s; ", 
                                   engagementId, engagementTarget.getBogT()));

            // Validate Workspace
            Optional<WorkspaceTarget> workspaceTargetOpt = workspaceTargetDetailsDao
                .findById_EngagementIdAndId_Workspace(engagementId, workspace);
                
            if (!workspaceTargetOpt.isPresent()) {
                String errorMsg = String.format("Workspace %s not found for engagement ID %s", workspace, engagementId);
                appendErrorAndLog(errors, logs, errorMsg, errorMsg);
            } else {
                WorkspaceTarget workspaceTarget = workspaceTargetOpt.get();
                response.setWorkspace(workspace);
                response.setOp_host_url(workspaceTarget.getOp_host_url());
                logs.append(String.format("Workspace %s validated with Op_host_url: %s; ", 
                                        workspace, workspaceTarget.getOp_host_url()));
            }

            // Fetch Mandatory Plugins
            List<String> mandatoryPlugins = engagementPluginDetailsDao.findMandatoryPluginsByEngagementId(engagementId);
            response.setMandatoryPlugins(mandatoryPlugins);
            logs.append(String.format("Retrieved %d mandatory plugins for engagement %s: %s; ", 
                                    mandatoryPlugins.size(), engagementId, mandatoryPlugins));

            // Get CP Admin API URL
            Optional<String> cpMasterOpt = cpMasterDetailsDao.findCpAdminApiUrl(engagementId, workspace);
            if (cpMasterOpt.isPresent()) {
                response.setOp_admin_api_url(cpMasterOpt.get());
                logs.append(String.format("Found CP Admin API URL: %s; ", cpMasterOpt.get()));
            } else {
                String errorMsg = String.format("No CP Admin API URL found in CpMaster table for engagement %s, workspace %s", 
                                              engagementId, workspace);
                appendErrorAndLog(errors, logs, errorMsg, errorMsg);
            }

            // Get DMZ Load Balancer if workspace exists
            if (workspaceTargetOpt.isPresent() && engagementTargetOpt.isPresent()) {
                String environment = workspaceTargetOpt.get().getEnvironment();
                String region = engagementTarget.get().getRegion();
                
                Optional<String> dmzLibOpt = dmzLibMasterDao.findLoadBalancerByEnvironmentAndRegion(environment, region);
                    
                if (dmzLibOpt.isPresent()) {
                    response.setDmzLib(dmzLibOpt.get());
                    logs.append(String.format("Found DMZ Load Balancer: %s for environment %s, region %s; ", 
                                           dmzLibOpt.get(), environment, region));
                } else {
                    String errorMsg = String.format("DMZ Load Balancer not found for environment %s, region %s", 
                                                 environment, region);
                    logs.append(errorMsg);
                    errors.append(errorMsg);
                }
            }

            // Set final response status
            response.setSuccess(errors.length() == 0);
            response.setLogs(logs.toString().trim());
            response.setErrors(errors.toString().trim());

            if (errors.length() > 0) {
                response.setStatus(errors.toString().contains("Workspace not found") ? 
                                "PARTIAL_SUCCESS" : "ERROR");
            } else {
                response.setStatus("SUCCESS");
            }

            // Add final status to logs
            logs.append(String.format("Processing completed with final status: %s; ", response.getStatus()));

        } catch (Exception e) {
            String errorMsg = String.format("Unexpected error: %s while processing engagement %s, workspace %s", 
                                          e.getMessage(), engagementId, workspace);
            logs.append(errorMsg);
            errors.append(errorMsg);
            response.setSuccess(false);
            response.setStatus("ERROR");
            response.setLogs(logs.toString().trim());
            response.setErrors(errors.toString().trim());
        }

        return response;
    }

    // ... [buildErrorResponse and appendErrorAndLog methods remain the same]
                                  }
