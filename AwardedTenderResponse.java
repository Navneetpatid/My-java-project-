package com.janaushadhi.adminservice.responsepayload;

import com.janaushadhi.adminservice.entity.Purchase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardedTenderResponse {
    private Long id;
    private String yearOfPurchase;
    private String nitFile;
    private String department;
    private List<Purchase> purchaseList;
    private Short status;
    private Integer serialNo;
}
