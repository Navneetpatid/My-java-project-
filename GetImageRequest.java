package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetImageRequest {

    private Integer pageIndex;
    private Integer pageSize;
    private String searchText;
    private String orderBy;
    private String columnName;
}
