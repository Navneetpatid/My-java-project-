package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddKendraRequest {
    private Long id;
    private String contactPerson;
    private String storeCode;
    private Long pinCode;
    private Long stateId;
    private Long districtId;
    private String kendraAddress;
    private Short status;
    private String latitude;
    private String longitude;
}
