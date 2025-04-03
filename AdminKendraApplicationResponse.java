public class HapCERServiceImpl implements HapCERService {
    
    public CerGetResponse getCerEngagementData(String engagementId, String workspace) {
        // Initialize necessary objects
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        CerGetResponse response = new CerGetResponse();

        try {
            // Main logic would go here
            
            response.setSuccess(errors.length() == 0);
            response.setLogs(logs.toString().trim() + " ");      // Added proper spacing
            response.setErrors(errors.toString().trim() + " ");  // Added proper spacing
            
        } catch(Exception e) {
            appendErrorAndLog(errors, logs, "Unexpected error: ", e.getMessage() + " ");  // Added proper spacing
            response.setSuccess(false);
            response.setErrors(errors.toString().trim() + " ");
            response.setLogs(logs.toString().trim() + " ");
        }

        return response;
    }

    private CerGetResponse buildErrorResponse1(String errorMessage, String logMessage) {
        CerGetResponse response = new CerGetResponse();
        response.setErrors(errorMessage.trim() + " ");    // Added proper spacing
        response.setLogs(logMessage.trim() + " ");       // Added proper spacing
        response.setSuccess(false);
        return response;
    }
}
