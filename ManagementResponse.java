package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagementResponse {


    private Long id;
    private String name;
    private String eduQualification;
    private String contactNo;
    private String designation;
    private String mail;
    private String fax;
    private String photo;
    private Date createdDate;
    private Date updatedDate;
    private Short status;
    private Integer serialNo;
    private boolean imageIsActive;
}
