package com.janaushadhi.adminservice.service;

import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.AdminAddKendraForHistory;
import com.janaushadhi.adminservice.requestpayload.AdminApplicationRequest;
import com.janaushadhi.adminservice.requestpayload.AdminUpdateKendraRequest;
import com.janaushadhi.adminservice.requestpayload.ApplicationFinalRejectPayload;

public interface AdminKendraService {

	Map<String, Object> adminKendraUpdate(AdminUpdateKendraRequest adminKendraUpdate);

	Map<String, Object> getApplicationById(String applicationId);

	Map<String, Object> addKendraDetailForAdmin(AdminAddKendraForHistory addKendraHistory);

	Map<String, Object> getAllKendraDetailsByAdminId(AdminApplicationRequest adminRequest);

	Map<String, Object> getAdminStateByAdminId(Long adminId);

	Map<String, Object> getAllKendraDetailsBySuperAdmin(AdminApplicationRequest adminRequest);

	Map<String, Object> getAdminDashboardAppCount(AdminApplicationRequest adminRequest);//(Long adminId, Long roleId);

	Map<String, Object> removeDrugFromRejectList(String applicationId);

	Map<String, Object> exportAllKendraDetailsByAdmin(AdminApplicationRequest adminRequest);

	Map<String, Object> initialLatterExpiredRequestExtension(String applicationId);

	Map<String, Object> getAllKendraDetailsByCategoryAdmin(AdminApplicationRequest adminRequest);

	Map<String, Object> getCategoryAdminDashboardAppCount(AdminApplicationRequest adminRequest);

	Map<String, Object> applicationRejectByL4AndSuperAdmin(ApplicationFinalRejectPayload finalRejectPayload);

}
