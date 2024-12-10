package com.janaushadhi.adminservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.janaushadhi.adminservice.requestpayload.AdminAddKendraForHistory;
import com.janaushadhi.adminservice.requestpayload.AdminApplicationRequest;
import com.janaushadhi.adminservice.requestpayload.AdminUpdateKendraRequest;
import com.janaushadhi.adminservice.requestpayload.ApplicationFinalRejectPayload;
import com.janaushadhi.adminservice.service.AdminKendraService;

@RestController
@RequestMapping("/api/v1/admin/kendra")
public class AdminKendraController {
	@Autowired
	private AdminKendraService adminKendraService;
	
	@PostMapping("/addKendraDetailForAdmin")
	public Map<String,Object> addKendraDetailForAdmin(@RequestBody AdminAddKendraForHistory addKendraHistory){
		return adminKendraService.addKendraDetailForAdmin(addKendraHistory);
	}
	
	@PostMapping("/adminKendraUpdate")
	public Map<String,Object> adminKendraUpdate(@RequestBody AdminUpdateKendraRequest adminKendraUpdate){
		return adminKendraService.adminKendraUpdate(adminKendraUpdate);
	}
	
	@GetMapping("/getApplicationById")
	public Map<String,Object> getApplicationById(String applicationId){
		return adminKendraService.getApplicationById(applicationId);
	}
	
	@PostMapping(value = "/getAllKendraDetailsByAdminId")
	public  ResponseEntity<Map<String, Object>> getAllKendraDetailsAdmin(@RequestBody AdminApplicationRequest adminRequest) {
		Map<String, Object> mapRes =  adminKendraService.getAllKendraDetailsByAdminId(adminRequest);
		return ResponseEntity.ok(mapRes);
	}
	
	@PostMapping("/getAdminStateByAdminId")
	public Map<String,Object> getAdminStateByAdminId(Long adminId){
		return adminKendraService.getAdminStateByAdminId(adminId);
	}
	
	@PostMapping(value = "/getAllKendraDetailsBySuperAdmin")
	public  ResponseEntity<Map<String, Object>> getAllKendraDetailsBySuperAdmin(@RequestBody AdminApplicationRequest adminRequest) {
		Map<String, Object> mapRes =  adminKendraService.getAllKendraDetailsBySuperAdmin(adminRequest);
		return ResponseEntity.ok(mapRes);
	}
	
	@PostMapping(value = "/getAdminDashboardAppCount")
	public  ResponseEntity<Map<String, Object>> getAdminDashboardAppCount(@RequestBody AdminApplicationRequest adminRequest) {
		Map<String, Object> mapRes =  adminKendraService.getAdminDashboardAppCount(adminRequest);
		return ResponseEntity.ok(mapRes);
	}
	
	@PostMapping("/removeAppFromRejectListAfterAgainDrugApply")
	public  Map<String, Object> removeDrugFromRejectList(@RequestParam String applicationId) {
		return  adminKendraService.removeDrugFromRejectList(applicationId);
	}
	
	@PostMapping(value = "/exportAllKendraDetailsByAdmin")
	public  ResponseEntity<Map<String, Object>> exportAllKendraDetailsByAdmin(@RequestBody AdminApplicationRequest adminRequest) {
		Map<String, Object> mapRes =  adminKendraService.exportAllKendraDetailsByAdmin(adminRequest);
		return ResponseEntity.ok(mapRes);
	}
	@PostMapping("/initialLatterExpiredRequestExtension")
	public  Map<String, Object> initialLatterExpiredRequestExtension(@RequestParam String applicationId) {
		return  adminKendraService.initialLatterExpiredRequestExtension(applicationId);
	
	}
	
	@PostMapping(value = "/getAllKendraDetailsByCategoryAdmin")
	public  ResponseEntity<Map<String, Object>> getAllKendraDetailsByCategoryAdmin(@RequestBody AdminApplicationRequest adminRequest) {
		Map<String, Object> mapRes =  adminKendraService.getAllKendraDetailsByCategoryAdmin(adminRequest);
		return ResponseEntity.ok(mapRes);
	}
	
	@PostMapping(value = "/applicationRejectByL4AndSuperAdmin")
	public  ResponseEntity<Map<String, Object>> applicationRejectByL4AndSuperAdmin(@RequestBody ApplicationFinalRejectPayload finalRejectPayload) {
		Map<String, Object> mapRes =  adminKendraService.applicationRejectByL4AndSuperAdmin(finalRejectPayload);
		return ResponseEntity.ok(mapRes);
	}
//	@PostMapping(value = "/getCategoryAdminDashboardAppCount")
//	public  ResponseEntity<Map<String, Object>> getCategoryAdminDashboardAppCount(@RequestBody AdminApplicationRequest adminRequest) {
//		Map<String, Object> mapRes =  adminKendraService.getCategoryAdminDashboardAppCount(adminRequest);
//		return ResponseEntity.ok(mapRes);
//	}
}
