package com.hsbc.hap.cer.service.impl;

import com.hsbc.hap.cer.dao.CpMasterDetailsDao;
import com.hsbc.hap.cer.dao.DmzLibMasterDao;
import com.hsbc.hap.cer.model.WorkspaceTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapCERServiceImpl {

    private final CpMasterDetailsDao cpMasterDetailsDao;
    private final DmzLibMasterDao dmzLibMasterDao;
    
    @Autowired
    public MapCERServiceImpl(CpMasterDetailsDao cpMasterDetailsDao, 
                           DmzLibMasterDao dmzLibMasterDao) {
        this.cpMasterDetailsDao = cpMasterDetailsDao;
        this.dmzLibMasterDao = dmzLibMasterDao;
    }

    public Map<String, Object> validateWorkspaceForEngagement(WorkspaceTarget workspaceTarget) {
        Map<String, Object> response = new HashMap<>();
        StringBuilder errors = new StringBuilder();
        StringBuilder logs = new StringBuilder();

        // Fetch CP Admin API URL
        if (workspaceTarget != null) {
            try {
                String apiUrl = cpMasterDetailsDao.findAdminApiUrlByEngagementAndWorkspace(
                    workspaceTarget.getEngagementId(), 
                    workspaceTarget.getWorkspace());
                
                if (StringUtils.hasText(apiUrl)) {
                    response.put("cp_admin_api_url", apiUrl);
                    logs.append("CP Admin API URL fetched ! ");
                } else {
                    errors.append("CP Admin API URL not found ! ");
                    logs.append("CP Admin API URL not found ! ");
                }
            } catch (Exception e) {
                errors.append("Error fetching CP Admin API URL ! ");
                logs.append("Database error occurred while fetching CP Admin API URL ! ");
            }
        } else {
            errors.append("Workspace not validated, cannot fetch CP Admin API URL ! ");
            logs.append("Workspace not validated, cannot fetch CP Admin API URL ! ");
        }

        // Fetch DMZ Load Balancer
        if (workspaceTarget != null) {
            try {
                DmzLibMaster dmzLibMaster = dmzLibMasterDao.findByEnvironmentAndRegion(
                    workspaceTarget.getEnvironment(), 
                    workspaceTarget.getRegion());
                
                if (dmzLibMaster != null) {
                    response.put("dmz_lb", dmzLibMaster.getLoadBalancer());
                    logs.append("DMZ Load Balancer fetched ! ");
                } else {
                    errors.append("DMZ Load Balancer not found ! ");
                    logs.append("DMZ Load Balancer not found ! ");
                }
            } catch (Exception e) {
                errors.append("Error fetching DMZ Load Balancer ! ");
                logs.append("Database error occurred while fetching DMZ Load Balancer ! ");
            }
        }

        // Add errors and logs to response if they exist
        if (errors.length() > 0) {
            response.put("errors", errors.toString());
        }
        response.put("logs", logs.toString());
        
        return response;
    }
}
