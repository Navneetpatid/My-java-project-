package com.janaushadhi.adminservice.service;

import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.GetImageRequest;
import com.janaushadhi.adminservice.requestpayload.UploadImageRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteImage;

public interface UploadImageService {

    //------------------------Add-Image------------------------
    Map<Object, Object> addImage(UploadImageRequest request);
    //------------------------GetAllImage----------------------
    Map<String, Object> getAllImage(GetImageRequest request);
    //------------------------DeleteImage----------------------
    Map<Object, Object> deleteImage(Long id);
    Map<String, Object> getAllDeleteImage(GetAllDeleteImage request);

}
