package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.entity.EventGallery;
import com.janaushadhi.adminservice.requestpayload.EventGalleryRequest;
import com.janaushadhi.adminservice.requestpayload.EventRequest;
import com.janaushadhi.adminservice.requestpayload.GetAllDeleteGallery;
import com.janaushadhi.adminservice.responsepayload.GetAllEventGallery;
import com.janaushadhi.adminservice.responsepayload.GetAllMedia;
import com.janaushadhi.adminservice.serviceimpl.MediaServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/admin/media")
public class MediaEventController {

    @Autowired
    private MediaServiceImpl mediaService;





    @PostMapping(value = "/addEventCalender")
    public ResponseEntity< Object> addEventCalender(@RequestBody EventRequest eventRequest) throws IOException {
       return  ResponseEntity.ok(mediaService.addEventCalender(eventRequest)) ;
    }



    @PostMapping( value = "/getAllMediaEventByMonthAndYear")
    public Map<String,Object> getAllMediaEventByMonthAndYear(@RequestBody GetAllMedia getAllMedia){
        return mediaService.getAllMediaEventByMonthAndYear(getAllMedia);
    }

    @PostMapping(value = "/addEventGallery")
    public ResponseEntity< Object> addEventGallery(@RequestBody EventGalleryRequest eventRequest) throws IOException {
        return  ResponseEntity.ok(mediaService.addEventGallery(eventRequest)) ;
    }

    @PostMapping( value = "/getAllEventGallery")
    public Map<String,Object> getAllMediaGallery(@RequestBody GetAllEventGallery getAllMedia){
        return mediaService.getAllMediaGallery(getAllMedia);
    }
    @PostMapping( value = "/getAllDeleteEventGallery")
    public Map<String,Object> getAllDeleteEventGallery(@RequestBody GetAllDeleteGallery getAllMedia){
        return mediaService.getAllDeleteMediaGallery(getAllMedia);
    }
}
