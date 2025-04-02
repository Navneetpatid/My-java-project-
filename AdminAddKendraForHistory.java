import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class HapCERServiceImpl implements HapCERService {

    @Autowired
    private CpMasterDetailsDao cpMasterDetailsDao;

    public List<Map<String, Object>> getCerEngagementData(String engagementId, String workspace) {
        Map<String, Object> response = new HashMap<>();
        StringBuilder errors = new StringBuilder();
        StringBuilder logs = new StringBuilder();

        // Fetch CP Admin API URL
        if (workspaceTarget != null) {
            try {
                // METHOD 1: Using embedded ID (recommended)
                CpMasterId cpMasterId = new CpMasterId(
                    workspaceTarget.getRegion(),
                    workspaceTarget.getPlatform(),
                    workspaceTarget.getEnvironment()
                );
                
                Optional<CpMaster> cpMasterOpt = cpMasterDetailsDao.findById(cpMasterId);
                
                // METHOD 2: Alternative using custom query
                /*
                CpMaster cpMaster = cpMasterDetailsDao.findById_RegionAndId_PlatformAndId_Environment(
                    workspaceTarget.getRegion(),
                    workspaceTarget.getPlatform(),
                    workspaceTarget.getEnvironment()
                );
                */
                
                if (cpMasterOpt.isPresent()) {
                    CpMaster cpMaster = cpMasterOpt.get();
                    response.put("cp_admin_api_url", cpMaster.getCp_admin_api_url());
                    logs.append("CP Admin API URL fetched successfully");
                } else {
                    errors.append("CP configuration not found for region: ")
                          .append(workspaceTarget.getRegion())
                          .append(", platform: ")
                          .append(workspaceTarget.getPlatform())
                          .append(", environment: ")
                          .append(workspaceTarget.getEnvironment());
                }
            } catch (Exception e) {
                errors.append("Error fetching CP Admin URL: ").append(e.getMessage());
            }
        } else {
            errors.append("Workspace target is null - cannot fetch CP Admin API URL");
        }

        // Add errors/logs to response if needed
        if (errors.length() > 0) {
            response.put("errors", errors.toString());
        }
        if (logs.length() > 0) {
            response.put("logs", logs.toString());
        }

        return Collections.singletonList(response);
    }
                }
