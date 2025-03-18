import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class HapCDRServiceImpl implements HapCDRService {

    public ResponseEntity<Map<String, String>> processKongCerRequest(@Valid KongCerRequest request) {
        Map<String, String> responseMap = new HashMap<>();
        
        try {
            engagementPluginDetailsDao.save(engagementPluginDetail);
            LOGGER.info("Saved EngagementPluginDetail successfully!");

            CpMaster cpMaster = requestResponseMapper.mapToCpMaster(request);
            cpMasterDetailsDao.save(cpMaster);
            LOGGER.info("Saved CpMaster successfully!");

            engagementTargetKongDao.save(engagementTargetKong);
            LOGGER.info("Saved EngagementTargetKong successfully!");

            // Creating JSON response
            responseMap.put("message", "Kong data saved successfully!");
            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            LOGGER.error("Error while processing KongCerRequest: {}", e.getMessage());
            
            responseMap.put("error", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(500).body(responseMap);
        }
    }
}
