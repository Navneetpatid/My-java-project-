package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddKendraResponse {
    private Long id;
    private String contactPerson;
    private String storeCode;
    private Long pinCode;
    private Long stateId;
    private Long districtId;
    private String stateName;
    private String districtName;
    private String kendraAddress;
    private String createdDate;
    private String updatedDate;
    private Short status;
    private String latitude;
    private String longitude;
    private Integer serialNo;

}
