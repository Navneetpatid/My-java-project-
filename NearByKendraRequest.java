package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class NearByKendraRequest {

    private Long stateId;
    private Long districtId;
    private String latitude;
    private String longitude;
}
