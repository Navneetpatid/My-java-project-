package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.externalservices.AuthService;
import com.janaushadhi.adminservice.requestpayload.AdminRequestPayload;
import com.janaushadhi.adminservice.requestpayload.ChangeProfileRequest;
import com.janaushadhi.adminservice.requestpayload.GetAllAdmin;
import com.janaushadhi.adminservice.requestpayload.RoleRequestPayload;
import com.janaushadhi.adminservice.responsepayload.GetAllPharmasict;
import com.janaushadhi.adminservice.service.AdminService;
import com.janaushadhi.adminservice.util.CommonUtilService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/admin")

public class AdminController {
    @Autowired
    private CommonUtilService commonUtilService;


    @Autowired
    private AuthService authService;
    @Autowired
    private AdminService adminService;
    @PostMapping(value = "/addAdmin")
    public ResponseEntity< Object> addAdmin(@Valid @RequestBody AdminRequestPayload adminRequestPayload, BindingResult bindingResult) throws IOException, ConstraintViolationException {
        if (bindingResult.hasErrors()) {
            return commonUtilService.requestValidation(bindingResult);
        }
        return  ResponseEntity.ok(adminService.addAdmin(adminRequestPayload)) ;

    }

    @PostMapping(value = "/addRole")
    public ResponseEntity< Object> addRole(RoleRequestPayload roleRequestPayload,BindingResult bindingResult) throws IOException{
        if (bindingResult.hasErrors()) {
            return commonUtilService.requestValidation(bindingResult);
        }
        return  ResponseEntity.ok(adminService.addRole(roleRequestPayload)) ;
    }

    @GetMapping("/getAllRoles")
    public Map<String,Object> getAllRoles(){
        return adminService.getAllRoles();
    }
    @GetMapping(value = "/getAdminById")
    public Map<String, Object> getAdminById(@RequestParam Long id) {
        return adminService.getAdminById(id);
    }
    
    @GetMapping("/getAdminByEmail")
    public Map<String, Object> getAdminByEmail(@RequestParam String email) {
        return adminService.getAdminByEmail(email);
    }

    @DeleteMapping("/deleteAdminById{id}")
    public  Map<String,Object> deleteById(Long id){
       return adminService.deleteById(id);

    }


    @PostMapping(value = "/getAllAdmin")
    public ResponseEntity<Map<String,Object>> getAllAdmin(@RequestBody GetAllAdmin getAllAdminPayload) {
        Map<String, Object> allAdmin = adminService.getAllAdmin(getAllAdminPayload);
        return ResponseEntity.ok(allAdmin);
    }

    @GetMapping(value = "/adminStatusActiveDeactive")
    public ResponseEntity<Map<String,Object>> adminStatusUpdate(@RequestParam Long id,@RequestParam short status){
     return ResponseEntity.ok(adminService.adminStatusUpdate( id,status));
    }

    @PostMapping(value = "/getAllPharmacist")
    public Map<String,Object> getAllPharmacist( @RequestBody GetAllPharmasict getAllPharmasict){
        return authService.getAllPharmacistWithFilters(getAllPharmasict);
    }



    @PutMapping(value = "/adminChangeProfile")
    public ResponseEntity<Map<String,Object>> updateAdminProfile(@RequestBody ChangeProfileRequest changeProfileRequest){
        return ResponseEntity.ok(adminService.updateAdminProfile(changeProfileRequest));
    }


}
