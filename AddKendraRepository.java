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

@RestController
@RequestMapping("/api/cdr")
@RequiredArgsConstructor
public class CERController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CERController.class);
    private final HapCDRService hapCDRService;

    @PostMapping(value = "/add/kong/data", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseDto<Object>> processKongCerRequest(
            @Valid @RequestBody KongCerRequest request, BindingResult result) {

        LOGGER.info("Received request to add CER Data");

        // Validate the request
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Validation failed",
                    BindingValidationUtil.requestValidation(result)
            ));
        }

        // Process the request
        ResponseDto<String> response = hapCDRService.processKongCerRequest(request);

        // Log the response
        LOGGER.info("CER Data Added Successfully");

        // Return structured response
        return ResponseEntity.status(response.getStatusCode()).body(new ResponseDto<>(
                response.getStatusCode(),
                response.getMessage(),
                null
        ));
    }
    }
