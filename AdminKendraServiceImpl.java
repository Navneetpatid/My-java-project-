package com.janaushadhi.adminservice.serviceimpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.janaushadhi.adminservice.requestpayload.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janaushadhi.adminservice.entity.Admin;
import com.janaushadhi.adminservice.entity.AdminKendraApplications;
import com.janaushadhi.adminservice.entity.AdminStateMapping;
import com.janaushadhi.adminservice.externalservices.AuthService;
import com.janaushadhi.adminservice.externalservices.KendraService;
import com.janaushadhi.adminservice.repository.AdminKendraRepository;
import com.janaushadhi.adminservice.repository.AdminRepository;
import com.janaushadhi.adminservice.responsepayload.AdminDashBoardAppCountResponse;
import com.janaushadhi.adminservice.responsepayload.AdminKendraAppResponsePage;
import com.janaushadhi.adminservice.responsepayload.AdminKendraApplicationResponse;
import com.janaushadhi.adminservice.responsepayload.AdminStateResponsePayload;
import com.janaushadhi.adminservice.responsepayload.ApplicationStatusResponse;
import com.janaushadhi.adminservice.responsepayload.DistrictOfIndiaResponse;
import com.janaushadhi.adminservice.responsepayload.ExportKendraAppResponse;
import com.janaushadhi.adminservice.responsepayload.ExportKendraAppResponsePage;
import com.janaushadhi.adminservice.responsepayload.KendraDetailsResponse;
import com.janaushadhi.adminservice.responsepayload.KendraDetailsResponsePage;
import com.janaushadhi.adminservice.responsepayload.KendraDetailsResponsePayload;
import com.janaushadhi.adminservice.responsepayload.UserResponsePayload;
import com.janaushadhi.adminservice.service.AdminKendraService;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminKendraServiceImpl implements AdminKendraService {
	@Autowired
	private AdminKendraRepository adminKendraRepo;

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private KendraService kendraService;
		
	@Autowired
	private AuthService authService;

	@Override
	public Map<String, Object> addKendraDetailForAdmin(AdminAddKendraForHistory addKendraHistory) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (addKendraHistory.getApplicationId() != null) {
			List<AdminKendraApplications> isExist=adminKendraRepo.findAllByApplicationId(addKendraHistory.getApplicationId());
			if(isExist!=null) {
				for(AdminKendraApplications app:isExist) {
				adminKendraRepo.delete(app);
				}
			}
			AdminKendraApplications addApplication = new AdminKendraApplications();
			BeanUtils.copyProperties(addKendraHistory, addApplication);
			addApplication.setLatestApplication(true);
			addApplication.setCreatedDate(new Date());
			log.info("Created Date Of Application : ", addApplication.getCreatedDate());
			adminKendraRepo.save(addApplication);
			map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
			map.put(DataConstant.MESSAGE, DataConstant.UPDATED_SUCCESSFULLY);
			map.put(DataConstant.DATA, addKendraHistory);
			log.info(" Application updated successfully : ",DataConstant.UPDATED_SUCCESSFULLY);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
		map.put(DataConstant.MESSAGE, DataConstant.FAILED_TO_ADD_KENDRA);
		map.put(DataConstant.DATA, addKendraHistory);
		log.info(" faild to add kendra : ",DataConstant.FAILED_TO_ADD_KENDRA);

		return map;
	}

	@Override
	public Map<String, Object> adminKendraUpdate(AdminUpdateKendraRequest adminKendraUpdate) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (adminKendraUpdate.getApplicationId() != null) {
			if (adminKendraUpdate.getAdminId() != null) {
				Admin admin = adminRepo.findById(adminKendraUpdate.getAdminId()).orElse(null);
//				if (admin != null) {
//					Optional<AdminKendraApplications> kendraDetails = adminKendraRepo
//							.findByApplicationIdAndAdminIdAndApplicationStatus(adminKendraUpdate.getApplicationId(),
//									adminKendraUpdate.getAdminId(), adminKendraUpdate.getApplicationStatus());
				if (admin != null) {
					//old code
//					Optional<AdminKendraApplications> kendraDetails = adminKendraRepo
//							.findByApplicationIdAndAdminId(adminKendraUpdate.getApplicationId(),
//									adminKendraUpdate.getAdminId());
					//old code end
					//newcode for super admin approval & rejection
					AdminKendraApplications kendraDetails = adminKendraRepo
							.findByApplicationIdAndIsLatestApplication(adminKendraUpdate.getApplicationId(),
									true);
					if (kendraDetails==null) {//old kendraDetail!=null
						///new code end
						AdminKendraApplications kendraUpdate = new AdminKendraApplications();
						BeanUtils.copyProperties(kendraDetails, kendraUpdate);
						kendraUpdate.setAdminId(adminKendraUpdate.getAdminId());
						kendraUpdate.setApplicationStatus(adminKendraUpdate.getApplicationStatus());
						kendraUpdate.setRoleId(adminKendraUpdate.getRoleId());
						if (adminKendraUpdate != null && adminKendraUpdate.getRejectReason() != null
								&& !adminKendraUpdate.getRejectReason().trim().isEmpty()) {
							kendraUpdate.setRejectedReason(adminKendraUpdate.getRejectReason());
						}
						if (admin.getIsDocumentVerification() == 1) {
							kendraUpdate.setDocumentVerification(adminKendraUpdate.isDocumentVerification());
						}
						if (admin.getIsIntitailApprovalWithDSC() == 1) {
							kendraUpdate.setIntitailApprovalWithDSC(adminKendraUpdate.isIntitailApprovalWithDSC());
						}
						if (admin.getIsDrugLicenceVerification() == 1) {
							kendraUpdate.setDrugLicenceVerification(adminKendraUpdate.isDrugLicenceVerification());
						}
						if (admin.getIsAggrementVerification() == 1) {
							kendraUpdate.setAggrementVerification(adminKendraUpdate.isAggrementVerification());
						}
						if (admin.getIsFinalApprovalWithDSC() == 1) {
							kendraUpdate.setFinalApprovalWithDSC(adminKendraUpdate.isFinalApprovalWithDSC());
							if(adminKendraUpdate.isFinalApprovalWithDSC()){
							kendraUpdate.setApplicationFinalApproval(1);
							kendraUpdate.setFinalKendraCode(adminKendraUpdate.getFinalKendraCode());
							}
							
						} else {
							kendraUpdate.setApplicationFinalApproval(0);
						}
						kendraUpdate.setUpdated(new Date());
						log.info("Updated Date Of Application : ", kendraUpdate.getUpdated());
						// update user kendra detail status and approval
						AdminUpdateKendraRequest kendraUpdateRequest = new AdminUpdateKendraRequest();
						BeanUtils.copyProperties(kendraUpdate, kendraUpdateRequest);
						kendraUpdateRequest.setApplicationStatus(kendraUpdate.getApplicationStatus());
						try {
							//if (kendraUpdateRequest.getApplicationStatus() != 2) {
								Map<String, Object> adminMap = kendraService.updateKedraByAdmin(kendraUpdateRequest);
								if (adminMap.containsValue("200")) {

								} else {
									map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
									map.put(DataConstant.MESSAGE, DataConstant.FAILED_TO_UPDATE_KENDRA);
									map.put(DataConstant.DATA, null);
									log.info("faild to update kendra : ",DataConstant.FAILED_TO_UPDATE_KENDRA);

									return map;
							//	}
							}
						} catch (Exception ex) {
							log.info(DataConstant.SERVER_MESSAGE, ex.getMessage());
							map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
							map.put(DataConstant.MESSAGE, DataConstant.FAILED_TO_UPDATE_KENDRA);
							map.put(DataConstant.DATA, null);
							return map;
						}

						adminKendraRepo.save(kendraUpdate);
						map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
						map.put(DataConstant.MESSAGE, DataConstant.UPDATED_SUCCESSFULLY);
						map.put(DataConstant.DATA, adminKendraUpdate);
						log.info("update kendra  successfully: ",DataConstant.UPDATED_SUCCESSFULLY);

						return map;
					}

					AdminKendraApplications kendraAdd = null;
					kendraAdd = adminKendraRepo
							.findByApplicationIdAndApplicationStatus(adminKendraUpdate.getApplicationId(), 0);
					if (kendraAdd != null) {
						kendraAdd.setUpdated(new Date());
						log.info("Created Date Of Application : ", kendraAdd.getUpdated());
					} else {
						List<AdminKendraApplications> applicationList = adminKendraRepo
								.findAllByApplicationId(adminKendraUpdate.getApplicationId());
						for (AdminKendraApplications application : applicationList) {
							application.setLatestApplication(false);
							adminKendraRepo.save(application);
						}
						kendraAdd = new AdminKendraApplications();
						kendraAdd.setCreatedDate(new Date());
//						kendraAdd.setLatitude(applicationList.get(0).getLatitude());
//						kendraAdd.setLongitude(applicationList.get(0).getLongitude());
						log.info("Created Date Of Application : ", kendraAdd.getCreatedDate());
					}

					// BeanUtils.copyProperties(adminKendraUpdate, kendraAdd);
					kendraAdd.setApplicationId(adminKendraUpdate.getApplicationId());
					kendraAdd.setKendraId(adminKendraUpdate.getKendraId());
					kendraAdd.setNameOfApplicant(adminKendraUpdate.getNameOfApplicant());
					kendraAdd.setNameOfOrganization(adminKendraUpdate.getNameOfOrganization());
					kendraAdd.setAdminId(adminKendraUpdate.getAdminId());
					kendraAdd.setApplicationStatus(adminKendraUpdate.getApplicationStatus());
					kendraAdd.setRoleId(adminKendraUpdate.getRoleId());
					kendraAdd.setCategory(adminKendraUpdate.getCategory());
					kendraAdd.setKendraStateId(adminKendraUpdate.getKendraStateId());
					kendraAdd.setKendraDistrictId(adminKendraUpdate.getKendraDistrictId());
					kendraAdd.setKendraBlockId(adminKendraUpdate.getKendraBlockId());
					kendraAdd.setLatestApplication(true);
					kendraAdd.setSubCategory(adminKendraUpdate.getSubCategory());
					if (adminKendraUpdate != null && adminKendraUpdate.getRejectReason() != null
							&& !adminKendraUpdate.getRejectReason().trim().isEmpty()) {
						kendraAdd.setRejectedReason(adminKendraUpdate.getRejectReason());
					}
					if (admin.getIsDocumentVerification() == 1) {
						kendraAdd.setDocumentVerification(adminKendraUpdate.isDocumentVerification());
					}
					if (admin.getIsIntitailApprovalWithDSC() == 1) {
						kendraAdd.setIntitailApprovalWithDSC(adminKendraUpdate.isIntitailApprovalWithDSC());
					}
					if (admin.getIsDrugLicenceVerification() == 1) {
						kendraAdd.setDrugLicenceVerification(adminKendraUpdate.isDrugLicenceVerification());
					}
					if (admin.getIsAggrementVerification() == 1) {
						kendraAdd.setAggrementVerification(adminKendraUpdate.isAggrementVerification());
					}
					if (admin.getIsFinalApprovalWithDSC() == 1) {
						kendraAdd.setFinalApprovalWithDSC(adminKendraUpdate.isFinalApprovalWithDSC());
						kendraAdd.setApplicationFinalApproval(1);
						kendraAdd.setFinalKendraCode(adminKendraUpdate.getFinalKendraCode());
					} else {
						kendraAdd.setApplicationFinalApproval(0);
					}

					// update user kendra detail status and approval
					AdminUpdateKendraRequest kendraUpdateRequest = new AdminUpdateKendraRequest();
					BeanUtils.copyProperties(kendraAdd, kendraUpdateRequest);
					kendraUpdateRequest.setApplicationStatus(kendraAdd.getApplicationStatus());
					try {
					//	if (kendraUpdateRequest.getApplicationStatus() != 2) {
							kendraUpdateRequest.setNameOfApplicant(null);
							kendraUpdateRequest.setNameOfOrganization(null);
							kendraUpdateRequest.setRejectReason(kendraAdd.getRejectedReason());
							Map<String, Object> adminMap = kendraService.updateKedraByAdmin(kendraUpdateRequest);
							if (adminMap.containsValue("200")) {

							} else {
//					map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
//					map.put(DataConstant.MESSAGE, DataConstant.FAILED_TO_UPDATE_KENDRA);
//					map.put(DataConstant.DATA, null);
								return adminMap;
							}

					//	}
					} catch (Exception ex) {
						log.info(DataConstant.SERVER_MESSAGE, ex.getMessage());
						map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
						map.put(DataConstant.MESSAGE, DataConstant.FAILED_TO_UPDATE_KENDRA);
						map.put(DataConstant.DATA, null);
						return map;

					}
					adminKendraRepo.save(kendraAdd);
					map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
					map.put(DataConstant.MESSAGE, DataConstant.UPDATED_SUCCESSFULLY);
					map.put(DataConstant.DATA, adminKendraUpdate);
					log.info("update kendra  successfully: ",DataConstant.UPDATED_SUCCESSFULLY);
					return map;
//				map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
//				map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
//				map.put(DataConstant.DATA, null);
//				return map;
				}
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.PLEASE_INPUT_APPLICATION_ID);
			map.put(DataConstant.DATA, null);
			log.info("please input vaild application id : ",DataConstant.PLEASE_INPUT_APPLICATION_ID);
			return map;
		}
		
		return map;
	}

	@Override
	public Map<String, Object> getApplicationById(String applicationId) {
		Map<String, Object> map = new HashMap<String, Object>();
		ApplicationStatusResponse appStatus = new ApplicationStatusResponse();
		appStatus.setApplicationIsVerified(0);
		appStatus.setIntialLatterIsGenerated(0);
		appStatus.setFilanApproval(0);
		appStatus.setDrugLicense(0);
		appStatus.setDrugAgreement(0);
		
		//new code 
		Map<String, Object> kendraMapData=kendraService.getKendraDetailsByApplicationId(applicationId);
		if(kendraMapData.containsValue("200")) {
			ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
	        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        KendraDetailsResponse kendraResponse = mapper.convertValue(kendraMapData.get("data"), KendraDetailsResponse.class);
	        System.out.println("map size" + kendraResponse);
	        
			//KendraDetailsResponse kendraResponse=(KendraDetailsResponse) kendraMapData.get("data");
			if(kendraResponse!=null) {
				////
				if(kendraResponse.isDocumentVerification() && kendraResponse.isIntitailApprovalWithDSC() && kendraResponse.isDrugLicenceVerification()
						&& kendraResponse.isAggrementVerification() && kendraResponse.isFinalApprovalWithDSC())  {
					appStatus.setApplicationIsVerified(1);
					appStatus.setIntialLatterIsGenerated(1);
					appStatus.setDrugLicense(1);
					appStatus.setDrugAgreement(1);
					appStatus.setFilanApproval(1);
					
					appStatus.setAppVerifyDate(kendraResponse.getDocumentVerificationDate());
					appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
					appStatus.setFilanApprovalDate(kendraResponse.getFinalApprovalDate());
					appStatus.setDrugLicenseDate(kendraResponse.getDrugLicenceVerificationDate());
					appStatus.setDrugAgreementDate(kendraResponse.getAggrementVerificationDate());
					}
				else if(kendraResponse.isDocumentVerification() && kendraResponse.isIntitailApprovalWithDSC() && kendraResponse.isDrugLicenceVerification()
						&& kendraResponse.isAggrementVerification())  {
					appStatus.setApplicationIsVerified(1);
					appStatus.setIntialLatterIsGenerated(1);
					appStatus.setDrugLicense(1);
					appStatus.setDrugAgreement(1);
					if(kendraResponse.getApplicationStatus()!=1){
						appStatus.setFilanApproval(kendraResponse.getApplicationStatus());
					}else{
					appStatus.setFilanApproval(0);
					}
					appStatus.setRejectReason(kendraResponse.getRejectReason());
					appStatus.setAppVerifyDate(kendraResponse.getDocumentVerificationDate());
					appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
					appStatus.setDrugLicenseDate(kendraResponse.getDrugLicenceVerificationDate());
					appStatus.setDrugAgreementDate(kendraResponse.getAggrementVerificationDate());
					appStatus.setFilanApprovalDate(kendraResponse.getUpdationDate());
					}
				else if(kendraResponse.isDocumentVerification() && kendraResponse.isIntitailApprovalWithDSC() && kendraResponse.isDrugLicenceVerification())  {
					appStatus.setApplicationIsVerified(1);
					appStatus.setIntialLatterIsGenerated(1);
					appStatus.setDrugLicense(1);
					if(kendraResponse.getApplicationStatus()!=1){
					appStatus.setDrugAgreement(kendraResponse.getApplicationStatus());
					}else{
						appStatus.setDrugAgreement(0);
					}
					appStatus.setFilanApproval(0);
					appStatus.setRejectReason(kendraResponse.getRejectReason());
					appStatus.setAppVerifyDate(kendraResponse.getDocumentVerificationDate());
					appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
					appStatus.setDrugLicenseDate(kendraResponse.getUpdationDate());
					appStatus.setDrugAgreementDate(kendraResponse.getUpdationDate());
					//appStatus.setDrugAgreementDate(kendraResponse.getAggrementVerificationDate());
					//appStatus.setFilanApprovalDate(kendraResponse.getUpdationDate());
					}
				else if(kendraResponse.isDocumentVerification() && kendraResponse.isIntitailApprovalWithDSC()){
					appStatus.setApplicationIsVerified(1);
					appStatus.setIntialLatterIsGenerated(1);
					if(kendraResponse.getApplicationStatus()!=1){
					appStatus.setDrugLicense(kendraResponse.getApplicationStatus());
					}else{
						appStatus.setDrugLicense(0);
					}
					appStatus.setDrugAgreement(0);
					appStatus.setFilanApproval(0);
					appStatus.setRejectReason(kendraResponse.getRejectReason());
					appStatus.setAppVerifyDate(kendraResponse.getDocumentVerificationDate());
					appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
					appStatus.setDrugLicenseDate(kendraResponse.getUpdationDate());
					appStatus.setDrugAgreementDate(kendraResponse.getUpdationDate());
					//appStatus.setFilanApprovalDate(kendraResponse.getUpdationDate());
					}
				else if(kendraResponse.isDocumentVerification() ){
					appStatus.setApplicationIsVerified(1);
					if(kendraResponse.getApplicationStatus()!=1){
						appStatus.setIntialLatterIsGenerated(kendraResponse.getApplicationStatus());
						appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
					}else{
					appStatus.setIntialLatterIsGenerated(0);
					}
					appStatus.setDrugLicense(0);
					appStatus.setDrugAgreement(0);
					appStatus.setFilanApproval(0);
					appStatus.setRejectReason(kendraResponse.getRejectReason());
					appStatus.setAppVerifyDate(kendraResponse.getDocumentVerificationDate());
					//appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
					//appStatus.setFilanApprovalDate(kendraResponse.getUpdationDate());
					}
				else {
					appStatus.setApplicationIsVerified(kendraResponse.getApplicationStatus());
					appStatus.setIntialLatterIsGenerated(0);
					appStatus.setDrugLicense(0);
					appStatus.setDrugAgreement(0);
					appStatus.setFilanApproval(0);
					appStatus.setRejectReason(kendraResponse.getRejectReason());
					appStatus.setAppVerifyDate(kendraResponse.getUpdationDate());
					//appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
					//appStatus.setFilanApprovalDate(kendraResponse.getUpdationDate());
					}
				////
				
				
//			if(kendraResponse.isDocumentVerification()){
//			appStatus.setApplicationIsVerified(1);
//			appStatus.setAppVerifyDate(kendraResponse.getDocumentVerificationDate());
//			if(kendraResponse.isIntitailApprovalWithDSC()) {
//				appStatus.setIntialLatterIsGenerated(1);
//				appStatus.setIntialLatterDate(kendraResponse.getIntitailApprovalDate());
//				if(kendraResponse.isDrugLicenceVerification()) {
//					appStatus.setDrugLicense(1);
//					if(kendraResponse.isAggrementVerification()) {
//						appStatus.setDrugAgreement(1);
//						if(kendraResponse.isFinalApprovalWithDSC()) {
//							appStatus.setFilanApproval(1);
//							appStatus.setFilanApprovalDate(kendraResponse.getFinalApprovalDate());;
//						}else if(kendraResponse.getApplicationStatus()==2){
//							appStatus.setFilanApproval(2);
//							appStatus.setFilanApprovalDate(kendraResponse.getUpdationDate());
//						}
//					}else if(kendraResponse.getApplicationStatus()==2) {
//						appStatus.setDrugAgreement(1);
//					}
//				}
//				else if(kendraResponse.getApplicationStatus()==2){
//					appStatus.setDrugLicense(2);
//				}
//				}else {
//					if(kendraResponse.getApplicationStatus()==2) {
//					appStatus.setIntialLatterIsGenerated(2);	
//					appStatus.setIntialLatterDate(kendraResponse.getUpdationDate());
//					}
//					
//				}
//			}
//			else {
//				appStatus.setApplicationIsVerified(kendraResponse.getApplicationStatus());
//				appStatus.setAppVerifyDate(kendraResponse.getCreationDate());
//				}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
			map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
			map.put(DataConstant.DATA, appStatus);
				log.info("record found  successfully in kendra Application: ",DataConstant.RECORD_FOUND_MESSAGE);

				return map;
		}
	}
		//new code ^



		map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
		map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
		map.put(DataConstant.DATA, appStatus);
		log.info("record found  successfully in kendra Application: ",DataConstant.RECORD_FOUND_MESSAGE);

		return map;
	}

	@Override
	public Map<String, Object> getAllKendraDetailsByAdminId(AdminApplicationRequest adminRequest) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (adminRequest.getPageNo() == null && adminRequest.getPageSize() == null) {
			 log.info("In getAllKendraDetailsByAdminId",DataConstant.INVALID_REQUEST);
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.INVALID_REQUEST);
			map.put(DataConstant.DATA, null);
			return map;
		}
		if (adminRequest.getAdminId() != null && adminRequest.getAdminId() != 0) {
			Optional<Admin> isAdmin = adminRepo.findById(adminRequest.getAdminId());
			if (isAdmin.isPresent()) {
				Admin admin = isAdmin.get();
				if (admin.getIsView() == 1) {
					AdminKendraAppResponsePage adminKendraAppResponsePage = new AdminKendraAppResponsePage();
					List<AdminKendraApplications> kendraApplicationList = new ArrayList<AdminKendraApplications>();
					List<Long> stateIdList = new ArrayList<>();
					List<Long> districtIdList = new ArrayList<>();
					for (AdminStateMapping adminState : admin.getAdminStateMapping()) {
						stateIdList.add(adminState.getStateId());
						districtIdList.addAll(adminState.getDistrictId());
					}
					Integer[] status = { 2, 3 };
				//	Integer superAdminRoleId=6;
					List<Long> requestStateIdList = new ArrayList<>();
					List<Long> requestDistrictIdList = new ArrayList<>();
					if (adminRequest.getAdminStateMapping() != null) {
						for (AdminStateResponsePayload adminState : adminRequest.getAdminStateMapping()) {
							requestStateIdList.add(adminState.getStateId());
							requestDistrictIdList.addAll(adminState.getDistrictId());
						}
					}
					if (admin.getIsDocumentVerification() == 1 && admin.getIsIntitailApprovalWithDSC() == 1
							&& admin.getIsDrugLicenceVerification() == 1 && admin.getIsAggrementVerification() == 1
							&& admin.getIsFinalApprovalWithDSC() == 1) {

						if (adminRequest.getApplicationStatus() != null && adminRequest.getApplicationStatus() == 0) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() < 4) { // Status 1 = Approved list, Status 2 =
																				// Reject List
					//		Integer superAdminRoleId =6;
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									//08-05-2024 superadmin approvals add in sub admin list
//								List<AdminKendraApplications>	superAdminList = adminKendraRepo
//											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), requestStateIdList,
//													requestDistrictIdList, superAdminRoleId,
//													adminRequest.getApplicationStatus());
//								kendraApplicationList.addAll(superAdminList);
								} else {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList =  adminKendraRepo
//											.findByCategoryAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(),superAdminRoleId ,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList =adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													requestStateIdList, requestDistrictIdList,
//													superAdminRoleId, adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
									
								} else {
									kendraApplicationList = adminKendraRepo
											.findByAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
									
//									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() == 4) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndAdminIdOrderByCreatedDate(adminRequest.getCategory(),
													adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
//									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
//											.findByAdminIdOrderByCreatedDate(adminRequest.getAdminId());
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
										//	.findByAdminIdAndIsFinalApprovalWithDSCOrderByCreatedDate(adminRequest.getAdminId(),true);
											
											.findByAdminIdAndIsLatestApplication(adminRequest.getAdminId(),true);
									
											kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									
//									List<AdminKendraApplications>	superAdminList =adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													requestStateIdList, requestDistrictIdList,
//													superAdminRoleId, adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
									
								}
							}

						}
					} else if (admin.getIsDocumentVerification() == 1 && admin.getIsIntitailApprovalWithDSC() == 1
							&& admin.getIsDrugLicenceVerification() == 1 && admin.getIsAggrementVerification() == 1) {

						if (adminRequest.getApplicationStatus() != null && adminRequest.getApplicationStatus() == 0) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() < 4) { // Status 1 = Approved list, Status 2 =
																				// Reject List
							//Integer superAdminRoleId=6;
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList =adminKendraRepo
//											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), requestStateIdList,
//													requestDistrictIdList, superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								} else {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
										
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList =adminKendraRepo.findByCategoryAndRoleIdAndApplicationStatusOrderByCreatedDate(
//											adminRequest.getCategory(), superAdminRoleId,
//											adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
										
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													requestStateIdList, requestDistrictIdList,
//													superAdminRoleId, adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);

								} else {
									kendraApplicationList = adminKendraRepo
											.findByAdminIdAndApplicationStatusOrderByCreatedDateDesc(
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
									
									 Set<String> seenNames = new HashSet<>();
								        kendraApplicationList = kendraApplicationList.stream()
								                .filter(app -> seenNames.add(app.getApplicationId()))
								                .collect(Collectors.toList());

									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() == 4) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);

									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);

									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndAdminIdOrderByCreatedDate(adminRequest.getCategory(),
													adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									
									
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);

									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsAggrementVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);

									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByAdminIdOrderByCreatedDateDesc(adminRequest.getAdminId());
									
									 Set<String> seenNames = new HashSet<>();
									 kendraApplicationApproveRejectList = kendraApplicationApproveRejectList.stream()
								                .filter(app -> seenNames.add(app.getApplicationId()))
								                .collect(Collectors.toList());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									
