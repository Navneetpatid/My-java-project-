package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class GetAllAwardedTender {

    private Integer pageIndex;
    private Integer pageSize;
    private String searchText;
    private String orderBy;
    private String columnName;


}