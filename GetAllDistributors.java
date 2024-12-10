package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllDistributors {

    private Integer pageIndex;
    private Integer pageSize;

    private Long districtId;
    private Long stateId;
    private Long pinCode;
}
