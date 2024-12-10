package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class RevocationResponse {
    private Long id;
    private String drugName;
    private String drugCode;
    private String manufacturedBy;
    private String blackListDate;
    private String uploadImage;
    private String reason;
    private Short status;
    private String createdDate;
    private  String updatedDate;
    private Integer serialNo;
}
