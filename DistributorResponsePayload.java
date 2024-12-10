package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributorResponsePayload {
    private  Long id;
    private String nameOfFirm;
    private String email;
    private String contactNumber;
    private String distributorAddress;
    private Long stateId;
    private Long districtId;
    private String stateName;
    private String districtName;
    private Long cityId;
    private Long pinCode;
    private String code;
    private  Short status;
    private String createdDate;
    private String updateddate;
    private Integer serialNo;
}
