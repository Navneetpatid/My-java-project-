package com.hsbc.hap.cdr.service.impl;

import com.hsbc.hap.cdr.dto.ResponseDto;
import com.hsbc.hap.cdr.model.*;
import com.hsbc.hap.cdr.repository.*;
import com.hsbc.hap.cdr.request.KongCerRequest;
import com.hsbc.hap.cdr.service.HapCDRService;
import com.hsbc.hap.cdr.mapper.RequestResponseMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HapCDRServiceImpl implements HapCDRService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HapCDRServiceImpl.class);

    private final EngagementTargetKongDao engagementTargetKongDao;
    private final WorkspaceTargetDetailsDao workspaceTargetDetailsDao;
    private final EngagementPluginDetailsDao engagementPluginDetailsDao;
    private final CpMasterDao cpMasterDao;
    private final RequestResponseMapper requestResponseMapper;

    @Override
    @Transactional
    public ResponseDto<String> processKongCerRequest(KongCerRequest request) {
        try {
            LOGGER.info("Processing KongCerRequest: {}", request);

            // Mapping and Saving EngagementTargetKong
            EngagementTargetKong engagementTargetKong = requestResponseMapper.mapToEngagementTargetKong(request);
            engagementTargetKongDao.save(engagementTargetKong);
            LOGGER.info("Saved EngagementTargetKong successfully");

            // Mapping and Saving WorkspaceTargetDetails
            WorkspaceTargetDetails workspaceTargetDetails = requestResponseMapper.mapToWorkspaceTargetDetails(request);
            workspaceTargetDetailsDao.save(workspaceTargetDetails);
            LOGGER.info("Saved WorkspaceTargetDetails successfully");

            // Mapping and Saving EngagementPluginDetail
            EngagementPluginDetail engagementPluginDetail = requestResponseMapper.mapToEngagementPluginDetail(request);
            engagementPluginDetailsDao.save(engagementPluginDetail);
            LOGGER.info("Saved EngagementPluginDetail successfully");

            // Mapping and Saving CpMaster
            CpMaster cpMaster = requestResponseMapper.mapToCpMaster(request);
            cpMasterDao.save(cpMaster);
            LOGGER.info("Saved CpMaster successfully");

            LOGGER.info("All data processed successfully");

            return new ResponseDto<>(HttpStatus.OK.value(), "Kong data saved successfully", null);

        } catch (Exception e) {
            LOGGER.error("Error processing KongCerRequest", e);
            return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error processing request", e.getMessage());
        }
    }
}
