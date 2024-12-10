package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class ProductSearchRequest {

    private Integer pageIndex;
    private Integer pageSize;

    private String query;
}
