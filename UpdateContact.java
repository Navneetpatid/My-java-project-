package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateContact {

private  Long id;
    private String name;
    private String designation;
    private String emailId;
    private String contactNo;
    private String intercomNo;
    private String department;
    private Short status;

}
