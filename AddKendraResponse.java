package com.example.service;

import com.example.dto.ValidationResponseDTO;
import com.example.entity.EngagementTarget;
import com.example.entity.WorkspaceTarget;
import com.example.repository.EngagementRepository;
import com.example.repository.WorkspaceRepository;
import com.example.repository.CpMasterRepository;
import com.example.repository.DmzRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            // **1. Check if Engagement ID exists**
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

            // **2. Fetch all workspaces for the Engagement ID**
            List<String> workspaces = workspaceRepository.findByEngagementId(engagementId)
                    .stream()
                    .map(WorkspaceTarget::getWorkspace)
                    .collect(Collectors.toList());

            if (workspaces.isEmpty()) {
                logs.append("No workspaces found for this Engagement ID | ");
                response.setSuccess(false);
                response.setErrors("No workspaces found for this Engagement ID");
                response.setLogs(logs.toString());
                return response;
            }

            response.setWorkspace(workspaces);
            logs.append("Workspaces retrieved | ");

            // **3. Validate the given workspace**
            Optional<WorkspaceTarget> workspaceOpt = workspaceRepository.findByEngagementIdAndWorkspace(engagementId, workspace);
            if (workspaceOpt.isEmpty()) {
                logs.append("Workspace not found for the given Engagement ID | ");
                response.setSuccess(false);
                response.setErrors("Workspace not found for the given Engagement ID");
                response.setLogs(logs.toString());
                return response;
            }

            WorkspaceTarget workspaceEntity = workspaceOpt.get();
            response.setDpHost(workspaceEntity.getDpHostUrl());
            logs.append("Workspace validated | ");

            // **4. Fetch CP Admin URL**
            Optional<CpMaster> cpOpt = cpMasterRepository.findByRegionAndEnvironment(engagement.getRegion(), workspaceEntity.getEnvironment());
            if (cpOpt.isPresent()) {
                response.setCpUrl(cpOpt.get().getCpAdminApiUrl());
                logs.append("CP Admin URL found | ");
            } else {
                logs.append("CP Admin URL not found | ");
                response.setErrors("CP Admin URL not found");
            }

            // **5. Fetch DMZ Load Balancer**
            Optional<DmzLbMaster> dmzOpt = dmzRepository.findByRegionAndEnvironment(engagement.getRegion(), workspaceEntity.getEnvironment());
            if (dmzOpt.isPresent()) {
                response.setDmzLb(dmzOpt.get().getLoadBalancer());
                logs.append("DMZ Load Balancer found | ");
            } else {
                logs.append("DMZ Load Balancer not found | ");
                response.setErrors("DMZ Load Balancer not found");
            }

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
