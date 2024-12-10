package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.GetImageRequest;
import com.janaushadhi.adminservice.requestpayload.UploadImageRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteImage;
import com.janaushadhi.adminservice.service.UploadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/uploadImage")
public class UploadImageController {


    @Autowired
    UploadImageService uploadImageService;

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @PostMapping(value = "addImage")
    public Map<Object, Object> addImage(@RequestBody UploadImageRequest request) {
        return uploadImageService.addImage(request);
    }

    /**
     * @return
     * @SameerKhan
     */
    @PostMapping(value = "getAllImage")
    public Map<String, Object> getAllImage(@RequestBody GetImageRequest request) {

        return uploadImageService.getAllImage(request);
    }

    /**
     * @param id
     * @return
     * @SameerKhan
     */
    @DeleteMapping(value = "/deleteImage/{id}")
    public Map<Object, Object> deleteImage(@PathVariable("id") Long id) {
        return uploadImageService.deleteImage(id);

    }
    @PostMapping(value = "getAllDeleteImage")
    public Map<String, Object> getAllDeleteImage(@RequestBody GetAllDeleteImage request) {
        return uploadImageService.getAllDeleteImage(request);
    }
}
