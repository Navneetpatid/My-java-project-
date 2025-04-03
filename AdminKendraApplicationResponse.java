public class MapCERServiceImpl implements MapCERService {
    public CerGetResponse getCerEngagementData(String engagementId, String workspace) {
        // ... existing code ...
        
        if(dmzlbOpt.isPresent()) {
            logs.append(" Received DMZ Load Balancer: ").append(dmzlbOpt.get()).append(" ");  // Added space at end
            response.setDmzlb(dmzlbOpt.get());
        } else {
            String printData = "DMZ Load Balancer not found for " + workspace + " ";  // Added space at end
            logs.append(printData);
            errors.append(printData).append(" ");  // Added space at end
        }
        
        // ... existing code ...
        
        response.setSuccess(errors.length() == 0);
        response.setLogs(logs.toString().trim() + " ");  // Ensure trailing space
        response.setErrors(errors.toString().trim() + " ");  // Ensure trailing space
        
        catch(Exception e) {
            appendErrorAndLog(errors, logs, "Unexpected error: ", e.getMessage() + " ");  // Added space
            response.setSuccess(false);
            response.setErrors(errors.toString().trim() + " ");
            response.setLogs(logs.toString().trim() + " ");
        }
        return response;
    }
}
