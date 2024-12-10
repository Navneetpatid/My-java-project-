package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class GetAllDeleteReport {
    private Integer pageIndex;
    private Integer pageSize;
    private String reportType;
}
