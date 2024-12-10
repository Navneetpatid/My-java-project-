package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPharmasict {

    private Integer pageIndex;
    private Integer pageSize;
//    private Long stateId;
//    private Long districtId;
    private String searchText;
    private String orderBy;
    private String columnName;

}
