package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;


@Data
public class BannerResponse {

    private Long id;

    private String bannerFile;

    private Short status;

    private String createdDate;

    private String updatedDate;

    private Long adminId;

    private String roleType;

    private Integer roleId;
    
    private Integer serialNo;
}
