package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllMedia {


    private Integer pageIndex;
    private Integer pageSize;
    private String month;
    private int year;
//    private String orderBy;
//    private String columnName;
}
