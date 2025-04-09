package com.hsbc.hap.cer.model;

public class QueryResult {
    private String query;
    private boolean success;
    private String errorMessage;

    public QueryResult() {}

    public QueryResult(String query, boolean success, String errorMessage) {
        this.query = query;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
