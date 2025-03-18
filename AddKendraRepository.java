package com.hsbc.hap.cdr.service;

import com.hsbc.hap.cdr.dto.KongApiResponseDTO;
import com.hsbc.hap.cdr.entity.*;
import com.hsbc.hap.cdr.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CERService {

    private final CpMasterRepository cpMasterRepository;
    private final EngagementPluginRepository engagementPluginRepository;
    private final EngagementTargetRepository engagementTargetRepository;
    private final WorkspaceTargetRepository workspaceTargetRepository;

    public CERService(CpMasterRepository cpMasterRepository, EngagementPluginRepository engagementPluginRepository,
                      EngagementTargetRepository engagementTargetRepository, WorkspaceTargetRepository workspaceTargetRepository) {
        this.cpMasterRepository = cpMasterRepository;
        this.engagementPluginRepository = engagementPluginRepository;
        this.engagementTargetRepository = engagementTargetRepository;
        this.workspaceTargetRepository = workspaceTargetRepository;
    }

    public KongApiResponseDTO getKongApiData(String engagementId, String region, String platform, String environment) {
        KongApiResponseDTO responseDTO = new KongApiResponseDTO();
        StringBuilder errorMsg = new StringBuilder();

        // Default values
        responseDTO.setSuccess("true");
        responseDTO.setErrors("");
        responseDTO.setDmzLb("");
        responseDTO.setLogs("");

        // Fetch cp_master data (for cp_url)
        Optional<CpMaster> cpMaster = cpMasterRepository.findByRegionAndPlatformAndEnvironment(region, platform, environment);
        if (cpMaster.isPresent()) {
            responseDTO.setCpUrl(cpMaster.get().getCpAdminApiUrl());
        } else {
            errorMsg.append("Data not found in cp_master table. ");
        }

        // Fetch engagement_plugin data (for mandatoryPlugins)
        List<String> mandatoryPlugins = engagementPluginRepository.findByEngagementId(engagementId)
                .stream()
                .map(EngagementPlugin::getMandatoryPlugin)
                .collect(Collectors.toList());
        if (!mandatoryPlugins.isEmpty()) {
            responseDTO.setMandatoryPlugins(mandatoryPlugins);
        } else {
            errorMsg.append("Data not found in engagement_plugin table. ");
        }

        // Fetch engagement_target data (for gbgf)
        Optional<EngagementTarget> engagementTarget = engagementTargetRepository.findByEngagementId(engagementId);
        if (engagementTarget.isPresent()) {
            responseDTO.setGbgf(engagementTarget.get().getGbgf());
        } else {
            errorMsg.append("Data not found in engagement_target table. ");
        }

        // Fetch workspace_target data (for workspace and dpHost)
        List<WorkspaceTarget> workspaceTargets = workspaceTargetRepository.findByEngagementId(engagementId);
        if (!workspaceTargets.isEmpty()) {
            responseDTO.setWorkspace(workspaceTargets.get(0).getWorkspace()); // Assuming one workspace per engagement
            responseDTO.setDpHost(workspaceTargets.get(0).getDpHostUrl());
        } else {
            errorMsg.append("Data not found in workspace_target table. ");
        }

        // If any errors occurred, update response
        if (errorMsg.length() > 0) {
            responseDTO.setSuccess("false");
            responseDTO.setErrors(errorMsg.toString().trim());
        }

        return responseDTO;
    }
            }
