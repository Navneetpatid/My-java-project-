package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class PurchaseResponse {
    private Long id;
    private String yearOfPurchase;
    private String nitFile;
}
