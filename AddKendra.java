import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HapCDRServiceImpl implements HapCDRService {
    private static final Logger LOGGER = LogManager.getLogger(HapCDRServiceImpl.class);

    public String processKongCerRequest(KongCerRequest request) {
        String response = "Processing completed successfully";
        try {
            LOGGER.info("Received request: {}", request);

            EngagementTargetKong engagementTargetKong = requestResponseMapper.mapToEngagementTargetKong(request);
            engagementTargetKongDao.save(engagementTargetKong);
            LOGGER.info("Saved EngagementTargetKong successfully");

            WorkspaceTargetDetails workspaceTargetDetails = requestResponseMapper.mapToWorkspaceTargetDetails(request);
            workspaceTargetDetailsDao.save(workspaceTargetDetails);
            LOGGER.info("Saved WorkspaceTargetDetails successfully");

            EngagementPluginDetail engagementPluginDetail = requestResponseMapper.mapToEngagementPluginDetail(request);
            engagementPluginDetailsDao.save(engagementPluginDetail);
            LOGGER.info("Saved EngagementPluginDetail successfully");

            CpMaster cpMaster = requestResponseMapper.mapToCpMaster(request);
            cpMasterDetailsDao.save(cpMaster);
            LOGGER.info("Saved CpMaster successfully");

            engagementTargetKongDao.save(engagementTargetKong);
            LOGGER.info("Re-saved EngagementTargetKong successfully");

        } catch (Exception e) {
            LOGGER.error("Error processing request: {}", e.getMessage(), e);
            throw new ApplicationException(e.getMessage());
        }

        LOGGER.info("Returning response: {}", response);
        return response;
    }
}
