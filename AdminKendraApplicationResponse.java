package com.hsbc.hap.cer.service.impl;

import com.hsbc.hap.cer.entity.DmzLbMaster;
import com.hsbc.hap.cer.repository.DmzLbMasterRepository;
import com.hsbc.hap.cer.model.LibDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DmzLbMasterService {

    private final DmzLbMasterRepository dmzLbMasterRepository;

    @Autowired
    public DmzLbMasterService(DmzLbMasterRepository dmzLbMasterRepository) {
        this.dmzLbMasterRepository = dmzLbMasterRepository;
    }

    public LibDetails getDmzLbById(Long id) {
        LibDetails libDetails = new LibDetails();
        libDetails.setLogs("DMZ Load Balancer lookup started");

        try {
            Optional<DmzLbMaster> dmzLbMaster = dmzLbMasterRepository.findById(id);
            
            if (dmzLbMaster.isPresent()) {
                libDetails.setLb(dmzLbMaster.get().getLoadBalancer());
                libDetails.setSuccess(true);
                libDetails.setLogs(libDetails.getLogs() + " | DMZ Load Balancer found");
            } else {
                libDetails.setErrors("DMZ Load Balancer not found for ID: " + id);
                libDetails.setLogs(libDetails.getLogs() + " | DMZ Load Balancer not found");
            }
        } catch (Exception e) {
            libDetails.setErrors("Error retrieving DMZ Load Balancer: " + e.getMessage());
            libDetails.setLogs(libDetails.getLogs() + " | Error: " + e.getMessage());
        }

        return libDetails;
    }

    public LibDetails getDmzLbByEnvironmentAndRegion(String environment, String region) {
        LibDetails libDetails = new LibDetails();
        libDetails.setLogs("DMZ Load Balancer lookup started");

        try {
            Optional<DmzLbMaster> dmzLbMaster = dmzLbMasterRepository
                .findByEnvironmentAndRegion(environment, region);
            
            if (dmzLbMaster.isPresent()) {
                libDetails.setLb(dmzLbMaster.get().getLoadBalancer());
                libDetails.setSuccess(true);
                libDetails.setLogs(libDetails.getLogs() + " | DMZ Load Balancer found");
            } else {
                libDetails.setErrors(String.format(
                    "DMZ Load Balancer not found for Environment: %s, Region: %s", 
                    environment, region));
                libDetails.setLogs(libDetails.getLogs() + " | DMZ Load Balancer not found");
            }
        } catch (Exception e) {
            libDetails.setErrors("Error retrieving DMZ Load Balancer: " + e.getMessage());
            libDetails.setLogs(libDetails.getLogs() + " | Error: " + e.getMessage());
        }

        return libDetails;
    }
        }
