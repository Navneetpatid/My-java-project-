package com.hsbc.hap.cdr.controller;

import com.hsbc.hap.cdr.service.HapCDRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hap-cdr")
public class HapCDRController {

    @Autowired
    private HapCDRService hapCDRService;

    @GetMapping("/validate")
    public Map<String, Object> validateWorkspace(
            @RequestParam String engagementId,
            @RequestParam String workspace) {
        return hapCDRService.validateWorkspaceForEngagement(engagementId, workspace);
    }
}
