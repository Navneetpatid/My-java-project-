package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacistResponsepayload {


    private Long id;
    private String name;
    private String email;
    private String contactNumber;
    private Long stateId;
    private Long districtId;
    private String districtName;
    private String stateName;
    private Long pinCode;
    private String address;
    private String createdDate;
    private String updatedDate;
    private  Short status;
    private Integer serialNo;

}
