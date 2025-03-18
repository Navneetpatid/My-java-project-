import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class HapCDRServiceImpl implements HapCDRService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HapCDRServiceImpl.class);

    @Override
    public String processKongCerRequest(@Valid KongCerRequest request) {
        // Validate fields manually
        validateRequestFields(request);

        String response = "";
        try {
            LOGGER.info("Received request: {}", request);

            // Mapping and saving data
            EngagementTargetKong engagementTargetKong = requestResponseMapper.mapToEngagementTargetKong(request);
            engagementTargetKongDao.save(engagementTargetKong);
            LOGGER.info("Saved EngagementTargetKong successfully!");

            WorkspaceTargetDetails workspaceTargetDetails = requestResponseMapper.mapToWorkspaceTargetDetails(request);
            workspaceTargetDetailsDao.save(workspaceTargetDetails);
            LOGGER.info("Saved WorkspaceTargetDetails successfully!");

            EngagementPluginDetail engagementPluginDetail = requestResponseMapper.mapToEngagementPluginDetail(request);
            engagementPluginDetailsDao.save(engagementPluginDetail);
            LOGGER.info("Saved EngagementPluginDetail successfully!");

            CpMaster cpMaster = requestResponseMapper.mapToCpMaster(request);
            cpMasterDetailsDao.save(cpMaster);
            LOGGER.info("Saved CpMaster successfully!");

            engagementTargetKongDao.save(engagementTargetKong);
            LOGGER.info("Re-saved EngagementTargetKong successfully!");

            // Creating the response JSON
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Kong data saved successfully!");

            return responseMap.toString();
        } catch (Exception e) {
            LOGGER.error("Error while processing KongCerRequest: {}", e.getMessage());
            throw new RuntimeException("Internal Server Error: " + e.getMessage());
        }
    }

    // Method to validate request fields
    private void validateRequestFields(KongCerRequest request) {
        if (request.getEngagementId() == null || request.getEngagementId().trim().isEmpty()) {
            throw new ValidationException("Engagement ID can't be empty!");
        }
        if (request.getMandatoryPlugin() == null || request.getMandatoryPlugin().trim().isEmpty()) {
            throw new ValidationException("Mandatory Plugin can't be empty!");
        }
        if (request.getDpPlatform() == null || request.getDpPlatform().trim().isEmpty()) {
            throw new ValidationException("DP Platform can't be empty!");
        }
        if (request.getRegion() == null || request.getRegion().trim().isEmpty()) {
            throw new ValidationException("Region can't be empty!");
        }
        if (request.getEnvironment() == null || request.getEnvironment().trim().isEmpty()) {
            throw new ValidationException("Environment can't be empty!");
        }
        if (request.getDpHostUrl() == null || request.getDpHostUrl().trim().isEmpty()) {
            throw new ValidationException("DP Host URL can't be empty!");
        }
    }
            }
