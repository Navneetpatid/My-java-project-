package com.janaushadhi.adminservice.service;

import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.BannerRequest;
import com.janaushadhi.adminservice.requestpayload.GetBannerRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteBanner;

public interface BannerService {

    //--------------------AddBanner------------------------------
    Map<Object, Object> addBanner(BannerRequest request);

    //--------------------Get-ById------------------------------
    Map<Object, Object> getBannerById(Long id);

    //--------------------Get-All-Banner--------------------
    Map<String, Object> getAllBanner(GetBannerRequest request);

    //--------------------Update-Banner-Status--------------------
    Map<Object, Object> updateBannerStatus(Long id, short status);

    Map<String, Object> getAllDeleteBanner(GetAllDeleteBanner request);

}
