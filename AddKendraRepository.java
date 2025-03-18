package com.hsbc.hap.cdr.service;

import com.hsbc.hap.cdr.dto.CERResponseDTO;
import com.hsbc.hap.cdr.entity.*;
import com.hsbc.hap.cdr.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public CERResponseDTO getCERData(String engagementId, String region, String platform, String environment) {
        CERResponseDTO responseDTO = new CERResponseDTO();

        // Fetch cp_master data
        Optional<CpMaster> cpMaster = cpMasterRepository.findByRegionAndPlatformAndEnvironment(region, platform, environment);
        cpMaster.ifPresent(responseDTO::setCpMaster);

        // Fetch engagement_plugin data
        List<EngagementPlugin> engagementPlugins = engagementPluginRepository.findByEngagementId(engagementId);
        responseDTO.setEngagementPlugins(engagementPlugins);

        // Fetch engagement_target data
        Optional<EngagementTarget> engagementTarget = engagementTargetRepository.findByEngagementId(engagementId);
        engagementTarget.ifPresent(responseDTO::setEngagementTarget);

        // Fetch workspace_target data
        List<WorkspaceTarget> workspaceTargets = workspaceTargetRepository.findByEngagementId(engagementId);
        responseDTO.setWorkspaceTargets(workspaceTargets);

        return responseDTO;
    }
}
