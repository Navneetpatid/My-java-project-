package com.example.service;

import com.example.dto.ValidationResponseDTO;
import com.example.entity.EngagementTarget;
import com.example.entity.WorkspaceTarget;
import com.example.entity.CpMaster;
import com.example.entity.DmzLbMaster;
import com.example.repository.EngagementRepository;
import com.example.repository.WorkspaceRepository;
import com.example.repository.CpMasterRepository;
import com.example.repository.DmzRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final EngagementRepository engagementRepository;
    private final WorkspaceRepository workspaceRepository;
    private final CpMasterRepository cpMasterRepository;
    private final DmzRepository dmzRepository;

    public ValidationResponseDTO validateEngagement(String engagementId, String workspace) {
        ValidationResponseDTO response = new ValidationResponseDTO();
        StringBuilder logs = new StringBuilder("HAP Database Validation Started | ");

        try {
            // **1. Validate Engagement ID**
            Optional<EngagementTarget> engagementOpt = engagementRepository.findByEngagementId(engagementId);
            if (engagementOpt.isEmpty()) {
                logs.append("Engagement ID not found | ");
                response.setSuccess(false);
                response.setErrors("Engagement ID not found");
                response.setLogs(logs.toString());
                return response;
            }
            EngagementTarget engagement = engagementOpt.get();
            logs.append("Engagement ID validated | ");

            // **2. Validate Workspace**
            Optional<WorkspaceTarget> workspaceOpt = workspaceRepository.findByEngagementIdAndWorkspace(engagementId, workspace);
            if (workspaceOpt.isEmpty()) {
                logs.append("Workspace not found for given Engagement ID | ");
                response.setSuccess(false);
                response.setErrors("Workspace not found for given Engagement ID");
                response.setLogs(logs.toString());
                return response;
            }

            WorkspaceTarget workspaceEntity = workspaceOpt.get();
            response.setWorkspace(workspace);
            response.setDpHost(workspaceEntity.getDpHostUrl());
            logs.append("Workspace validated | ");

            // **3. Fetch CP Admin URL**
            Optional<CpMaster> cpOpt = cpMasterRepository.findByRegionAndEnvironment(engagement.getRegion(), workspaceEntity.getEnvironment());
            response.setCp_url(cpOpt.map(CpMaster::getCpAdminApiUrl).orElse(""));
            logs.append(cpOpt.isPresent() ? "CP Admin URL found | " : "CP Admin URL not found | ");

            // **4. Fetch DMZ Load Balancer**
            Optional<DmzLbMaster> dmzOpt = dmzRepository.findByRegionAndEnvironment(engagement.getRegion(), workspaceEntity.getEnvironment());
            response.setDmz_lb(dmzOpt.map(DmzLbMaster::getLoadBalancer).orElse(""));
            logs.append(dmzOpt.isPresent() ? "DMZ Load Balancer found | " : "DMZ Load Balancer not found | ");

            // **5. Static / Default values for missing fields**
            response.setMandatoryPlugins("DefaultPluginList"); // Replace with actual logic if needed
            response.setGbgf("GBGF_Value"); // Fetch dynamically if required

            response.setSuccess(true);
            response.setLogs(logs.toString());
            return response;

        } catch (Exception e) {
            logs.append("Database error: ").append(e.getMessage()).append(" | ");
            response.setSuccess(false);
            response.setErrors("Internal Server Error");
            response.setLogs(logs.toString());
            return response;
        }
    }
                }
