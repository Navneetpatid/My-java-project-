package com.hsbc.hap.cdr.controller;

import com.hsbc.hap.cdr.dto.ResponseDto;
import com.hsbc.hap.cdr.request.KongCerRequest;
import com.hsbc.hap.cdr.service.HapCDRService;
import com.hsbc.hap.cdr.util.BindingValidationUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/cdr")
@RequiredArgsConstructor
public class CERController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CERController.class);
    private final HapCDRService hapCDRService;

    @PostMapping(value = "/add/kong/data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseDto<Map<String, Object>>> processKongCerRequest(
            @Valid @RequestBody KongCerRequest request, BindingResult result) {

        LOGGER.info("Received request to add CER Data");

        // Validate the request
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation failed",
                    null
            ));
        }

        // Process the request
        ResponseDto<Map<String, Object>> response = hapCDRService.processKongCerRequest(request);

        // Log the response
        LOGGER.info("CER Data Added Successfully");

        // Return structured response with actual saved data
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
