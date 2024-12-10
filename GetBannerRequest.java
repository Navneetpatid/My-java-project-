package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBannerRequest {

    private Integer pageIndex;
    private Integer pageSize;
    private String orderBy;
    private String columnName;
}
