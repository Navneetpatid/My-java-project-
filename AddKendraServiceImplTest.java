package com.hsbc.hap.cdr.controller;

import com.hsbc.hap.cdr.service.HapCDRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hap-cdr")
public class HapCDRController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HapCDRController.class);

    @Autowired
    private HapCDRService hapCDRService;

    @GetMapping(value = "/validate", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> validateWorkspace(
            @RequestParam(required = true) String engagementId,
            @RequestParam(required = true) String workspace) {

        LOGGER.info("Validating workspace for Engagement ID: {} and Workspace: {}", engagementId, workspace);

        List<Map<String, Object>> responseList = hapCDRService.validateWorkspaceForEngagement(engagementId, workspace);

        LOGGER.info("Validation completed for Engagement ID: {}, Response: {}", engagementId, responseList);

        return ResponseEntity.ok(responseList);
    }
}
