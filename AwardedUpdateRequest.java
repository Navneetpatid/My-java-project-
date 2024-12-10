package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardedUpdateRequest {

    private Long awardedTenderId;
    private String yearOfPurchase;
    private String nitFile;
    private List<PurchaseRequest>purchaseRequests;

}