//									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							}

						}
					} else if (admin.getIsDocumentVerification() == 1 && admin.getIsIntitailApprovalWithDSC() == 1
							&& admin.getIsDrugLicenceVerification() == 1) {

						if (adminRequest.getApplicationStatus() != null && adminRequest.getApplicationStatus() == 0) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							}
						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() < 4) { // Status 1 = Approved list, Status 2 =
																				// Reject List
						//	Integer superAdminRoleId=6;
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), requestStateIdList,
//													requestDistrictIdList, superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								} else {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByCategoryAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													requestStateIdList, requestDistrictIdList,
//													superAdminRoleId, adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								} else {
									kendraApplicationList = adminKendraRepo
											.findByAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							}
						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() == 4) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								}
							} else {

								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDrugLicenceVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByAdminIdOrderByCreatedDate(adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							}
						}
					} else if (admin.getIsDocumentVerification() == 1 && admin.getIsIntitailApprovalWithDSC() == 1) {

						if (adminRequest.getApplicationStatus() != null && adminRequest.getApplicationStatus() == 0) {

							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {

//										kendraApplicationList = adminKendraRepo
//												.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplication(
//														adminRequest.getCategory(), requestStateIdList,requestDistrictIdList, false,
//														true);
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

								} else {
//									kendraApplicationList = adminKendraRepo
//											.findByCategoryAndIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
//													adminRequest.getCategory(), false, stateIdList, districtIdList,
//													true);
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {

//											kendraApplicationList = adminKendraRepo
//													.findByKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplication(
//															requestStateIdList,
//															requestDistrictIdList, false, true);
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							}
						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() < 4) { // Status 1 = Approved list, Status 2 =//
																				// Reject List
				//			Integer superAdminRoleId=6;
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), requestStateIdList,
//													requestDistrictIdList, superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
									
								} else {
									
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByCategoryAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													requestStateIdList, requestDistrictIdList,
//													superAdminRoleId, adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
								} else {
									kendraApplicationList = adminKendraRepo
											.findByAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getAdminId(), adminRequest.getApplicationStatus());
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									
//									kendraApplicationList.addAll(superAdminList);
								}
							}
						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() == 4) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										if (application.getApplicationStatus() != 2
												&& application.getApplicationStatus() != 3) {
											application.setApplicationStatus(0);
										}
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										if (application.getApplicationStatus() != 2
												&& application.getApplicationStatus() != 3) {
											application.setApplicationStatus(0);
										}
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndAdminIdOrderByCreatedDate(adminRequest.getCategory(),
													adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {

									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										if (application.getApplicationStatus() != 2
												&& application.getApplicationStatus() != 3) {
											application.setApplicationStatus(0);
										}
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,
													adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsIntitailApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										if (application.getApplicationStatus() != 2
												&& application.getApplicationStatus() != 3) {
											application.setApplicationStatus(0);
										}
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByAdminIdOrderByCreatedDate(adminRequest.getAdminId());
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									
//									kendraApplicationList.addAll(superAdminList);
								}
							}
						}
					}

					else if (admin.getIsDocumentVerification() == 1) {

						if (adminRequest.getApplicationStatus() != null && adminRequest.getApplicationStatus() == 0) { // Status
																														// 0
																														// =
																														// pending
																														// list
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {

									kendraApplicationList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplicationAndApplicationStatusNotIn(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, false, true,status);

								} else {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													adminRequest.getCategory(), false, stateIdList, districtIdList,
													true,status);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {

									kendraApplicationList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplicationAndApplicationStatusNotIn(
													requestStateIdList, requestDistrictIdList, false, true,status);

								} else {

									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, stateIdList, districtIdList, true,status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							}
						}

						else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() < 4) { // Status 1 = Approved list, Status 2 =
																				// Reject List
						//	Integer superAdminRoleId=6;
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), requestStateIdList,
//													requestDistrictIdList, superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);
									} else {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndAdminIdAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), adminRequest.getAdminId(),
													adminRequest.getApplicationStatus());
									
									//08-05-2024 superadmin approvals add in sub admin list
