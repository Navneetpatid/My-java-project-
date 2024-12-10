package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class BannerRequest {

    private Long id;

    private String bannerFile;

    private Long adminId;

    private String roleType;

    private Integer roleId;
}
