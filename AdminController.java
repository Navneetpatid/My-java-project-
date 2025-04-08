package com.hsbc.hap.cer.service;

import com.hsbc.hap.cer.model.CerGetResponse;
import com.hsbc.hap.cer.model.EngagementTargetKong;
import com.hsbc.hap.cer.model.WorkspaceTarget;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MapCERServiceImpl implements MapCERService {

    @Override
    public CerGetResponse getCerEngagementData(String engagementId, String workspace) {
        CerGetResponse response = new CerGetResponse();
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        final String LOG_SEPARATOR = " || ";
        
        try {
            // Validate Engagement
            Optional<EngagementTargetKong> engagementTargetOpt = engagementTargetKongDao.findByEngagementId(engagementId);
            if (!engagementTargetOpt.isPresent()) {
                return buildErrorResponse("engagementId not found", "engagementId not found in engagement_target");
            }
            
            EngagementTargetKong engagementTarget = engagementTargetOpt.get();
            response.setGbgr(engagementTarget.getGbgr());
            logs.append("EngagementId Validated").append(LOG_SEPARATOR);

            // Validate Workspace
            Optional<WorkspaceTarget> workspaceTargetOpt = workspaceTargetDetailsDao.findById_EngagementIdAndId_Workspace(engagementId, workspace);
            if (!workspaceTargetOpt.isPresent()) {
                appendErrorAndLog(errors, logs, "Workspace not found", 
                    "Workspace not found for engagement ID " + engagementId);
            } else {
                WorkspaceTarget workspaceTarget = workspaceTargetOpt.get();
                response.setWorkspace(workspace);
                response.setOp_host_url(workspaceTarget.getOp_host_url());
                logs.append("Workspace Validated").append(LOG_SEPARATOR);
            }
            
            // Set logs and errors in response
            if (errors.length() > 0) {
                response.setStatus("ERROR");
                response.setErrorMessages(errors.toString());
            } else {
                response.setStatus("SUCCESS");
            }
            response.setLogs(logs.toString());
            
        } catch (Exception e) {
            return buildErrorResponse("System error: " + e.getMessage(), 
                   "Exception occurred: " + e.getClass().getSimpleName());
        }
        
        return response;
    }

    private CerGetResponse buildErrorResponse(String errorMessage, String logMessage) {
        CerGetResponse response = new CerGetResponse();
        response.setStatus("ERROR");
        response.setErrorMessages(errorMessage);
        response.setLogs(logMessage);
        return response;
    }

    private void appendErrorAndLog(StringBuilder errors, StringBuilder logs, 
                                 String errorMessage, String logMessage) {
        errors.append(errorMessage).append("; ");
        logs.append(logMessage).append("; ");
    }
}