//									List<AdminKendraApplications>	superAdminList=adminKendraRepo
//											.findByCategoryAndRoleIdAndApplicationStatusOrderByCreatedDate(
//													adminRequest.getCategory(), superAdminRoleId,
//													adminRequest.getApplicationStatus());
//									kendraApplicationList.addAll(superAdminList);	
								}
							} else if (requestStateIdList != null && requestStateIdList.size() != 0) {
								kendraApplicationList = adminKendraRepo
										.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
												requestStateIdList, requestDistrictIdList, adminRequest.getAdminId(),
												adminRequest.getApplicationStatus());
								//08-05-2024 superadmin approvals add in sub admin list
//								List<AdminKendraApplications>	superAdminList=adminKendraRepo
//										.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//												requestStateIdList, requestDistrictIdList, superAdminRoleId,
//												adminRequest.getApplicationStatus());
//								kendraApplicationList.addAll(superAdminList);
							} else {
								kendraApplicationList = adminKendraRepo
										.findByAdminIdAndApplicationStatusOrderByCreatedDate(adminRequest.getAdminId(),
												adminRequest.getApplicationStatus());
								
								//08-05-2024 superadmin approvals add in sub admin list
//								List<AdminKendraApplications>	superAdminList=adminKendraRepo
//										.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//												adminRequest.getApplicationStatus());
//								kendraApplicationList.addAll(superAdminList);
							}
						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() == 4) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDocumentVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										if (application.getApplicationStatus() != 2
												&& application.getApplicationStatus() != 3) {
											application.setApplicationStatus(0);
										}
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndIsDocumentVerificationAndApplicationStatus(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, adminRequest.getAdminId(), true, 1);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsDocumentVerificationAndCategoryAndIsLatestApplication(false,
													adminRequest.getCategory(), true);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										if (application.getApplicationStatus() != 2
												&& application.getApplicationStatus() != 3) {
											application.setApplicationStatus(0);
										}
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndAdminIdAndIsDocumentVerificationAndApplicationStatus(
													adminRequest.getCategory(), adminRequest.getAdminId(), true, 1);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								}

							} else if (requestStateIdList != null && requestStateIdList.size() != 0
									&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
								List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
										.findByIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
												false, requestStateIdList, requestDistrictIdList, true);
								for (AdminKendraApplications application : kendraApplicationPendingList) {
									if (application.getApplicationStatus() != 2
											&& application.getApplicationStatus() != 3) {
										application.setApplicationStatus(0);
									}
									kendraApplicationList.add(application);
								}

								List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
										.findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndIsDocumentVerificationAndApplicationStatus(
												requestStateIdList, requestDistrictIdList, adminRequest.getAdminId(),
												true, 1);
								kendraApplicationList.addAll(kendraApplicationApproveRejectList);
							} else {
								List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
										.findByIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
												false, stateIdList, districtIdList, true);
								for (AdminKendraApplications application : kendraApplicationPendingList) {
									if (application.getApplicationStatus() != 2
											&& application.getApplicationStatus() != 3) {
										application.setApplicationStatus(0);
									}
									kendraApplicationList.add(application);
								}

								List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
										.findByAdminIdAndIsDocumentVerificationAndApplicationStatus(
												adminRequest.getAdminId(), true, 1);
								kendraApplicationList.addAll(kendraApplicationApproveRejectList);
								//08-05-2024 superadmin approvals add in sub admin list
