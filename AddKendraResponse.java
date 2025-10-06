package com.hsbc.hap

class ErrorDetail {
    String errorCode
    String errorMessage

    String toString() {
        return "[ErrorDetail] errorCode: ${errorCode}, errorMessage: ${errorMessage}"
    }
}

class Job {
    String buildNumber
    String buildUserID
    String buildURL
    List<ErrorDetail> error
    String buildStart
    String buildEnd
    String buildStatus

    String toString() {
        return """[Job]
    buildNumber : ${buildNumber},
    buildUserID : ${buildUserID},
    buildURL    : ${buildURL},
    buildStart  : ${buildStart},
    buildEnd    : ${buildEnd},
    buildStatus : ${buildStatus},
    error       : ${error}
"""
    }
    }
