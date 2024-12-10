package com.janaushadhi.adminservice.service;

import org.springframework.web.multipart.MultipartFile;

import com.janaushadhi.adminservice.requestpayload.DistributorRequestPayload;

import java.io.IOException;
import java.util.Map;

public interface DistributorService {

    public Map<String, Object> addDistributor(DistributorRequestPayload distributorRequestPayload);
    public Map<String, Object> distributorStatusUpdate(Long id, short status);
    public Map<String, Object> getByDistributorId(Long id);
    public Map<String, Object> updateDistributor(Long id);
    Map<Object, Object> uploadCsvFileToDataBase(MultipartFile docfile) throws IOException;
}