//								List<AdminKendraApplications>	superAdminList=adminKendraRepo
//										.findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(stateIdList,districtIdList,superAdminRoleId,
//												adminRequest.getApplicationStatus());
//								kendraApplicationList.addAll(superAdminList);
							}
						}
					}
					if (adminRequest.getPageNo() != null && adminRequest.getPageSize() > 0) {
						List<AdminKendraApplicationResponse> kendraApplicationListNew = new ArrayList<AdminKendraApplicationResponse>();
						int index=0;
						kendraApplicationList = kendraApplicationList.stream()
                                .distinct()
                                .collect(Collectors.toList());
						//from date to date  //10-07-2024
						if(adminRequest.getFromDate()!=null && !adminRequest.getFromDate().trim().isEmpty() && 
								adminRequest.getToDate()!=null && !adminRequest.getToDate().trim().isEmpty()){
						
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

					        try {
					            // Parse the date strings
					            Date fromDate = formatter.parse(adminRequest.getFromDate()+" 00:00:00");
					            Date toDate = formatter.parse(adminRequest.getToDate()+" 23:59:59");
						            // Filter the list using the stream API
						            kendraApplicationList = kendraApplicationList.stream()
						                    .filter(obj -> !obj.getCreatedDate().before(fromDate) && !obj.getCreatedDate().after(toDate))
						                    .collect(Collectors.toList());
						            // Print the filtered objects
						        //    filteredObjects.forEach(obj -> System.out.println(obj.getData()));
						        } catch (ParseException e) {
						         log.error(e.getMessage());
						        }	
						}
						for (AdminKendraApplications kendraApp : kendraApplicationList) {
							AdminKendraApplicationResponse adminKendraApp = new AdminKendraApplicationResponse();
							String createdDate = DateUtil.convertUtcToIst(kendraApp.getCreatedDate());
							BeanUtils.copyProperties(kendraApp, adminKendraApp);

							adminKendraApp.setCreatedDate(createdDate);
							if (kendraApp.getUpdated() != null) {
								String updatedDate = DateUtil.convertUtcToIst(kendraApp.getUpdated());
								adminKendraApp.setUpdated(updatedDate);
							}
							//for frontEnd team pagination
                            if(adminRequest.getPageNo() == 0) {
                            	adminKendraApp.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		adminKendraApp.setSerialNo((adminRequest.getPageSize()*adminRequest.getPageNo())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
							kendraApplicationListNew.add(adminKendraApp);
							

						}
						if(adminRequest.getSerachText()!=null && !adminRequest.getSerachText().trim().isEmpty()) {
							kendraApplicationListNew = kendraApplicationListNew.stream()
                                     .filter(obj -> (obj.getNameOfApplicant()!=null && obj.getNameOfApplicant().contains(adminRequest.getSerachText())) || (obj.getNameOfOrganization()!=null && obj.getNameOfOrganization().contains(adminRequest.getSerachText())) || obj.getApplicationId().contains(adminRequest.getSerachText()))
                                     .collect(Collectors.toList());}
						Collections.sort(kendraApplicationListNew);
						Pageable pageable = PageRequest.of(adminRequest.getPageNo(), adminRequest.getPageSize());
						PagedListHolder<AdminKendraApplicationResponse> page = new PagedListHolder<>(
								kendraApplicationListNew);
						page.setPageSize(pageable.getPageSize()); // number of items per page
						page.setPage(pageable.getPageNumber());
						adminKendraAppResponsePage.setAdminKendraApplicationsList(page.getPageList());
						adminKendraAppResponsePage.setPageIndex(page.getPage());
						adminKendraAppResponsePage.setPageSize(page.getPageSize());
						adminKendraAppResponsePage.setTotalElement(kendraApplicationListNew.stream().count());
						adminKendraAppResponsePage.setTotalPages(page.getPageCount());
						adminKendraAppResponsePage.setIsLastPage(page.isLastPage());
						adminKendraAppResponsePage.setIsFirstPage(page.isFirstPage());
						if (kendraApplicationListNew != null && kendraApplicationListNew.size() != 0) {

							map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
							map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
							map.put(DataConstant.RESPONSE_BODY, adminKendraAppResponsePage);
							log.info("In getAllKendraDetailsByAdminId",DataConstant.RECORD_FOUND_MESSAGE);

							return map;

						}
						map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
						map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
						map.put(DataConstant.RESPONSE_BODY, adminKendraAppResponsePage);
						log.info("In getAllKendraDetailsByAdminId",DataConstant.RECORD_NOT_FOUND_MESSAGE);

						return map;

					}
					map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
					map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
					map.put(DataConstant.RESPONSE_BODY, kendraApplicationList);
					log.info("In getAllKendraDetailsByAdminId",DataConstant.RECORD_FOUND_MESSAGE);

					return map;
				}
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.ADMIN_NOT_FOUND);
			map.put(DataConstant.RESPONSE_BODY, null);
			log.info("In getAllKendraDetailsByAdminId",DataConstant.ADMIN_NOT_FOUND);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.RESPONSE_BODY, null);
		log.info("In getAllKendraDetailsByAdminId",DataConstant.ADMIN_ID_NOT_NULL);

		return map;
	}

	@Override
	public Map<String, Object> getAdminStateByAdminId(Long adminId) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (adminId != null && adminId != 0) {
			Admin admin = adminRepo.findById(adminId).orElse(null);
			if (admin != null) {
				AdminStateRequestPayload adminStateId = new AdminStateRequestPayload();
				List<Integer> adminStateIds = new ArrayList<Integer>();
				for (AdminStateMapping adminState : admin.getAdminStateMapping()) {
					adminStateIds.add(adminState.getStateId().intValue());
				}
				adminStateId.setStateIds(adminStateIds);
				Map<String, Object> mapData = kendraService.getStateOfIndiaByStateIds(adminStateId);
				return mapData;
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.ADMIN_NOT_FOUND);
			map.put(DataConstant.RESPONSE_BODY, null);
			log.info("In getAdminStateByAdminId",DataConstant.ADMIN_NOT_FOUND);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.RESPONSE_BODY, null);
		log.info("In getAdminStateByAdminId",DataConstant.ADMIN_ID_NOT_NULL);
		return map;
	}

	@Override
	public Map<String, Object> getAllKendraDetailsBySuperAdmin(AdminApplicationRequest adminRequest) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (adminRequest.getPageNo() == null && adminRequest.getPageSize() == null) {
			log.info(" In getAllKendraDetailsBySuperAdmin",DataConstant.INVALID_REQUEST);
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.INVALID_REQUEST);
			map.put(DataConstant.DATA, null);
			return map;
		}
		if (adminRequest.getAdminId() != null && adminRequest.getAdminId() != 0) {
			Optional<Admin> isAdmin = adminRepo.findById(adminRequest.getAdminId());
			if (isAdmin.isPresent()) {
				Admin admin = isAdmin.get();
				if (admin.getIsView() == 1) {
					AdminKendraAppResponsePage adminKendraAppResponsePage = new AdminKendraAppResponsePage();
					List<AdminKendraApplications> kendraApplicationList = new ArrayList<AdminKendraApplications>();
//					List<Long> stateIdList = new ArrayList<>();
//					List<Long> districtIdList = new ArrayList<>();
//					for (AdminStateMapping adminState : admin.getAdminStateMapping()) {
//						stateIdList.add(adminState.getStateId());
//						districtIdList.addAll(adminState.getDistrictId());
//					}
					Integer[] status = { 2, 3 };
					List<Long> requestStateIdList = new ArrayList<>();
					List<Long> requestDistrictIdList = new ArrayList<>();
					if (adminRequest.getAdminStateMapping() != null) {
						for (AdminStateResponsePayload adminState : adminRequest.getAdminStateMapping()) {
							requestStateIdList.add(adminState.getStateId());
							requestDistrictIdList.addAll(adminState.getDistrictId());
						}
					}
					if (admin.getIsDocumentVerification() == 1 && admin.getIsIntitailApprovalWithDSC() == 1
							&& admin.getIsDrugLicenceVerification() == 1 && admin.getIsAggrementVerification() == 1
							&& admin.getIsFinalApprovalWithDSC() == 1) {

						if (adminRequest.getApplicationStatus() != null && adminRequest.getApplicationStatus() == 0) {
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndAndIsLatestApplicationAndApplicationStatusNotIn(
													false, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() < 4) { // Status 1 = Approved list, Status 2 =
																				// Reject List
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									if(adminRequest.getApplicationStatus() ==1) {
										kendraApplicationList = adminKendraRepo
												.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
														adminRequest.getCategory(), requestStateIdList,
														requestDistrictIdList, true, adminRequest.getApplicationStatus(),true);
									}else {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatus(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, adminRequest.getApplicationStatus());
									}
									} else {
										if(adminRequest.getApplicationStatus() ==1) {
											kendraApplicationList = adminKendraRepo
													.findByCategoryAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
															adminRequest.getCategory(), true,
															adminRequest.getApplicationStatus(),true);		
										}
										else {
									kendraApplicationList = adminKendraRepo
											.findByCategoryAndIsLatestApplicationAndApplicationStatusOrderByCreatedDate(
													adminRequest.getCategory(), true,
													adminRequest.getApplicationStatus());
									}

								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									if(adminRequest.getApplicationStatus() ==1) {
										kendraApplicationList = adminKendraRepo
												.findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
														requestStateIdList, requestDistrictIdList,true,
														adminRequest.getApplicationStatus(),true);		
									}else {
									kendraApplicationList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,true,
													adminRequest.getApplicationStatus());
									}
								} else {
									if(adminRequest.getApplicationStatus() ==1) {
										kendraApplicationList = adminKendraRepo
												.findByIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(true,
														adminRequest.getApplicationStatus(),true);	
									}else {
									kendraApplicationList = adminKendraRepo
											.findByIsLatestApplicationAndApplicationStatusOrderByCreatedDate(true,
													adminRequest.getApplicationStatus());
									}
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() == 4) {
							Integer[] appStatus = { 1, 2, 3 };
							if (adminRequest.getCategory() != null && !adminRequest.getCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusIn(
													adminRequest.getCategory(), requestStateIdList,
													requestDistrictIdList, true, appStatus);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByCategoryAndIsLatestApplicationAndApplicationStatusIn(
													adminRequest.getCategory(), true, appStatus);
									
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusIn(
													requestStateIdList, requestDistrictIdList, true, appStatus);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndAndIsLatestApplicationAndApplicationStatusNotIn(
													false, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByIsLatestApplicationAndApplicationStatusIn(true, appStatus);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								}
							}

						}

					}
					if (adminRequest.getPageNo() != null && adminRequest.getPageSize() > 0) {
						List<AdminKendraApplicationResponse> kendraApplicationListNew = new ArrayList<AdminKendraApplicationResponse>();
						int index=0;
						//from date to date  //10-07-2024
						
						if(adminRequest.getFromDate()!=null && !adminRequest.getFromDate().trim().isEmpty() && 
								adminRequest.getToDate()!=null && !adminRequest.getToDate().trim().isEmpty()){

							 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

						        try {
						            // Parse the date strings
						            Date fromDate = formatter.parse(adminRequest.getFromDate()+" 00:00:00");
						            Date toDate = formatter.parse(adminRequest.getToDate()+" 23:59:59");
						        	
//						        	Date fromDate = formatter.parse("2024-07-11 05:25:00");
//						            Date toDate = formatter.parse("2024-07-11 00:00:00");
						            // Filter the list using the stream API
						            kendraApplicationList = kendraApplicationList.stream()
						                    .filter(obj -> !obj.getCreatedDate().before(fromDate) && !obj.getCreatedDate().after(toDate))
						                    .collect(Collectors.toList());
						            // Print the filtered objects
						        //    filteredObjects.forEach(obj -> System.out.println(obj.getData()));
						        } catch (ParseException e) {
						           log.error(e.getMessage());
						        }	
						}
						for (AdminKendraApplications kendraApp : kendraApplicationList) {
							AdminKendraApplicationResponse adminKendraApp = new AdminKendraApplicationResponse();
							String createdDate = DateUtil.convertUtcToIst(kendraApp.getCreatedDate());
							BeanUtils.copyProperties(kendraApp, adminKendraApp);

							adminKendraApp.setCreatedDate(createdDate);
							if (kendraApp.getUpdated() != null) {
								String updatedDate = DateUtil.convertUtcToIst(kendraApp.getUpdated());
								adminKendraApp.setUpdated(updatedDate);
							}
							//for frontEnd team pagination
                            if(adminRequest.getPageNo() == 0) {
                            	adminKendraApp.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		adminKendraApp.setSerialNo((adminRequest.getPageSize()*adminRequest.getPageNo())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
							kendraApplicationListNew.add(adminKendraApp);
							
						}
						if(adminRequest.getSerachText()!=null && !adminRequest.getSerachText().trim().isEmpty()) {
							kendraApplicationListNew = kendraApplicationListNew.stream()
                                     .filter(obj -> (obj.getNameOfApplicant()!=null && obj.getNameOfApplicant().contains(adminRequest.getSerachText())) || (obj.getNameOfOrganization()!=null && obj.getNameOfOrganization().contains(adminRequest.getSerachText())) || obj.getApplicationId().contains(adminRequest.getSerachText()))
                                     .collect(Collectors.toList());}
						Collections.sort(kendraApplicationListNew);
						Pageable pageable = PageRequest.of(adminRequest.getPageNo(), adminRequest.getPageSize());
						PagedListHolder<AdminKendraApplicationResponse> page = new PagedListHolder<>(
								kendraApplicationListNew);
						page.setPageSize(pageable.getPageSize()); // number of items per page
						page.setPage(pageable.getPageNumber());
						adminKendraAppResponsePage.setAdminKendraApplicationsList(page.getPageList());
						adminKendraAppResponsePage.setPageIndex(page.getPage());
						adminKendraAppResponsePage.setPageSize(page.getPageSize());
						adminKendraAppResponsePage.setTotalElement(kendraApplicationListNew.stream().count());
						adminKendraAppResponsePage.setTotalPages(page.getPageCount());
						adminKendraAppResponsePage.setIsLastPage(page.isLastPage());
						adminKendraAppResponsePage.setIsFirstPage(page.isFirstPage());
						if (kendraApplicationListNew != null && kendraApplicationListNew.size() != 0) {

							map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
							map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
							map.put(DataConstant.RESPONSE_BODY, adminKendraAppResponsePage);
							log.info(" In getAllKendraDetailsBySuperAdmin",DataConstant.RECORD_FOUND_MESSAGE);

							return map;

						}
						map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
						map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
						map.put(DataConstant.RESPONSE_BODY, adminKendraAppResponsePage);
						log.info(" In getAllKendraDetailsBySuperAdmin",DataConstant.RECORD_NOT_FOUND_MESSAGE);

						return map;

					}
				}
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_FOUND);
			map.put(DataConstant.DATA, null);
			log.info(" In getAllKendraDetailsBySuperAdmin",DataConstant.ADMIN_ID_NOT_FOUND);

			return map;

		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.DATA, null);
		log.info(" In getAllKendraDetailsBySuperAdmin",DataConstant.ADMIN_ID_NOT_NULL);

		return map;
	}

	
	@Override
	public Map<String, Object> getAdminDashboardAppCount(AdminApplicationRequest adminRequest) {
		
		Map<String, Object> map = new HashMap<String, Object>();
//		AdminApplicationRequest adminApplicationRequest = new AdminApplicationRequest();
		AdminDashBoardAppCountResponse dashBoardResponse = new AdminDashBoardAppCountResponse();
		if (adminRequest.getAdminId() != null &&adminRequest.getAdminId() != 0 && adminRequest.getRoleId() != null && adminRequest.getRoleId() != 0) {
			Admin admin = adminRepo.findById(adminRequest.getAdminId()).orElse(null);
			if (admin!=null) {
				Map<String, Object> adminAllMapData;
				Map<String, Object> adminPendingMapData;
				Map<String, Object> adminApproveMapData;
				Map<String, Object> adminRejectMapData;
				Map<String, Object> adminHoldMapData;
													
				List<Long> requestStateIdList = new ArrayList<>();
				List<Long> requestDistrictIdList = new ArrayList<>();
				if(adminRequest.getAdminStateMapping()!=null) {
				for (AdminStateResponsePayload adminState : adminRequest.getAdminStateMapping()) {
					requestStateIdList.add(adminState.getStateId());
					requestDistrictIdList.addAll(adminState.getDistrictId());
				}
				}

				if (adminRequest.getRoleId() != 6 && admin.getAccessCategory()==null ) {
					List<Long> stateIdList = new ArrayList<>();
					List<Long> districtIdList = new ArrayList<>();
					for (AdminStateMapping adminState : admin.getAdminStateMapping()) {
						stateIdList.add(adminState.getStateId());
						districtIdList.addAll(adminState.getDistrictId());
					}
					adminRequest.setApplicationStatus(4);
					adminAllMapData = getAllKendraDetailsByAdminId(adminRequest);
					adminRequest.setApplicationStatus(0);
					adminPendingMapData = getAllKendraDetailsByAdminId(adminRequest);
					adminRequest.setApplicationStatus(1);
					adminApproveMapData = getAllKendraDetailsByAdminId(adminRequest);
					adminRequest.setApplicationStatus(2);
					adminRejectMapData = getAllKendraDetailsByAdminId(adminRequest);
					adminRequest.setApplicationStatus(3);
					adminHoldMapData = getAllKendraDetailsByAdminId(adminRequest);
					
					PaidUserRequest paidUser=new PaidUserRequest();
					paidUser.setPaymentStatus(DataConstant.SUCCESS);
					paidUser.setCategory(adminRequest.getCategory());
					if(adminRequest.getAdminStateMapping()==null) {
						paidUser.setStateId(stateIdList);
						paidUser.setDistrictId(districtIdList);
					}
					else {
					paidUser.setStateId(requestStateIdList);
					paidUser.setDistrictId(requestDistrictIdList);
					}
					try {
					Map<String,Object> adminPaymentMap=kendraService.getAllPaidUsers(paidUser);
					if(adminPaymentMap.containsValue("200")) {
						Integer payment=(Integer) adminPaymentMap.get(DataConstant.RESPONSE_BODY);
						dashBoardResponse.setTotalPayment(payment.longValue());
						}
					}catch(Exception e) {
						log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
					}
				}
				//
				else if (admin.getAccessCategory()!=null && !admin.getAccessCategory().trim().isEmpty()) {
					List<Long> stateIdList = new ArrayList<>();
					List<Long> districtIdList = new ArrayList<>();
					for (AdminStateMapping adminState : admin.getAdminStateMapping()) {
						stateIdList.add(adminState.getStateId());
						districtIdList.addAll(adminState.getDistrictId());
					}
					adminRequest.setApplicationStatus(4);
					adminAllMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(0);
					adminPendingMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(1);
					adminApproveMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(2);
					adminRejectMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(3);
					adminHoldMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					
					PaidUserRequest paidUser=new PaidUserRequest();
					paidUser.setPaymentStatus(DataConstant.SUCCESS);
					paidUser.setCategory(adminRequest.getCategory());
					paidUser.setSubCategory(admin.getAccessSubCategory());
					if(adminRequest.getAdminStateMapping()==null) {
						paidUser.setStateId(stateIdList);
						paidUser.setDistrictId(districtIdList);
					}
					else {
					paidUser.setStateId(requestStateIdList);
					paidUser.setDistrictId(requestDistrictIdList);
					}
					try {
					Map<String,Object> adminPaymentMap=kendraService.getAllPaidUsers(paidUser);
					if(adminPaymentMap.containsValue("200")) {
						Integer payment=(Integer) adminPaymentMap.get(DataConstant.RESPONSE_BODY);
						dashBoardResponse.setTotalPayment(payment.longValue());
						}
					}catch(Exception e) {
						log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
					}
				}
				//
				else {
					adminRequest.setApplicationStatus(4);
					adminAllMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(0);
					adminPendingMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(1);
					adminApproveMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(2);
					adminRejectMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(3);
					adminHoldMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					PaidUserRequest paidUser=new PaidUserRequest();
					paidUser.setPaymentStatus(DataConstant.SUCCESS);
					paidUser.setCategory(adminRequest.getCategory());
					if(adminRequest.getAdminStateMapping()!=null) {
					paidUser.setStateId(requestStateIdList);
					paidUser.setDistrictId(requestDistrictIdList);
					}
					try {
					Map<String,Object> adminPaymentMap=kendraService.getAllPaidUsers(paidUser);
					if(adminPaymentMap.containsValue("200")) {
						Integer payment=(Integer) adminPaymentMap.get(DataConstant.RESPONSE_BODY);
						dashBoardResponse.setTotalPayment(payment.longValue());
						}
					}catch(Exception e) {
						log.info(DataConstant.SERVER_MESSAGE, e.getMessage());	
					}
				}
				AdminKendraAppResponsePage allResponseData = (AdminKendraAppResponsePage) adminAllMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage pendingResponseData = (AdminKendraAppResponsePage) adminPendingMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage approveResponseData = (AdminKendraAppResponsePage) adminApproveMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage rejctResponseData = (AdminKendraAppResponsePage) adminRejectMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage HoldResponseData = (AdminKendraAppResponsePage) adminHoldMapData
						.get(DataConstant.RESPONSE_BODY);
				
				dashBoardResponse.setTotalApplication(allResponseData.getTotalElement());
				dashBoardResponse.setPendingApplication(pendingResponseData.getTotalElement());
				dashBoardResponse.setApproveApplication(approveResponseData.getTotalElement());
				dashBoardResponse.setRejectApplication(rejctResponseData.getTotalElement());
				dashBoardResponse.setHoldApplication(HoldResponseData.getTotalElement());
				
				Map<String,Object> userMapData=authService.getAllUsersByRoleId(0);
				if(userMapData.containsValue(200)) {
					@SuppressWarnings("unchecked")
					List<UserResponsePayload> userList=(List<UserResponsePayload>) userMapData.get(DataConstant.RESPONSE_BODY);
					if(userList!=null) {
						Integer users=userList.size();
						dashBoardResponse.setTotalUser(users.longValue());	
					}
				}
				map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
				map.put(DataConstant.RESPONSE_BODY, dashBoardResponse);
				log.info(" In getAdminDashboardAppCount",DataConstant.RECORD_FOUND_MESSAGE);

				return map;
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_FOUND);
			map.put(DataConstant.DATA, null);
			log.info(" In getAdminDashboardAppCount",DataConstant.ADMIN_ID_NOT_FOUND);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.DATA, null);
		log.info(" In getAdminDashboardAppCount",DataConstant.ADMIN_ID_NOT_NULL);

		return map;
	}
	@Override
	public Map<String, Object> removeDrugFromRejectList(String applicationId) {
		
		Map<String,Object> map=new HashMap<String, Object>(); 
		if(applicationId!=null) {
			
		AdminKendraApplications drugDetailRejected=adminKendraRepo.findByApplicationIdAndIsIntitailApprovalWithDSCAndIsDrugLicenceVerificationAndIsLatestApplicationAndApplicationStatus(applicationId,true, false,true,2);
		if(drugDetailRejected!=null) {
			adminKendraRepo.delete(drugDetailRejected);
			log.info(" In removeDrugFromRejectList data delete",drugDetailRejected);

			AdminKendraApplications latestApplication=adminKendraRepo.findByApplicationIdAndLatest(applicationId);
			latestApplication.setLatestApplication(true);
			adminKendraRepo.save(latestApplication);
			log.info(" In removeDrugFromRejectList data save",latestApplication);
			}
		AdminKendraApplications drugAgrrementRejected=adminKendraRepo.findByApplicationIdAndIsDrugLicenceVerificationAndIsAggrementVerificationAndIsLatestApplicationAndApplicationStatus(applicationId,true, false,true,2);
		if(drugAgrrementRejected!=null) {
			adminKendraRepo.delete(drugAgrrementRejected);
			log.info(" In removeDrugFromRejectList data delete",drugAgrrementRejected);

			AdminKendraApplications latestApplication=adminKendraRepo.findByApplicationIdAndLatest(applicationId);
			latestApplication.setLatestApplication(true);
			adminKendraRepo.save(latestApplication);
			log.info(" In removeDrugFromRejectList data save",latestApplication);

		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
		map.put(DataConstant.MESSAGE, DataConstant.RECORD_REMOVED_SUUCESSFULLY);
		map.put(DataConstant.RESPONSE_BODY, null);
			log.info(" In removeDrugFromRejectList ",DataConstant.RECORD_REMOVED_SUUCESSFULLY);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.DATA, null);
		log.info(" In removeDrugFromRejectList ",DataConstant.ADMIN_ID_NOT_NULL);

		return map;
		
	}
	
	
	@Override
	public Map<String, Object> exportAllKendraDetailsByAdmin(AdminApplicationRequest adminRequest) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		ExportKendraAppResponsePage exportKendraAppResponsePage=new ExportKendraAppResponsePage();
		List<ExportKendraAppResponse> exportKendraAppResponseList=new ArrayList<>();
		if (adminRequest.getAdminId() != null &&adminRequest.getAdminId() != 0 && adminRequest.getRoleId() != null && adminRequest.getRoleId() != 0) {
//			Admin admin = adminRepo.findById(adminRequest.getAdminId()).orElse(null);
//			if (admin!=null) {
				Map<String,Object> adminMapData=null;
				if(adminRequest.getRoleId()==6) {
					 adminMapData=getAllKendraDetailsBySuperAdmin(adminRequest);
					 if(!adminMapData.containsValue(200)){
						return adminMapData; 
					 }
				}else {
					adminMapData=getAllKendraDetailsByAdminId(adminRequest);
					if(!adminMapData.containsValue(200)){
						return adminMapData; 
					 }
				}
				AdminKendraAppResponsePage adminKendraAppResponsePage=(AdminKendraAppResponsePage) adminMapData.get(DataConstant.RESPONSE_BODY);
				if(adminKendraAppResponsePage!=null) {
					Map<Object, Object> kendraDetailListMap=kendraService.getAllKendraDetails(null, null, null);
					 ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
				        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				        KendraDetailsResponsePage kendraDetailsResponsePage = mapper.convertValue(kendraDetailListMap.get("data"), KendraDetailsResponsePage.class);
					//KendraDetailsResponsePage kendraList= (KendraDetailsResponsePage) kendraDetailListMap.get("data");
					if(kendraDetailsResponsePage!=null) {
					for(AdminKendraApplicationResponse appResponse:adminKendraAppResponsePage.getAdminKendraApplicationsList()){
						for(KendraDetailsResponsePayload kendraResponse:kendraDetailsResponsePage.getKendraDetailsResponsePayloadList()) {
							if(appResponse.getApplicationId().equals(kendraResponse.getApplicationId())) {
						AdminKendraApplications admiKendraApp=adminKendraRepo.findByApplicationIdAndIsDocumentVerification(appResponse.getApplicationId(),true);	
						ExportKendraAppResponse exprotKendra=new ExportKendraAppResponse();
						BeanUtils.copyProperties(appResponse, exprotKendra);
						exprotKendra.setLatitude(kendraResponse.getLatitude());
						exprotKendra.setLongitude(kendraResponse.getLongitude());
						if(kendraResponse.getPartyNames()!=null && !kendraResponse.getPartyNames().isEmpty() &&kendraResponse.getPartyNames().size()>0) {
							List<PartyNameModelRequestPayload> partyNameModelRequestPayload= new ArrayList<>();

							for(PartyNameModelRequestPayload partyName :kendraResponse.getPartyNames()){
								//PartyNameModelRequestPayload payload =new PartyNameModelRequestPayload();
								partyNameModelRequestPayload.add(partyName);
							}
							exprotKendra.setPartyNames(partyNameModelRequestPayload);

	////						BeanUtils.copyProperties(kendraResponse.getPartyNames(), exprotKendra.getPartyNames());
						}
								BeanUtils.copyProperties(appResponse, exprotKendra);
						if(admiKendraApp!=null) {
							Admin admin=adminRepo.findById(admiKendraApp.getAdminId()).orElse(null);
							if(admin!=null) {
								exprotKendra.setFieldOfficer(admin.getUserName());
							}
						}
						Map<String, Object> userStateDistrictMap=kendraService.getDistrictOfIndiaByDistrictId(kendraResponse.getDistrictId());
						if(userStateDistrictMap!=null && userStateDistrictMap.containsValue("200")) {
							ObjectMapper mapper1 = new ObjectMapper(); // jackson's objectmapper
					        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					        DistrictOfIndiaResponse userDistrictOfIndia = mapper1.convertValue(userStateDistrictMap.get(DataConstant.RESPONSE_BODY
							), DistrictOfIndiaResponse.class);
						
							//DistrictOfIndiaResponse districtOfIndia=(DistrictOfIndiaResponse) userStateDistrictMap.get("responseBody");
							exprotKendra.setUserStateDistrict(userDistrictOfIndia);
							if (appResponse.getKendraDistrictId().equals(kendraResponse.getDistrictId())) {
								exprotKendra.setProposedStateDistrict(userDistrictOfIndia);
							} else {
								Map<String, Object> proposedStateDistrictMap = kendraService
										.getDistrictOfIndiaByDistrictId(appResponse.getKendraDistrictId());
								if (proposedStateDistrictMap != null && proposedStateDistrictMap.containsValue("200")) {
									ObjectMapper mapper2 = new ObjectMapper(); // jackson's objectmapper
									mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
									DistrictOfIndiaResponse proposedDistrictOfIndia = mapper2.convertValue(
											proposedStateDistrictMap.get(DataConstant.RESPONSE_BODY),
											DistrictOfIndiaResponse.class);
									// DistrictOfIndiaResponse districtOfIndia=(DistrictOfIndiaResponse)
									// proposedStateDistrictMap.get("responseBody");
									exprotKendra.setProposedStateDistrict(proposedDistrictOfIndia);
								}
							}
						}
						exprotKendra.setMobileNo(kendraResponse.getMobileNo());
						exportKendraAppResponseList.add(exprotKendra);
						
						}
						}
					}
					BeanUtils.copyProperties(adminKendraAppResponsePage, exportKendraAppResponsePage);
					exportKendraAppResponsePage.setExportKendraApplicationsList(exportKendraAppResponseList);
					map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
					map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
					map.put(DataConstant.RESPONSE_BODY, exportKendraAppResponsePage);
						log.info(" In exportAllKendraDetailsByAdmin data found ",exportKendraAppResponsePage);

						return map;
					}
				}
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
			map.put(DataConstant.DATA, null);
		log.info(" In exportAllKendraDetailsByAdmin  ",DataConstant.ADMIN_ID_NOT_NULL);

		return map;
		
	}
	@Override
	public Map<String, Object> initialLatterExpiredRequestExtension(String applicationId) {
		
		Map<String,Object> map=new HashMap<String, Object>(); 
		if(applicationId!=null) {
			
		AdminKendraApplications initialExpired=adminKendraRepo.findByApplicationIdAndIsIntitailApprovalWithDSCAndIsDrugLicenceVerificationAndIsLatestApplicationAndApplicationStatus(applicationId,true, false,true,1);
		if(initialExpired!=null) {
			adminKendraRepo.delete(initialExpired);
			log.info(" In initialLatterExpiredRequestExtension data delete  ",initialExpired);
			AdminKendraApplications latestApplication=adminKendraRepo.findByApplicationIdAndLatest(applicationId);
			latestApplication.setLatestApplication(true);
			adminKendraRepo.save(latestApplication);
			}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
		map.put(DataConstant.MESSAGE, DataConstant.RECORD_REMOVED_SUUCESSFULLY);
		map.put(DataConstant.RESPONSE_BODY, null);
		log.info(" In initialLatterExpiredRequestExtension data remove ",DataConstant.RECORD_REMOVED_SUUCESSFULLY);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.DATA, null);
		log.info(" In initialLatterExpiredRequestExtension  ",DataConstant.ADMIN_ID_NOT_NULL);
		return map;
	}
	
	@Override
	public Map<String, Object> getAllKendraDetailsByCategoryAdmin(AdminApplicationRequest adminRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (adminRequest.getPageNo() == null && adminRequest.getPageSize() == null) {
			 log.info(DataConstant.INVALID_REQUEST);
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.INVALID_REQUEST);
			map.put(DataConstant.DATA, null);
			return map;
		}
		if (adminRequest.getAdminId() != null && adminRequest.getAdminId() != 0) {
			Optional<Admin> isAdmin = adminRepo.findById(adminRequest.getAdminId());
			if (isAdmin.isPresent()) {
				Admin admin = isAdmin.get();
				if (admin.getIsView() == 1) {
					AdminKendraAppResponsePage adminKendraAppResponsePage = new AdminKendraAppResponsePage();
					List<AdminKendraApplications> kendraApplicationList = new ArrayList<AdminKendraApplications>();
//					List<Long> stateIdList = new ArrayList<>();
//					List<Long> districtIdList = new ArrayList<>();
//					for (AdminStateMapping adminState : admin.getAdminStateMapping()) {
//						stateIdList.add(adminState.getStateId());
//						districtIdList.addAll(adminState.getDistrictId());
//					}
					Integer[] status = { 2, 3 };
					List<Long> requestStateIdList = new ArrayList<>();
					List<Long> requestDistrictIdList = new ArrayList<>();
					if (adminRequest.getAdminStateMapping() != null) {
						for (AdminStateResponsePayload adminState : adminRequest.getAdminStateMapping()) {
							requestStateIdList.add(adminState.getStateId());
							requestDistrictIdList.addAll(adminState.getDistrictId());
						}
					}
				//	adminRequest.setCategory(admin.getAccessCategory());
					adminRequest.setSubCategory(admin.getAccessSubCategory());
				
//					if (admin.getIsDocumentVerification() == 1 && admin.getIsIntitailApprovalWithDSC() == 1
//							&& admin.getIsDrugLicenceVerification() == 1 && admin.getIsAggrementVerification() == 1
//							&& admin.getIsFinalApprovalWithDSC() == 1) {

						if (adminRequest.getApplicationStatus() != null && adminRequest.getApplicationStatus() == 0) {
							if (adminRequest.getSubCategory() != null && !adminRequest.getSubCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndSubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getSubCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndSubCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getSubCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndAndIsLatestApplicationAndApplicationStatusNotIn(
													false, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() < 4) { // Status 1 = Approved list, Status 2 =
																				// Reject List
							if (adminRequest.getSubCategory() != null && !adminRequest.getSubCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									if(adminRequest.getApplicationStatus() ==1) {
										kendraApplicationList = adminKendraRepo
												.findBySubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
														adminRequest.getSubCategory(), requestStateIdList,
														requestDistrictIdList, true, adminRequest.getApplicationStatus(),true);
									}else {
									kendraApplicationList = adminKendraRepo
											.findBySubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatus(
													adminRequest.getSubCategory(), requestStateIdList,
													requestDistrictIdList, true, adminRequest.getApplicationStatus());
									}
									} else {
										if(adminRequest.getApplicationStatus() ==1) {
											kendraApplicationList = adminKendraRepo
													.findBySubCategoryAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
															adminRequest.getSubCategory(), true,
															adminRequest.getApplicationStatus(),true);		
										}
										else {
									kendraApplicationList = adminKendraRepo
											.findBySubCategoryAndIsLatestApplicationAndApplicationStatusOrderByCreatedDate(
													adminRequest.getSubCategory(), true,
													adminRequest.getApplicationStatus());
									}

								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									if(adminRequest.getApplicationStatus() ==1) {
										kendraApplicationList = adminKendraRepo
												.findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
														requestStateIdList, requestDistrictIdList,true,
														adminRequest.getApplicationStatus(),true);		
									}else {
									kendraApplicationList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusOrderByCreatedDate(
													requestStateIdList, requestDistrictIdList,true,
													adminRequest.getApplicationStatus());
									}
								} else {
									if(adminRequest.getApplicationStatus() ==1) {
										kendraApplicationList = adminKendraRepo
												.findByIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(true,
														adminRequest.getApplicationStatus(),true);	
									}else {
									kendraApplicationList = adminKendraRepo
											.findByIsLatestApplicationAndApplicationStatusOrderByCreatedDate(true,
													adminRequest.getApplicationStatus());
									}
								}
							}

						} else if (adminRequest.getApplicationStatus() != null
								&& adminRequest.getApplicationStatus() == 4) {
							Integer[] appStatus = { 1, 2, 3 };
							if (adminRequest.getSubCategory() != null && !adminRequest.getSubCategory().trim().isEmpty()) {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndSubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getSubCategory(), requestStateIdList,
													requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}

									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findBySubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusIn(
													adminRequest.getSubCategory(), requestStateIdList,
													requestDistrictIdList, true, appStatus);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndSubCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
													false, adminRequest.getSubCategory(), true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findBySubCategoryAndIsLatestApplicationAndApplicationStatusIn(
													adminRequest.getSubCategory(), true, appStatus);
									
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								}
							} else {
								if (requestStateIdList != null && requestStateIdList.size() != 0
										&& requestDistrictIdList != null && requestDistrictIdList.size() != 0) {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
													false, requestStateIdList, requestDistrictIdList, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusIn(
													requestStateIdList, requestDistrictIdList, true, appStatus);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								} else {
									List<AdminKendraApplications> kendraApplicationPendingList = adminKendraRepo
											.findByIsFinalApprovalWithDSCAndAndIsLatestApplicationAndApplicationStatusNotIn(
													false, true, status);
									for (AdminKendraApplications application : kendraApplicationPendingList) {
										application.setApplicationStatus(0);
										kendraApplicationList.add(application);
									}
									List<AdminKendraApplications> kendraApplicationApproveRejectList = adminKendraRepo
											.findByIsLatestApplicationAndApplicationStatusIn(true, appStatus);
									kendraApplicationList.addAll(kendraApplicationApproveRejectList);
									Set<AdminKendraApplications> set = new HashSet<>(kendraApplicationList);
									kendraApplicationList =new ArrayList<AdminKendraApplications>(set);
								}
							}

						}

			//		}
					if (adminRequest.getPageNo() != null && adminRequest.getPageSize() > 0) {
						List<AdminKendraApplicationResponse> kendraApplicationListNew = new ArrayList<AdminKendraApplicationResponse>();
						int index=0;
						//from date to date  //10-07-2024
						if(adminRequest.getFromDate()!=null && !adminRequest.getFromDate().trim().isEmpty() && 
								adminRequest.getToDate()!=null && !adminRequest.getToDate().trim().isEmpty()){
						
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

					        try {
					            // Parse the date strings
					            Date fromDate = formatter.parse(adminRequest.getFromDate()+" 00:00:00");
					            Date toDate = formatter.parse(adminRequest.getToDate()+" 23:59:59");
						            
						            // Filter the list using the stream API
						            kendraApplicationList = kendraApplicationList.stream()
						                    .filter(obj -> !obj.getCreatedDate().before(fromDate) && !obj.getCreatedDate().after(toDate))
						                    .collect(Collectors.toList());
						            // Print the filtered objects
						        //    filteredObjects.forEach(obj -> System.out.println(obj.getData()));
						        } catch (ParseException e) {
								log.error(e.getMessage());
							}
						}
						for (AdminKendraApplications kendraApp : kendraApplicationList) {
							AdminKendraApplicationResponse adminKendraApp = new AdminKendraApplicationResponse();
							String createdDate = DateUtil.convertUtcToIst(kendraApp.getCreatedDate());
							BeanUtils.copyProperties(kendraApp, adminKendraApp);

							adminKendraApp.setCreatedDate(createdDate);
							if (kendraApp.getUpdated() != null) {
								String updatedDate = DateUtil.convertUtcToIst(kendraApp.getUpdated());
								adminKendraApp.setUpdated(updatedDate);
							}
							//for frontEnd team pagination
                            if(adminRequest.getPageNo() == 0) {
                            	adminKendraApp.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		adminKendraApp.setSerialNo((adminRequest.getPageSize()*adminRequest.getPageNo())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
							kendraApplicationListNew.add(adminKendraApp);
						}
						if(adminRequest.getSerachText()!=null && !adminRequest.getSerachText().trim().isEmpty()) {
							kendraApplicationListNew = kendraApplicationListNew.stream()
                                     .filter(obj -> (obj.getNameOfApplicant()!=null && obj.getNameOfApplicant().contains(adminRequest.getSerachText())) || (obj.getNameOfOrganization()!=null && obj.getNameOfOrganization().contains(adminRequest.getSerachText())) || obj.getApplicationId().contains(adminRequest.getSerachText()))
                                     .collect(Collectors.toList());}
						Collections.sort(kendraApplicationListNew);
						Pageable pageable = PageRequest.of(adminRequest.getPageNo(), adminRequest.getPageSize());
						PagedListHolder<AdminKendraApplicationResponse> page = new PagedListHolder<>(
								kendraApplicationListNew);
						page.setPageSize(pageable.getPageSize()); // number of items per page
						page.setPage(pageable.getPageNumber());
						adminKendraAppResponsePage.setAdminKendraApplicationsList(page.getPageList());
						adminKendraAppResponsePage.setPageIndex(page.getPage());
						adminKendraAppResponsePage.setPageSize(page.getPageSize());
						adminKendraAppResponsePage.setTotalElement(kendraApplicationListNew.stream().count());
						adminKendraAppResponsePage.setTotalPages(page.getPageCount());
						adminKendraAppResponsePage.setIsLastPage(page.isLastPage());
						adminKendraAppResponsePage.setIsFirstPage(page.isFirstPage());
						if (kendraApplicationListNew != null && kendraApplicationListNew.size() != 0) {

							map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
							map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
							map.put(DataConstant.RESPONSE_BODY, adminKendraAppResponsePage);
							log.info(" In getAllKendraDetailsByCategoryAdmin  ",DataConstant.RECORD_FOUND_MESSAGE);

							return map;

						}
						map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
						map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
						map.put(DataConstant.RESPONSE_BODY, adminKendraAppResponsePage);
						log.info(" In getAllKendraDetailsByCategoryAdmin  ",DataConstant.RECORD_NOT_FOUND_MESSAGE);

						return map;

					}
				}
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_FOUND);
			map.put(DataConstant.DATA, null);
			log.info(" In getAllKendraDetailsByCategoryAdmin  ",DataConstant.ADMIN_ID_NOT_FOUND);

			return map;

		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.DATA, null);
		log.info(" In getAllKendraDetailsByCategoryAdmin  ",DataConstant.ADMIN_ID_NOT_NULL);

		return map;
	}
	
	@Override
	public Map<String, Object> getCategoryAdminDashboardAppCount(AdminApplicationRequest adminRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
//		AdminApplicationRequest adminApplicationRequest = new AdminApplicationRequest();
		AdminDashBoardAppCountResponse dashBoardResponse = new AdminDashBoardAppCountResponse();
		if (adminRequest.getAdminId() != null &&adminRequest.getAdminId() != 0 && adminRequest.getRoleId() != null && adminRequest.getRoleId() != 0) {
			Admin admin = adminRepo.findById(adminRequest.getAdminId()).orElse(null);
			if (admin!=null) {
				Map<String, Object> adminAllMapData;
				Map<String, Object> adminPendingMapData;
				Map<String, Object> adminApproveMapData;
				Map<String, Object> adminRejectMapData;
				Map<String, Object> adminHoldMapData;
													
				List<Long> requestStateIdList = new ArrayList<>();
				List<Long> requestDistrictIdList = new ArrayList<>();
				if(adminRequest.getAdminStateMapping()!=null) {
				for (AdminStateResponsePayload adminState : adminRequest.getAdminStateMapping()) {
					requestStateIdList.add(adminState.getStateId());
					requestDistrictIdList.addAll(adminState.getDistrictId());
				}
				}

				if (adminRequest.getRoleId() != 6) {
					List<Long> stateIdList = new ArrayList<>();
					List<Long> districtIdList = new ArrayList<>();
					for (AdminStateMapping adminState : admin.getAdminStateMapping()) {
						stateIdList.add(adminState.getStateId());
						districtIdList.addAll(adminState.getDistrictId());
					}
					adminRequest.setApplicationStatus(4);
					adminAllMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(0);
					adminPendingMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(1);
					adminApproveMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(2);
					adminRejectMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					adminRequest.setApplicationStatus(3);
					adminHoldMapData = getAllKendraDetailsByCategoryAdmin(adminRequest);
					
					PaidUserRequest paidUser=new PaidUserRequest();
					paidUser.setPaymentStatus(DataConstant.SUCCESS);
					paidUser.setCategory(adminRequest.getCategory());
					paidUser.setSubCategory(admin.getAccessCategory());
					if(adminRequest.getAdminStateMapping()==null) {
						paidUser.setStateId(stateIdList);
						paidUser.setDistrictId(districtIdList);
					}
					else {
					paidUser.setStateId(requestStateIdList);
					paidUser.setDistrictId(requestDistrictIdList);
					}
					try {
					Map<String,Object> adminPaymentMap=kendraService.getAllPaidUsers(paidUser);
					if(adminPaymentMap.containsValue("200")) {
						Integer payment=(Integer) adminPaymentMap.get(DataConstant.RESPONSE_BODY);
						dashBoardResponse.setTotalPayment(payment.longValue());
						}
					}catch(Exception e) {
						log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
					}
				} else {
					adminRequest.setApplicationStatus(4);
					adminAllMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(0);
					adminPendingMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(1);
					adminApproveMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(2);
					adminRejectMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					adminRequest.setApplicationStatus(3);
					adminHoldMapData = getAllKendraDetailsBySuperAdmin(adminRequest);
					PaidUserRequest paidUser=new PaidUserRequest();
					paidUser.setPaymentStatus(DataConstant.SUCCESS);
					paidUser.setCategory(adminRequest.getCategory());
					if(adminRequest.getAdminStateMapping()!=null) {
					paidUser.setStateId(requestStateIdList);
					paidUser.setDistrictId(requestDistrictIdList);
					}
					try {
					Map<String,Object> adminPaymentMap=kendraService.getAllPaidUsers(paidUser);
					if(adminPaymentMap.containsValue("200")) {
						Integer payment=(Integer) adminPaymentMap.get(DataConstant.RESPONSE_BODY);
						dashBoardResponse.setTotalPayment(payment.longValue());
						}
					}catch(Exception e) {
						log.info(DataConstant.SERVER_MESSAGE, e.getMessage());	
					}
				}
				AdminKendraAppResponsePage allResponseData = (AdminKendraAppResponsePage) adminAllMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage pendingResponseData = (AdminKendraAppResponsePage) adminPendingMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage approveResponseData = (AdminKendraAppResponsePage) adminApproveMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage rejctResponseData = (AdminKendraAppResponsePage) adminRejectMapData
						.get(DataConstant.RESPONSE_BODY);
				AdminKendraAppResponsePage HoldResponseData = (AdminKendraAppResponsePage) adminHoldMapData
						.get(DataConstant.RESPONSE_BODY);
				
				dashBoardResponse.setTotalApplication(allResponseData.getTotalElement());
				dashBoardResponse.setPendingApplication(pendingResponseData.getTotalElement());
				dashBoardResponse.setApproveApplication(approveResponseData.getTotalElement());
				dashBoardResponse.setRejectApplication(rejctResponseData.getTotalElement());
				dashBoardResponse.setHoldApplication(HoldResponseData.getTotalElement());
				
				Map<String,Object> userMapData=authService.getAllUsersByRoleId(0);
				if(userMapData.containsValue(200)) {
					@SuppressWarnings("unchecked")
					List<UserResponsePayload> userList=(List<UserResponsePayload>) userMapData.get(DataConstant.RESPONSE_BODY);
					if(userList!=null) {
						Integer users=userList.size();
						dashBoardResponse.setTotalUser(users.longValue());	
					}
				}
				map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
				map.put(DataConstant.RESPONSE_BODY, dashBoardResponse);
				log.info(" In getCategoryAdminDashboardAppCount  ",DataConstant.RECORD_FOUND_MESSAGE);

				return map;
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_FOUND);
			map.put(DataConstant.DATA, null);
			log.info(" In getCategoryAdminDashboardAppCount  ",DataConstant.ADMIN_ID_NOT_FOUND);

			return map;
		}
		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_NULL);
		map.put(DataConstant.DATA, null);
		log.info(" In getCategoryAdminDashboardAppCount  ",DataConstant.ADMIN_ID_NOT_NULL);

		return map;
	
	}

	@Override
	public Map<String, Object> applicationRejectByL4AndSuperAdmin(ApplicationFinalRejectPayload finalRejectPayload) {
		Map<String, Object> map = new HashMap<String, Object>();
		AdminKendraApplications docVerification=new AdminKendraApplications();
		if (!finalRejectPayload.isDocumentVerification()) {
			List<AdminKendraApplications> applicationList = adminKendraRepo
					.findAllByApplicationIdAndIsIntitailApprovalWithDSC(finalRejectPayload.getApplicationId(), true);
			if (applicationList != null) {
				adminKendraRepo.deleteAllInBatch(applicationList);
				}
			docVerification = adminKendraRepo
					.findByApplicationIdAndIsDocumentVerification(finalRejectPayload.getApplicationId(), true);
		
			}
		
		else if(!finalRejectPayload.isIntitailApprovalWithDSC()) {
			List<AdminKendraApplications> applicationList = adminKendraRepo
					.findAllByApplicationIdAndIsDrugLicenceVerification(finalRejectPayload.getApplicationId(), true);
			if (applicationList != null) {
				adminKendraRepo.deleteAllInBatch(applicationList);
				 docVerification = adminKendraRepo
							.findByApplicationIdAndIsIntitailApprovalWithDSC(finalRejectPayload.getApplicationId(), true);
			}
			
		}
		
		else if(!finalRejectPayload.isDrugLicenceVerification()) {
			List<AdminKendraApplications> applicationList = adminKendraRepo
					.findAllByApplicationIdAndIsAggrementVerification(finalRejectPayload.getApplicationId(), true);
			if (applicationList != null) {
				adminKendraRepo.deleteAllInBatch(applicationList);	
			}
			docVerification = adminKendraRepo
					.findByApplicationIdAndIsDrugLicenceVerification(finalRejectPayload.getApplicationId(), true);
		}
		
		else if(!finalRejectPayload.isAggrementVerification()) {
//			List<AdminKendraApplications> applicationList = adminKendraRepo
//					.findAllByApplicationIdAndIsAggrementVerification(finalRejectPayload.getApplicationId(), true);
//			if (applicationList != null) {
//				adminKendraRepo.deleteAllInBatch(applicationList);
//			}
			 docVerification = adminKendraRepo
					.findByApplicationIdAndIsAggrementVerification(finalRejectPayload.getApplicationId(), true);
		}
		
			if (docVerification != null) {
				docVerification.setDocumentVerification(finalRejectPayload.isDocumentVerification());
				docVerification.setIntitailApprovalWithDSC(finalRejectPayload.isIntitailApprovalWithDSC());	
				docVerification.setDrugLicenceVerification(finalRejectPayload.isDrugLicenceVerification());
				docVerification.setAggrementVerification(finalRejectPayload.isAggrementVerification());
				docVerification.setFinalApprovalWithDSC(false);
				
				docVerification.setApplicationFinalApproval(0);
				docVerification.setApplicationStatus(2);
				docVerification.setLatestApplication(true);
				docVerification.setRoleId(finalRejectPayload.getRoleId());
				docVerification.setAdminId(finalRejectPayload.getAdminId());;
				docVerification.setRejectedReason(finalRejectPayload.getRejectReason());
				
				AdminUpdateKendraRequest kendraReject=new AdminUpdateKendraRequest();
				kendraReject.setApplicationId(docVerification.getApplicationId());
				kendraReject.setAdminId(finalRejectPayload.getAdminId());
				kendraReject.setRoleId(finalRejectPayload.getRoleId());
				kendraReject.setRejectReason(finalRejectPayload.getRejectReason());
				kendraReject.setApplicationStatus(2);
				kendraReject.setDocumentVerification(finalRejectPayload.isDocumentVerification());
				kendraReject.setIntitailApprovalWithDSC(finalRejectPayload.isIntitailApprovalWithDSC());	
				kendraReject.setDrugLicenceVerification(finalRejectPayload.isDrugLicenceVerification());
				kendraReject.setAggrementVerification(finalRejectPayload.isAggrementVerification());
				kendraReject.setFinalApprovalWithDSC(false);
				Map<String, Object> rejectKendra=kendraService.kedraFinalRejectByL4Admin(kendraReject);
				if(!rejectKendra.containsValue("200")) {
					return rejectKendra;
				}
				adminKendraRepo.save(docVerification);
				map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				map.put(DataConstant.MESSAGE, DataConstant.APPLICATION_REJECTED);
				map.put(DataConstant.DATA, null);
				log.info("application rejected",DataConstant.APPLICATION_REJECTED);
				return map;
			}
		//}
		return null;
	}
}