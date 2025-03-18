package com.hsbc.hap.cdr.controller;

import com.hsbc.hap.cdr.dto.KongApiResponseDTO;
import com.hsbc.hap.cdr.service.CERService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kong")
public class CERController {

    private final CERService cerService;

    public CERController(CERService cerService) {
        this.cerService = cerService;
    }

    @GetMapping("/getData")
    public ResponseEntity<KongApiResponseDTO> getKongApiData(
        @RequestParam String engagementId,
        @RequestParam String region,
        @RequestParam String platform,
        @RequestParam String environment
    ) {
        KongApiResponseDTO responseDTO = cerService.getKongApiData(engagementId, region, platform, environment);
        return ResponseEntity.ok(responseDTO);
    }
}
