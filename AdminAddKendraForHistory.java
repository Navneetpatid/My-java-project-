package com.example.service.impl;

import com.example.entity.CpMaster;
import com.example.entity.CpMasterId;
import com.example.repository.CpMasterRepository;
import com.example.service.HapCERService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class HapCERServiceImpl implements HapCERService {

    @Autowired
    private CpMasterRepository cpMasterRepository;

    @Override
    public Map<String, Object> getCerEngagementData(String engagementId, String region, String platform, String environment) {
        Map<String, Object> response = new HashMap<>();
        StringBuilder logs = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        logs.append("No mandatory plugins found | ");

        // Fetch CP Admin API URL
        if (region != null && platform != null && environment != null) {
            CpMasterId cpMasterId = new CpMasterId(region, platform, environment);
            Optional<CpMaster> cpMasterOpt = cpMasterRepository.findById(cpMasterId);

            if (cpMasterOpt.isPresent()) {
                CpMaster cpMaster = cpMasterOpt.get();
                response.put("cp_url", cpMaster.getCpAdminApiUrl());
                logs.append("CP Admin API URL fetched | ");
            } else {
                errors.append("CP Admin API URL not found | ");
                logs.append("CP Admin API URL not found | ");
            }
        } else {
            errors.append("Invalid input, cannot fetch CP Admin API URL | ");
            logs.append("Invalid input, cannot fetch CP Admin API URL | ");
        }

        response.put("logs", logs.toString());
        response.put("errors", errors.toString());

        return response;
    }
}
