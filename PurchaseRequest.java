package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class PurchaseRequest {
    private String yearOfPurchase;
    private String nitFile;
}
