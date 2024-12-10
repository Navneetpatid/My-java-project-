package com.janaushadhi.adminservice.responsepayload;
import lombok.Data;

@Data
public class BlackListResponse {
    private Long id;
    private String drugName;
    private String drugCode;
    private String manufacturedBy;
    private String fromDate;
    private String toDate;
    private String uploadImage;
    private String reason;
    private Short status;
    private String createdDate;
    private  String updatedDate;
    private Integer serialNo;
}
