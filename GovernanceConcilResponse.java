package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GovernanceConcilResponse {
    private  Long id;
    private String designation;
    private String gcMember;
    private Integer serialNo;
}
