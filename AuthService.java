package com.janaushadhi.adminservice.externalservices;


import com.janaushadhi.adminservice.requestpayload.RegistrationRequestPayload;
import com.janaushadhi.adminservice.requestpayload.UpdateAdmin;
import com.janaushadhi.adminservice.responsepayload.GetAllPharmasict;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "JANAUSHADHI-AUTH-SERVICE", url = "10.192.92.45:7002/auth") // url = "151.106.39.5:7002/auth"  //NOSONAR
public interface AuthService {


    @PostMapping("/registerAdmin")
    public Map<String,Object> addAdmin(@RequestBody RegistrationRequestPayload registraionRequest);


    @DeleteMapping("/deleteUserByEmailId")
    public  Map<String,Object> deleteByEmailId( @RequestParam String email);
    
	@PostMapping("/updateAdminRole")
	public Map<String,Object> updateAdminRole(@RequestBody UpdateAdmin updateAdmin);


    @PostMapping( value = "/getAllPharmacistWithFilters")
    public Map<String,Object> getAllPharmacistWithFilters(@RequestBody GetAllPharmasict getAllPharmasict);
	
	@PostMapping("/getAllUsersByRoleId")
	public Map<String,Object> getAllUsersByRoleId(@RequestParam Integer roleId);
	
}
