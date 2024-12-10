package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutiveResponse {
    private  Long id;
    private String designation;
    private String ecMember;
    private Integer serialNo;
}
