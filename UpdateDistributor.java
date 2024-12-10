package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDistributor {

    private Long id;
    private String nameOfFirm;
    private String email;
    private String contactNumber;
    private String code;
    private String distributorAddress;
    private Long stateId;
    private Long districtId;
    private Long cityId;
    private  Short status;
}
