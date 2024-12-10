package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.BannerRequest;
import com.janaushadhi.adminservice.requestpayload.GetBannerRequest;
import com.janaushadhi.adminservice.requestpayload.StatusRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteBanner;
import com.janaushadhi.adminservice.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/banner")
public class BannerController {

    @Autowired
    BannerService bannerService;

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @PostMapping(value = "addBanner")
    public Map<Object, Object> addBanner(@RequestBody BannerRequest request) {
        return bannerService.addBanner(request);
    }

    /**
     * @param id
     * @return
     * @SameerKhan
     */
    @PostMapping("getBannerById")
    public Map<Object, Object> getBannerById(@RequestParam Long id) {
        return bannerService.getBannerById(id);

    }

    /**
     * @return
     * @SameerKhan
     */
    @PostMapping(value = "getAllBanner")
    public Map<String, Object> getAllBanner(@RequestBody GetBannerRequest request) {
        return bannerService.getAllBanner(request);
    }

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @PostMapping("/updateBannerStatus")
    public Map<Object, Object> updateBannerStatus(@RequestBody StatusRequest request) {
        return bannerService.updateBannerStatus(request.getId(), request.getStatus());

    }

    @PostMapping(value = "getAllDeleteBanner")
    public Map<String, Object> getAllDeleteBanner(@RequestBody GetAllDeleteBanner request) {
        return bannerService.getAllDeleteBanner(request);
    }

}
