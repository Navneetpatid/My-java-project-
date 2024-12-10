package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;


@Data
public class DebarredRequest {


    private Long id;
    private String drugName;
    private String drugCode;
    private String manufacturedBy;
    private String fromDate;
    private String toDate;
    private String reason;
    private Short status;

}
