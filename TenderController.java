package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.*;
import com.janaushadhi.adminservice.responsepayload.*;
import com.janaushadhi.adminservice.serviceimpl.TenderServiceImpl;
import com.janaushadhi.adminservice.util.CommonUtilService;
import com.janaushadhi.adminservice.util.DataConstant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/tender")

public class TenderController {

@Autowired
private TenderServiceImpl tenderService;
    @Autowired
    private CommonUtilService commonUtilService;

    @PostMapping(value = "/addTender")
    public ResponseEntity< Object> addTender(@Valid @RequestBody TenderRequestPayload tenderRequestPayload, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return commonUtilService.requestValidation(bindingResult);
        }
        return  ResponseEntity.ok(tenderService.addTender(tenderRequestPayload)) ;
    }
    @PutMapping (value = "/updateTender")
    public ResponseEntity< Object> addAmendment(  @RequestBody TenderUpdateRequestPayload tenderUpdateRequestPayload)throws  IOException{
        return  ResponseEntity.ok(tenderService.updateTender(tenderUpdateRequestPayload));
    }
    @GetMapping(value = "/tenderStatusUpdate")
    public ResponseEntity<Map<String, Object>> tenderStatusUpdate(@RequestParam Long id,@RequestParam short status){
        return ResponseEntity.ok(tenderService.tenderStatusUpdate( id,status));
    }
    @GetMapping(value = "/getByTenderId")
    public Map<String, Object> getByTenderId(Long id) {
        return tenderService.getByTenderId(id);
    }

    @PostMapping(value = "/getAllTender")
    public ResponseEntity<Map<String, Object>> getAllTender(@RequestBody GetAllTender getAllTender) {
        Map<String, Object> allAdmin = tenderService.getAllTender(getAllTender);
        return ResponseEntity.ok(allAdmin);
    }
    @DeleteMapping("/TenderDeleteById{id}")
    public  Map<String,Object> deleteById(Long id){
        return tenderService.tenderDeleteById(id);
    }


