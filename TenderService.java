package com.janaushadhi.adminservice.service;

import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.TenderRequestPayload;

public interface TenderService {

    public Map<String,Object> addTender(TenderRequestPayload tenderRequestPayload);
    public Map<String, Object> updateTender(TenderRequestPayload tenderRequestPayload);

}
