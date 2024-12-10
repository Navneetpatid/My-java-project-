package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class RoleRequestPayload {

    private  Long id;
    private  String role;
    private String description;
    private  Short status;
}
