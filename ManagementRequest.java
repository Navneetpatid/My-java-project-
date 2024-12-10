package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagementRequest {


    private Long id;
    private String name;
    private String eduQualification;
    private String contactNo;
    private String designation;
    private String mail;
    private String fax;
    private String photo;
    private Short status;
}
