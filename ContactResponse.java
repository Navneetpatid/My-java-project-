package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactResponse {
    private Long id;
    private String name;
    private String designation;
    private String emailId;
    private String contactNo;
    private String intercomNo;
    private String department;
    private String createdDate;
    private String updatedDate;
    private Short status;
    private Integer serialNo;
}