//    @GetMapping(value = "/tenderSoftDelete")
//    public ResponseEntity<Map<String, Object>> tenderSoftDelete(@RequestParam Long id,@RequestParam short status){
//        return ResponseEntity.ok(tenderService.tenderSoftDelete( id,status));
//    }
    @PostMapping(value = "/getAllDeleteTender")
    public ResponseEntity<Map<String, Object>> getAllDeleteTender(@RequestBody GetAllDeleteTender getAllTender) {
        Map<String, Object> allAdmin = tenderService.getAllDeleteTender(getAllTender);
        return ResponseEntity.ok(allAdmin);
    }

    @PostMapping(value = "/addDebarredList")
    public ResponseEntity< Object> addDebarredList(@RequestBody DebarredRequest debarredRequest) throws IOException {
       return  ResponseEntity.ok(tenderService.addDebarredList(debarredRequest)) ;
    }

    @PostMapping(value = "/uploadCsvForDebarredList",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Object, Object> uploadCsvFileToDataBase(@ModelAttribute("docfile") MultipartFile docfile)throws IOException {
        HashMap<Object, Object> map = new HashMap<>();
        if (docfile != null) {
            return tenderService.uploadCsvForDebarredList(docfile);
        }
        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
        map.put(DataConstant.RESPONSE_BODY, null);
        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
        return map;
    }

    @PostMapping(value = "/getAllDebarredList")
    public ResponseEntity<Map<String, Object>> getAllDebarredList(@RequestBody GetAllDebarred getAllDebarred) {
        Map<String, Object> allAdmin = tenderService.getAllDebarredList(getAllDebarred);
        return ResponseEntity.ok(allAdmin);
    }
    @DeleteMapping("/DebarredListDeletById{id}")
    public  Map<String,Object> debarredDeleteById(Long id){
        return tenderService.debarredDeleteById(id);
    }

    @PostMapping(value = "/getAllDeleteDebarredList")
    public ResponseEntity<Map<String, Object>> getAllDeleteDebarredList(@RequestBody GetAllDeleteDebarred getAllDebarred) {
        Map<String, Object> allAdmin = tenderService.getAllDeleteDebarredList(getAllDebarred);
        return ResponseEntity.ok(allAdmin);
    }


//    @GetMapping(value = "/debarredSoftDelete")
//    public ResponseEntity<Map<String, Object>> debarredStatusUpdate(@RequestParam Long id,@RequestParam short status){
//        return ResponseEntity.ok(tenderService.debarredStatusUpdate( id,status));
//    }
    @PostMapping(value = "/addBlackList")
    public ResponseEntity< Object> addBlackList(@RequestBody BlackListRequest blackListRequest) throws IOException {
        return  ResponseEntity.ok(tenderService.addBlackList(blackListRequest)) ;
    }

    @PostMapping(value = "/uploadCsvForBlackList",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Object, Object> uploadCsvForBlackList(@ModelAttribute("docfile") MultipartFile docfile)throws IOException {
        HashMap<Object, Object> map = new HashMap<>();
        if (docfile != null) {
            return tenderService.uploadCsvForBlackList(docfile);
        }
        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
        map.put(DataConstant.RESPONSE_BODY, null);
        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
        return map;
    }

    @PostMapping(value = "/getAllBlackList")
    public ResponseEntity<Map<String, Object>> getAllBlackList(@RequestBody GetAllBlackList getAllBlackList) {
        Map<String, Object> allAdmin = tenderService.getAllBlackList(getAllBlackList);
        return ResponseEntity.ok(allAdmin);
    }

    @DeleteMapping("/blackListDeleteById{id}")
    public  Map<String,Object> blackListDeleteById(Long id){
        return tenderService.blackListDeleteById(id);
    }

    @PostMapping(value = "/getAllDeleteBlackList")
    public ResponseEntity<Map<String, Object>> getAllDeleteBlackList(@RequestBody GetAlldeleteBlackList getAllBlackList) {
        Map<String, Object> allAdmin = tenderService.getAllDeleteBlackList(getAllBlackList);
        return ResponseEntity.ok(allAdmin);
    }


//    @GetMapping(value = "/blackListSoftDelete")
//    public ResponseEntity<Map<String, Object>> blackListSoftDelete(@RequestParam Long id,@RequestParam short status){
//        return ResponseEntity.ok(tenderService.blackListSoftDelete( id,status));
//    }

    @PostMapping(value = "/addRevocationOrder")
    public ResponseEntity< Object> addRevocationOrder(@RequestBody RevocationRequest revocationRequest) throws IOException {
        return  ResponseEntity.ok(tenderService.addRevocationorder(revocationRequest)) ;
    }

    @PostMapping(value = "/uploadCsvForRevocationOrder",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<Object, Object> uploadCsvForRevocationOrder(@ModelAttribute("docfile") MultipartFile docfile)throws IOException {
        HashMap<Object, Object> map = new HashMap<>();
        if (docfile != null) {
            return tenderService.uploadCsvForRevocationOrder(docfile);
        }
        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
        map.put(DataConstant.RESPONSE_BODY, null);
        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
        return map;
    }

    @PostMapping(value = "/getAllRevocationOrder")
    public ResponseEntity<Map<String, Object>> getAllRevocationOrder(@RequestBody GetAllRevocation getAllRevocation) {
        Map<String, Object> allAdmin = tenderService.getAllRevocationOrder(getAllRevocation);
        return ResponseEntity.ok(allAdmin);
    }

    @DeleteMapping("/revocationOrderDeleteById{id}")
    public  Map<String,Object> revocationOrderDeleteById(Long id){
        return tenderService.revocationOrderDeleteById(id);
    }

    @PostMapping(value = "/getAllDeleteRevocationOrder")
    public ResponseEntity<Map<String, Object>> getAllDeleteRevocationOrder(@RequestBody GetAllDeleteRevocation getAllRevocation) {
        Map<String, Object> allAdmin = tenderService.getAllDeleteRevocationOrder(getAllRevocation);
        return ResponseEntity.ok(allAdmin);
    }

//    @GetMapping(value = "/revocationSoftDelete")
//    public ResponseEntity<Map<String, Object>> revocationSoftDelete(@RequestParam Long id,@RequestParam short status){
//        return ResponseEntity.ok(tenderService.revocationSoftDelete( id,status));
//    }
    @PostMapping(value = "/addAwardedTender")
    public ResponseEntity< Object> addAwardedTender(@Valid @RequestBody AwardedTenderRequest awardedTenderRequest, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return commonUtilService.requestValidation(bindingResult);
        }
        return  ResponseEntity.ok(tenderService.addAwardedTender(awardedTenderRequest)) ;
    }

    @PutMapping (value = "/updateAwardedTender")
    public ResponseEntity< Object> updateAwardedTender(  @RequestBody AwardedUpdateRequest awardedUpdateRequest)throws  IOException{
        return  ResponseEntity.ok(tenderService.updateAwardedTender(awardedUpdateRequest));
    }

    @PostMapping(value = "/getAllAwardedTender")
    public ResponseEntity<Map<String, Object>> getAllAwardedTender(@RequestBody GetAllAwardedTender getAllTender) {
        Map<String, Object> allAdmin = tenderService.getAllAwardedTender(getAllTender);
        return ResponseEntity.ok(allAdmin);
    }
    @DeleteMapping("/awrdedTenderDeleteById{id}")
    public  Map<String,Object> awrdedTenderDeleteById(Long id){
        return tenderService.awrdedTenderDeleteById(id);
    }
    @PostMapping(value = "/getAllDeleteAwardedTender")
    public ResponseEntity<Map<String, Object>> getAllDeleteAwardedTender(@RequestBody GetAllDeleteAwardedTender getAllTender) {
        Map<String, Object> allAdmin = tenderService.getAllDeleteAwardedTender(getAllTender);
        return ResponseEntity.ok(allAdmin);
    }

//    @GetMapping(value = "/awardedTenderSoftDelete")
//    public ResponseEntity<Map<String, Object>> awardedTenderSoftDelete(@RequestParam Long id,@RequestParam short status){
//        return ResponseEntity.ok(tenderService.awardedTenderSoftDelete( id,status));
//    }
}

