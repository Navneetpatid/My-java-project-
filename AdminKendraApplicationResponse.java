public class HapCERServiceImpl implements HapCERService {
    
    public CerGetResponse getCerEngagementData(String engagementId, String workspace) {
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        CerGetResponse response = new CerGetResponse();
        final String LOG_SEPARATOR = " || ";  // Separator between log messages

        try {
            // Example log messages (replace with your actual logging logic)
            logs.append("EngagementId Validated").append(LOG_SEPARATOR);
            logs.append("Workspace Validated").append(LOG_SEPARATOR);
            logs.append("Mandatory plugins fetched").append(LOG_SEPARATOR);
            
            // DMZ Load Balancer check
            if(dmzlbOpt.isPresent()) {
                logs.append("Received DMZ Load Balancer: ").append(dmzlbOpt.get()).append(LOG_SEPARATOR);
                response.setDmzlb(dmzlbOpt.get());
            } else {
                logs.append("DMZ Load Balancer not found for ").append(workspace).append(LOG_SEPARATOR);
                errors.append("DMZ Load Balancer not found").append(LOG_SEPARATOR);
            }

            response.setSuccess(errors.length() == 0);
            response.setLogs(logs.toString());
            response.setErrors(errors.toString());
            
        } catch(Exception e) {
            logs.append("Unexpected error: ").append(e.getMessage()).append(LOG_SEPARATOR);
            errors.append("Unexpected error: ").append(e.getMessage()).append(LOG_SEPARATOR);
            response.setSuccess(false);
            response.setErrors(errors.toString());
            response.setLogs(logs.toString());
        }

        return response;
    }

    private CerGetResponse buildErrorResponse1(String errorMessage, String logMessage) {
        CerGetResponse response = new CerGetResponse();
        response.setErrors(errorMessage + " || ");
        response.setLogs(logMessage + " || ");
        response.setSuccess(false);
        return response;
    }
}
