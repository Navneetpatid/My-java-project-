package com.example.service;

import com.example.dto.ValidationResponseDTO;
import com.example.entity.EngagementTarget;
import com.example.entity.WorkspaceTargetDetails;
import com.example.entity.CpMaster;
import com.example.entity.DmzLbMaster;
import com.example.repository.EngagementRepository;
import com.example.repository.WorkspaceTargetDetailsDao;
import com.example.repository.CpMasterDetailsDao;
import com.example.repository.DmzRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    private final EngagementRepository engagementRepository;
    private final WorkspaceTargetDetailsDao workspaceTargetDetailsDao;
    private final CpMasterDetailsDao cpMasterDetailsDao;
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
            Optional<WorkspaceTargetDetails> workspaceOpt =
                    workspaceTargetDetailsDao.findByEngagementIdAndWorkspace(engagementId, workspace);
            if (workspaceOpt.isEmpty()) {
                logs.append("Workspace not found for given Engagement ID | ");
                response.setSuccess(false);
                response.setErrors("Workspace not found for given Engagement ID");
                response.setLogs(logs.toString());
                return response;
            }

            WorkspaceTargetDetails workspaceEntity = workspaceOpt.get();
            response.setWorkspace(workspace);
            response.setDpHost(workspaceEntity.getDpHostUrl());
            logs.append("Workspace validated | ");

            // **3. Fetch CP Admin URL**
            Optional<CpMaster> cpOpt = cpMasterDetailsDao.findByRegionAndEnvironment(
                    engagement.getRegion(), workspaceEntity.getEnvironment());
            response.setCp_url(cpOpt.map(CpMaster::getCpAdminApiUrl).orElse(""));
            logs.append(cpOpt.isPresent() ? "CP Admin URL found | " : "CP Admin URL not found | ");

            // **4. Fetch DMZ Load Balancer**
            Optional<DmzLbMaster> dmzOpt = dmzRepository.findByRegionAndEnvironment(
                    engagement.getRegion(), workspaceEntity.getEnvironment());
            response.setDmz_lb(dmzOpt.map(DmzLbMaster::getLoadBalancer).orElse(""));
            logs.append(dmzOpt.isPresent() ? "DMZ Load Balancer found | " : "DMZ Load Balancer not found | ");

            // **5. Static / Default values for missing fields**
            response.setMandatoryPlugins("DefaultPluginList"); // Replace with actual logic if needed
            response.setGbgf("GBGF_Value"); // Fetch dynamically if required

            response.setSuccess(true);
            response.setLogs(logs.toString());

            logger.info("Validation Successful for Engagement ID: {}", engagementId);
            return response;

        } catch (Exception e) {
            logs.append("Database error: ").append(e.getMessage()).append(" | ");
            response.setSuccess(false);
            response.setErrors("Internal Server Error");
            response.setLogs(logs.toString());

            logger.error("Validation failed due to an exception: ", e);
            return response;
        }
    }
}
