package com.hsbc.hap.cer.controller;

import com.hsbc.hap.cer.model.QueryRequest;
import com.hsbc.hap.cer.model.QueryResult;
import com.hsbc.hap.cer.service.HapCERService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hap/cer")
public class CERController {

    @Autowired
    private HapCERService hapCerService;

    @PostMapping("/bulkUpdate")
    public ResponseEntity<?> bulkUpdate(@RequestBody QueryRequest queryRequest) {
        try {
            List<QueryResult> resultList = hapCerService.executeQueries(queryRequest.getQueries());
            return ResponseEntity.ok(resultList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error: " + e.getMessage());
        }
    }
}
