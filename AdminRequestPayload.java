public class EngagementValidationResponse {
    private boolean success;
    private String gbgf;
    private String workspace;
    private String dpHost;
    private List<String> mandatoryPlugins;
    private String cpAdminApiUrl;
    private String dpHostUrl;
    private String logs;
    private String errors;

    // Constructors
    public EngagementValidationResponse() {
        this.success = true;
        this.gbgf = "";
        this.workspace = "";
        this.dpHost = "";
        this.mandatoryPlugins = new ArrayList<>();
        this.cpAdminApiUrl = "";
        this.dpHostUrl = "";
        this.logs = "";
        this.errors = "";
    }

    // Builder method for fluent construction
    public static EngagementValidationResponse builder() {
        return new EngagementValidationResponse();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public EngagementValidationResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getGbgf() {
        return gbgf;
    }

    public EngagementValidationResponse setGbgf(String gbgf) {
        this.gbgf = gbgf;
        return this;
    }

    // ... similar getters and setters for all fields (omitted for brevity)
    // Each setter should return 'this' for method chaining

    // Helper methods
    public EngagementValidationResponse appendLog(String message) {
        if (!this.logs.isEmpty()) {
            this.logs += " | ";
        }
        this.logs += message;
        return this;
    }

    public EngagementValidationResponse appendError(String message) {
        if (!this.errors.isEmpty()) {
            this.errors += " | ";
        }
        this.errors += message;
        return this;
    }
}
