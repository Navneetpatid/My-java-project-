package com.janaushadhi.adminservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.janaushadhi.adminservice.entity.AdminKendraApplications;
@Repository
public interface AdminKendraRepository extends JpaRepository<AdminKendraApplications, Long>{

	Optional<AdminKendraApplications> findByApplicationIdAndAdminId(String applicationId, Long adminId);

	Optional<AdminKendraApplications> findByApplicationIdAndAdminIdAndApplicationStatus(String applicationId,
			Long adminId, Integer applicationStatus);

	List<AdminKendraApplications> findByApplicationId(String applicationId);
	
	@Query(value="SELECT * FROM janaushadhi.admin_kendra_applications where application_id=:applicationId and role_id=:roleId ORDER BY created_date DESC LIMIT 1",nativeQuery=true)
	AdminKendraApplications findByApplicationIdAndRoleIdOrderByCreatedDateDesc(String applicationId, Long roleId);

	List<AdminKendraApplications> findByIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdIn(boolean b,
			List<Long> stateIdList, List<Long> districtIdList);

	List<AdminKendraApplications> findByIsIntitailApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdIn(boolean b,
			List<Long> stateIdList, List<Long> districtIdList);

	AdminKendraApplications findByApplicationIdAndApplicationStatus(String applicationId, int i);

	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdIn(boolean b,
			List<Long> stateIdList, List<Long> districtIdList);

	List<AdminKendraApplications> findByIsAggrementVerificationAndKendraStateIdInAndKendraDistrictIdIn(boolean b,
			List<Long> stateIdList, List<Long> districtIdList);

	List<AdminKendraApplications> findByIsDrugLicenceVerificationAndKendraStateIdInAndKendraDistrictIdIn(boolean b,
			List<Long> stateIdList, List<Long> districtIdList);

