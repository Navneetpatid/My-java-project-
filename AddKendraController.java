package com.hsbc.hap.cdr.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class KongCerRequest {
    
    @NotEmpty(message = "Engagement ID can't be empty!")
    @NotNull(message = "Engagement ID can't be null!")
    private String engagementId;

    @NotEmpty(message = "Mandatory Plugin can't be empty!")
    @NotNull(message = "Mandatory Plugin can't be null!")
    private String mandatoryPlugin;

    @NotEmpty(message = "Region can't be empty!")
    @NotNull(message = "Region can't be null!")
    private String region;

    @NotEmpty(message = "Network Region can't be empty!")
    @NotNull(message = "Network Region can't be null!")
    private String networkRegion;

    @NotEmpty(message = "Workspace can't be empty!")
    @NotNull(message = "Workspace can't be null!")
    private String workspace;

    @NotEmpty(message = "Environment can't be empty!")
    @NotNull(message = "Environment can't be null!")
    private String environment;

    @NotEmpty(message = "DP Platform can't be empty!")
    @NotNull(message = "DP Platform can't be null!")
    private String dpPlatform;

    @NotEmpty(message = "DP Host URL can't be empty!")
    @NotNull(message = "DP Host URL can't be null!")
    private String dpHostUrl;

    @NotEmpty(message = "CP Admin API URL can't be empty!")
    @NotNull(message = "CP Admin API URL can't be null!")
    private String cpAdminApiUrl;

    @NotEmpty(message = "GBOF can't be empty!")
    @NotNull(message = "GBOF can't be null!")
    private String gbof;

    // Getters and Setters
    public String getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(String engagementId) {
        this.engagementId = engagementId;
    }

    public String getMandatoryPlugin() {
        return mandatoryPlugin;
    }

    public void setMandatoryPlugin(String mandatoryPlugin) {
        this.mandatoryPlugin = mandatoryPlugin;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNetworkRegion() {
        return networkRegion;
    }

    public void setNetworkRegion(String networkRegion) {
        this.networkRegion = networkRegion;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getDpPlatform() {
        return dpPlatform;
    }

    public void setDpPlatform(String dpPlatform) {
        this.dpPlatform = dpPlatform;
    }

    public String getDpHostUrl() {
        return dpHostUrl;
    }

    public void setDpHostUrl(String dpHostUrl) {
        this.dpHostUrl = dpHostUrl;
    }

    public String getCpAdminApiUrl() {
        return cpAdminApiUrl;
    }

    public void setCpAdminApiUrl(String cpAdminApiUrl) {
        this.cpAdminApiUrl = cpAdminApiUrl;
    }

    public String getGbof() {
        return gbof;
    }

    public void setGbof(String gbof) {
        this.gbof = gbof;
    }
    }
