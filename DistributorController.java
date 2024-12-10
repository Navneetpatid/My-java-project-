package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.DistributerDeletePayload;
import com.janaushadhi.adminservice.requestpayload.DistributorRequestPayload;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteDistributor;
import com.janaushadhi.adminservice.responsepayload.GetAllDistributor;
import com.janaushadhi.adminservice.responsepayload.GetAllDistributors;
import com.janaushadhi.adminservice.serviceimpl.DistributorServiceImpl;
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
@RequestMapping("/api/v1/admin/distributor")
public class DistributorController {

    @Autowired
    private DistributorServiceImpl distributorService;
    @PostMapping(value = "/addDistributor")
    public ResponseEntity< Object> addDistributor(@RequestBody DistributorRequestPayload distributorRequestPayload) throws IOException {
        return  ResponseEntity.ok(distributorService.addDistributor(distributorRequestPayload)) ;
    }

    @GetMapping(value = "/distributorStatusUpdate")
    public ResponseEntity<Map<String, Object>> distributorStatusUpdate(@RequestParam Long id,@RequestParam short status){
        return ResponseEntity.ok(distributorService.distributorStatusUpdate( id,status));
    }
    @GetMapping(value = "/getByDistributorId")
    public Map<String, Object> getByDistributorId(Long id) {
        return distributorService.getByDistributorId(id);
    }

    @PostMapping( value = "/getAllDistributor")
    public Map<String,Object> getAllDistributor(@RequestBody GetAllDistributor getAllDistributor){
        return distributorService.getAllDistributor(getAllDistributor);
    }


    @PostMapping( value = "/getAllDistributorByStateAndDistrict")
    public Map<String,Object> getAllDistributorByStateAndDistrict(@RequestBody GetAllDistributors getAllDistributor){
        return distributorService.getAllDistributorByStateAndDistrict(getAllDistributor);
    }


    @PostMapping(value = "/uploadCsvFileToDataBase",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Object, Object> uploadCsvFileToDataBase(@ModelAttribute("docfile") MultipartFile docfile)throws IOException {
        HashMap<Object, Object> map = new HashMap<>();
        if (docfile != null) {
            return distributorService.uploadCsvFileToDataBase(docfile);
        }
        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
        map.put(DataConstant.RESPONSE_BODY, null);
        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
        return map;
    }


    @GetMapping(value = "/getDistributourByDistrictId")
    public Map<String, Object> getDistributourByDistrictId( @RequestParam Long districtId) {
        return distributorService.getDistributourByDistrictId(districtId);
    }
    
    @DeleteMapping(value = "/bulkDeleteDistributer")
    public Map<Object, Object> deleteDistributerBulk(@RequestBody DistributerDeletePayload distributerIds) {
        return distributorService.deleteDistributerBulk(distributerIds);

    }

    @PostMapping( value = "/getAllDeleteDistributor")
    public Map<String,Object> getAllDeleteDistributor(@RequestBody GetAllDeleteDistributor getAllDistributor){
        return distributorService.getAllDeleteDistributor(getAllDistributor);
    }
}
