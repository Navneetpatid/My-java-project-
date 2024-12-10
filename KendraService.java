package com.janaushadhi.adminservice.externalservices;

import java.util.Map;


import com.janaushadhi.adminservice.responsepayload.GetAllDistributor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.janaushadhi.adminservice.requestpayload.AdminStateRequestPayload;
import com.janaushadhi.adminservice.requestpayload.AdminUpdateKendraRequest;
import com.janaushadhi.adminservice.requestpayload.PaidUserRequest;



@FeignClient(name = "JANAUSHADHI-KENDRA-SERVICE", url = "10.192.92.45:7004/api/kendra") // url = "151.106.39.5:7002/auth" //NOSONAR

public interface KendraService {


	@PostMapping("/updateKedraByAdmin")
	public Map<String, Object> updateKedraByAdmin( @RequestBody AdminUpdateKendraRequest adminKendraUpdate);
	
	@PostMapping("/getStateOfIndiaByStateIds")
	public Map<String, Object> getStateOfIndiaByStateIds(@RequestBody AdminStateRequestPayload adminStateIds);

	@GetMapping("/getDistrictOfIndiaByDistrictId")
	public Map<String, Object> getDistrictOfIndiaByDistrictId(@RequestParam Long districtId);
	@PostMapping("/getAllPaidUsers")
	public Map<String, Object> getAllPaidUsers(@RequestBody PaidUserRequest paidRequest );

	@GetMapping("/getKendraDetailsByApplicationId")
	public Map<String, Object> getKendraDetailsByApplicationId(@RequestParam String applicationId);
	
	@GetMapping(value = "/getAllKendraDetails")
	public Map<Object, Object> getAllKendraDetails(@RequestParam(required = false) Integer pageIndex,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Short status);

	@PostMapping("/kedraFinalRejectByL4Admin")//internally used in admin service
	public Map<String, Object> kedraFinalRejectByL4Admin(@RequestBody AdminUpdateKendraRequest adminKendraUpdate);


	}

