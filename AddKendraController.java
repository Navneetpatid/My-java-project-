package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.AddKendraRequest;
import com.janaushadhi.adminservice.requestpayload.KendraDeleteBulk;
import com.janaushadhi.adminservice.requestpayload.NearByKendraRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteKendra;
import com.janaushadhi.adminservice.responsepayload.GetAllKendra;
import com.janaushadhi.adminservice.responsepayload.GetKendra;
import com.janaushadhi.adminservice.serviceimpl.AddKendraServiceImpl;
import com.janaushadhi.adminservice.util.DataConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/addKendra")
public class AddKendraController {

    @Autowired
    private AddKendraServiceImpl addKendraService;

    @PostMapping(value = "/addKendra")
    public ResponseEntity< Object> addKendra(@RequestBody AddKendraRequest addKendraRequest) throws IOException {
    	return  ResponseEntity.ok(addKendraService.addKendra(addKendraRequest)) ;
    }

    @GetMapping(value = "/kendraStatusUpdate")
    public ResponseEntity< Object> kendraStatusUpdate(@RequestParam Long id,@RequestParam short status){
        return ResponseEntity.ok(addKendraService.kendraStatusUpdate( id,status));
    }
    @GetMapping(value = "/getByKendraId")
    public Map<String, Object> getByKendraId(Long id) {
        return addKendraService.getByKendraId(id);
    }

    @PostMapping( value = "/getAllKendra")
    public Map<String,Object> getAllKendra(@RequestBody GetAllKendra getAllKendra){
    	return addKendraService.getAllKendra(getAllKendra);
    }


    @PostMapping(value = "/uploadKendraCsvFileToDataBase",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Object, Object> uploadCsvFileToDataBase(@ModelAttribute("docfile") MultipartFile docfile)throws IOException {
        HashMap<Object, Object> map = new HashMap<>();
        if (docfile != null) {
            return addKendraService.uploadCsvFileToDataBase(docfile);
        }
        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
        map.put(DataConstant.RESPONSE_BODY, null);
        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
        return map;
    }


    @PostMapping( value = "/getAllKendraByStateDistrict")
    public Map<String,Object> getAllKendraByStateDistrict(@RequestBody GetKendra getKendra){
        return addKendraService.getAllKendraByStateDistrict(getKendra);
    }

    @DeleteMapping(value = "/softDeleteKendras")
    public Map<Object, Object> deleteProductBulk(@RequestBody KendraDeleteBulk kendraIds) {
        return addKendraService.deleteKendraBulk(kendraIds);

    }

    @PostMapping( value = "/getAllDeleteKendra")
    public Map<String,Object> getAllDeleteKendra(@RequestBody GetAllDeleteKendra getAllKendra){
        return addKendraService.getAllDeleteKendra(getAllKendra);
    }
    @PostMapping( value = "/getNearByKendra")
    public Map<String,Object> nearByKendra(@RequestBody NearByKendraRequest nearByKendraRequest){
        return addKendraService.nearByKendra(nearByKendraRequest);
    }
}
