package com.hsbc.hap.cdr.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KongApiResponseDTO {
    private String success;
    private String errors;
    private String workspace;
    private String cpUrl;
    private List<String> mandatoryPlugins;
    private String dpHost;
    private String gbgf;
    private String dmzLb;
    private String logs;
}
