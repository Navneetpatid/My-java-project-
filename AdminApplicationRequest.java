public class CERGetResponse {
    private String success;
    private String errors;
    private String workspace;
    private String cpUrl;
    private String mandatoryPlugins;
    private String dpHost;
    private String gbgf;
    private String dmzLb;
    private String logs;

    // Constructor
    public CERGetResponse() {
        // Initialize with default values if needed
    }

    // Getters and Setters

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getCpUrl() {
        return cpUrl;
    }

    public void setCpUrl(String cpUrl) {
        this.cpUrl = cpUrl;
    }

    public String getMandatoryPlugins() {
        return mandatoryPlugins;
    }

    public void setMandatoryPlugins(String mandatoryPlugins) {
        this.mandatoryPlugins = mandatoryPlugins;
    }

    public String getDpHost() {
        return dpHost;
    }

    public void setDpHost(String dpHost) {
        this.dpHost = dpHost;
    }

    public String getGbgf() {
        return gbgf;
    }

    public void setGbgf(String gbgf) {
        this.gbgf = gbgf;
    }

    public String getDmzLb() {
        return dmzLb;
    }

    public void setDmzLb(String dmzLb) {
        this.dmzLb = dmzLb;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "CERGetResponse{" +
                "success='" + success + '\'' +
                ", errors='" + errors + '\'' +
                ", workspace='" + workspace + '\'' +
                ", cpUrl='" + cpUrl + '\'' +
                ", mandatoryPlugins='" + mandatoryPlugins + '\'' +
                ", dpHost='" + dpHost + '\'' +
                ", gbgf='" + gbgf + '\'' +
                ", dmzLb='" + dmzLb + '\'' +
                ", logs='" + logs + '\'' +
                '}';
    }
}
