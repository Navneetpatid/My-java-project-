package com.hsbc.hap.cdr.controller;

import com.hsbc.hap.cdr.service.HapCDRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hap-cdr")
public class HapCDRController {

    @Autowired
    private HapCDRService hapCDRService;

    @GetMapping(value = "/validate", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> validateWorkspace(
            @RequestParam(required = true) String engagementId,
            @RequestParam(required = true) String workspace) {

        List<Map<String, Object>> responseList = hapCDRService.validateWorkspaceForEngagement(engagementId, workspace);
        return ResponseEntity.ok(responseList);
    }
}
