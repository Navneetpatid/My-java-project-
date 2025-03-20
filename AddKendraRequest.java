package com.example.controller;

import com.example.dto.ValidationResponseDTO;
import com.example.dto.ErrorResponseDTO;
import com.example.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validate")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @GetMapping("/{engagementId}/{workspace}")
    public ResponseEntity<?> validateEngagement(
            @PathVariable String engagementId,
            @PathVariable String workspace) {
        ValidationResponseDTO response = validationService.validateEngagement(engagementId, workspace);
        
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO(response.getErrors()));
        }

        return ResponseEntity.ok(response);
    }
}
