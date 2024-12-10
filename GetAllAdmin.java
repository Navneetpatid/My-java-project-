package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllAdmin {


    private Integer pageIndex;
    private Integer pageSize;
    private String searchText;
    private String orderBy;
    private String columnName;
    private Long adminId;
//    private Short status;

}
