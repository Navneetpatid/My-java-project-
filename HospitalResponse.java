package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalResponse {

    private Long id;
    private Long hsplno;
    private String hsplid;
    private String hsplType;
    private String state;
    private String district;
    private String hsplName;
    private String address;
    private String pincode;
    private String currentStatus;
    private String creationDate;
    private String updationDate;
    private Integer serialNo;
}
