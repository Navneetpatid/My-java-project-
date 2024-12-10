package com.janaushadhi.adminservice.requestpayload;
import lombok.Data;

@Data
public class AwardedTenderRequest {
    private Long id;
    private String yearOfPurchase;
    private String nitFile;
    private String department;
}
