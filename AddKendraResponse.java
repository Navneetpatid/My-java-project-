package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponseDTO {
    private boolean success;
    private String errors;
    private List<String> workspace;
    private String cpUrl;
    private List<String> mandatoryPlugins;
    private String dpHost;
    private String gbgf;
    private String dmzLb;
    private String logs;
}
