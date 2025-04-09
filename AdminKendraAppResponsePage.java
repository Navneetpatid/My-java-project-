package com.example.demo.controller;

import com.example.demo.dto.QueryRequest;
import com.example.demo.service.BulkUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/update")
public class BulkUpdateController {

    @Autowired
    private BulkUpdateService bulkUpdateService;

    @PostMapping("/execute")
    public ResponseEntity<Object> bulkUpdate(@RequestBody QueryRequest queryRequest) {
        try {
            bulkUpdateService.executeQueries(queryRequest.getQuery());
            return ResponseEntity.ok("Update successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error occurred: " + e.getMessage());
        }
    }
}
