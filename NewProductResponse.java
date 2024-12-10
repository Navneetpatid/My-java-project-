package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class NewProductResponse {

    private Long productId;
    private String genericName;
    private String groupName;
    private Integer drugCode;
    private String unitSize;
    private Double mrp;
    private Short status;
    private Integer serialNo;

}
