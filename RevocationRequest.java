package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class RevocationRequest {
    private Long id;
    private String drugName;
    private String drugCode;
    private String manufacturedBy;
    private String blackListDate;
    private String uploadImage;
    private String reason;
    private Short status;

}
