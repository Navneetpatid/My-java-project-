package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class GetAllHospital {
    private Integer pageIndex;
    private Integer pageSize;
    private String hsplType;
    private String state;
    private String district;
}