	List<AdminKendraApplications> findByIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean i);

	List<AdminKendraApplications> findByIsIntitailApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean i);

	List<AdminKendraApplications> findAllByApplicationId(String applicationId);

	List<AdminKendraApplications> findByIsDrugLicenceVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c);

	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c);

	List<AdminKendraApplications> findByIsAggrementVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c);

	AdminKendraApplications findByApplicationIdAndIsLatestApplication(String applicationId, boolean b);
	
	@Query(value="SELECT * FROM janaushadhi.admin_kendra_applications where application_id=:applicationId and is_document_verification=:b ORDER BY created_date ASC LIMIT 1",nativeQuery=true)
	AdminKendraApplications findByApplicationIdAndIsDocumentVerification(String applicationId, boolean b);
	
	@Query(value="SELECT * FROM janaushadhi.admin_kendra_applications where application_id=:applicationId and is_intitail_approval_withdsc=:b ORDER BY created_date ASC LIMIT 1",nativeQuery=true)
	AdminKendraApplications findByApplicationIdAndIsIntitailApprovalWithDSC(String applicationId, boolean b);

	@Query(value="SELECT * FROM janaushadhi.admin_kendra_applications where application_id=:applicationId and is_final_approval_withdsc=:b ORDER BY created_date ASC LIMIT 1",nativeQuery=true)
	AdminKendraApplications findByApplicationIdAndIsFinalApprovalWithDSC(String applicationId, boolean b);

	List<AdminKendraApplications> findByAdminIdAndApplicationStatusOrderByCreatedDate(Long adminId,
			Integer applicationStatus);
	List<AdminKendraApplications> findByAdminIdAndApplicationStatusOrderByCreatedDateDesc(Long adminId,
			Integer applicationStatus);
	List<AdminKendraApplications> findByAdminIdOrderByCreatedDate(Long adminId);
	List<AdminKendraApplications> findByAdminIdOrderByCreatedDateDesc(Long adminId);

	List<AdminKendraApplications> findByCategoryAndIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
			String category, boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdAndIsDocumentVerificationAndIsLatestApplication(
			String category, Long stateId, boolean b, boolean c);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdAndKendraDistrictIdAndIsDocumentVerificationAndIsLatestApplication(
			String category, Long stateId, Long districtId, boolean b, boolean c);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdAndKendraDistrictIdAndKendraBlockIdAndIsDocumentVerificationAndIsLatestApplication(
			String category, Long stateId, Long districtId, Long blockId, boolean b, boolean c);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplication(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b, boolean c);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplication(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b, boolean c);

	List<AdminKendraApplications> findByAdminIdAndIsDocumentVerificationAndApplicationStatus(Long adminId, boolean b,
			int i);

	List<AdminKendraApplications> findByIsIntitailApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsDrugLicenceVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsAggrementVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c, Integer[] status);

	List<AdminKendraApplications> findByCategoryAndAdminIdAndApplicationStatusOrderByCreatedDate(String category,
			Long adminId, Integer applicationStatus);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
			String category, List<Long> requestStateIdList, List<Long> requestStateIdList2, Long adminId,
			Integer applicationStatus);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndApplicationStatusOrderByCreatedDate(
			List<Long> requestStateIdList, List<Long> requestStateIdList2, Long adminId, Integer applicationStatus);

	List<AdminKendraApplications> findByIsDocumentVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplication(
			boolean b, String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean c);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdAndIsDocumentVerificationAndApplicationStatus(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, Long adminId, boolean b,
			int i);

	List<AdminKendraApplications> findByIsDocumentVerificationAndCategoryAndIsLatestApplication(boolean b,
			String category, boolean c);

	List<AdminKendraApplications> findByCategoryAndAdminIdAndIsDocumentVerificationAndApplicationStatus(String category,
			Long adminId, boolean b, int i);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndAdminIdAndIsDocumentVerificationAndApplicationStatus(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList, Long adminId, boolean b, int i);

	List<AdminKendraApplications> findByIsIntitailApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean c,
			Integer[] status);

	List<AdminKendraApplications> findByIsIntitailApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, boolean c, Integer[] status);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, Long adminId);

	List<AdminKendraApplications> findByCategoryAndAdminIdOrderByCreatedDate(String category, Long adminId);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndAdminIdOrderByCreatedDate(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList, Long adminId);
	
	@Query(value="SELECT * FROM janaushadhi.admin_kendra_applications where application_id=:applicationId and is_drug_licence_verification=:b ORDER BY created_date ASC LIMIT 1",nativeQuery=true)
	AdminKendraApplications findByApplicationIdAndIsDrugLicenceVerification(String applicationId, boolean b);
	
	@Query(value="SELECT * FROM janaushadhi.admin_kendra_applications where application_id=:applicationId and is_aggrement_verification=:b ORDER BY created_date ASC LIMIT 1",nativeQuery=true)
	AdminKendraApplications findByApplicationIdAndIsAggrementVerification(String applicationId, boolean b);

	List<AdminKendraApplications> findByIsDrugLicenceVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean c,
			Integer[] status);

	List<AdminKendraApplications> findByIsDrugLicenceVerificationAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsAggrementVerificationAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, List<Long> stateIdList, List<Long> districtIdList, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsAggrementVerificationAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean c,
			Integer[] status);

	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String category, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, boolean c, Integer[] status);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatus(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b,
			Integer applicationStatus);

	List<AdminKendraApplications> findByCategoryAndIsLatestApplicationAndApplicationStatusOrderByCreatedDate(
			String category, boolean b, Integer applicationStatus);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusOrderByCreatedDate(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList,boolean b, Integer applicationStatus);

	List<AdminKendraApplications> findByIsLatestApplicationAndApplicationStatusOrderByCreatedDate(boolean b,
			Integer applicationStatus);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusIn(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b,
			Integer[] appStatus);

	List<AdminKendraApplications> findByCategoryAndIsLatestApplicationAndApplicationStatusIn(String category, boolean b,
			Integer[] appStatus);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusIn(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b, Integer[] appStatus);

	List<AdminKendraApplications> findByIsLatestApplicationAndApplicationStatusIn(boolean b, Integer[] appStatus);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplicationAndApplicationStatusNotIn(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b, boolean c,
			Integer[] status);

	List<AdminKendraApplications> findByCategoryAndIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			String category, boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c, Integer[] status);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndIsDocumentVerificationAndIsLatestApplicationAndApplicationStatusNotIn(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b, boolean c, Integer[] status);

	List<AdminKendraApplications> findByIsDocumentVerificationAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, List<Long> stateIdList, List<Long> districtIdList, boolean c, Integer[] status);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b,
			Integer applicationStatus, boolean c);

	List<AdminKendraApplications> findByCategoryAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
			String category, boolean b, Integer applicationStatus, boolean c);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b, Integer applicationStatus,
			boolean c);

	List<AdminKendraApplications> findByIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(boolean b,
			Integer applicationStatus, boolean c);

	AdminKendraApplications findByApplicationIdAndIsDocumentVerificationAndApplicationStatus(String applicationId,
			boolean b, int i);

	List<AdminKendraApplications> findByCategoryAndKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
			String category, List<Long> requestStateIdList, List<Long> requestDistrictIdList, Integer superAdminRoleId,
			Integer applicationStatus);

	List<AdminKendraApplications> findByCategoryAndRoleIdAndApplicationStatusOrderByCreatedDate(String category,
			Integer superAdminRoleId, Integer applicationStatus);

	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
			List<Long> requestStateIdList, List<Long> requestDistrictIdList, Integer superAdminRoleId,
			Integer applicationStatus);

	List<AdminKendraApplications> findByRoleIdAndApplicationStatusOrderByCreatedDate(Integer superAdminRoleId,
			Integer applicationStatus);


	AdminKendraApplications findByApplicationIdAndIsIntitailApprovalWithDSCAndIsDrugLicenceVerificationAndIsLatestApplicationAndApplicationStatus(
			String applicationId, boolean b, boolean c, boolean d, int i);

	AdminKendraApplications findByApplicationIdAndIsDrugLicenceVerificationAndIsAggrementVerificationAndIsLatestApplicationAndApplicationStatus(
			String applicationId, boolean b, boolean c, boolean d, int i);

	@Query(value="SELECT * FROM janaushadhi.admin_kendra_applications where application_id=:applicationId ORDER BY created_date DESC LIMIT 1",nativeQuery=true)
	AdminKendraApplications findByApplicationIdAndLatest(String applicationId);

	List<AdminKendraApplications> findByAdminIdAndIsFinalApprovalWithDSCOrderByCreatedDate(Long adminId, boolean b);

	// 05-07-2024 // for categorywise access
	
	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndSubCategoryAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String subCategory, boolean c, Integer[] status);

	List<AdminKendraApplications> findBySubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
			String subCategory, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b,
			Integer applicationStatus, boolean c);

	List<AdminKendraApplications> findBySubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatus(
			String subCategory, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b,
			Integer applicationStatus);

	List<AdminKendraApplications> findBySubCategoryAndIsLatestApplicationAndApplicationStatusAndIsFinalApprovalWithDSC(
			String subCategory, boolean b, Integer applicationStatus, boolean c);

	List<AdminKendraApplications> findByIsFinalApprovalWithDSCAndSubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusNotIn(
			boolean b, String subCategory, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean c,
			Integer[] status);

	List<AdminKendraApplications> findBySubCategoryAndIsLatestApplicationAndApplicationStatusOrderByCreatedDate(
			String subCategory, boolean b, Integer applicationStatus);

	List<AdminKendraApplications> findBySubCategoryAndKendraStateIdInAndKendraDistrictIdInAndIsLatestApplicationAndApplicationStatusIn(
			String subCategory, List<Long> requestStateIdList, List<Long> requestDistrictIdList, boolean b,
			Integer[] appStatus);

	List<AdminKendraApplications> findBySubCategoryAndIsLatestApplicationAndApplicationStatusIn(String subCategory,
			boolean b, Integer[] appStatus);

	List<AdminKendraApplications> findByAdminIdAndIsLatestApplication(Long adminId, boolean b);

	List<AdminKendraApplications> findAllByApplicationIdAndIsIntitailApprovalWithDSC(String applicationId, boolean b);

	List<AdminKendraApplications> findAllByApplicationIdAndIsDrugLicenceVerification(String applicationId, boolean b);

	List<AdminKendraApplications> findAllByApplicationIdAndIsAggrementVerification(String applicationId, boolean b);

//	List<AdminKendraApplications> findByKendraStateIdInAndKendraDistrictIdInAndRoleIdAndApplicationStatusOrderByCreatedDate(
//			List<Long> stateIdList, List<Long> districtIdList, Integer superAdminRoleId, Integer applicationStatus);
	

}
