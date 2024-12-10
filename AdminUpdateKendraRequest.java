package com.janaushadhi.adminservice.requestpayload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateKendraRequest {

//	private String applicationId;

	private String applicationId;
	private Long kendraId;
	private Integer roleId;
	private String nameOfApplicant;
	private String nameOfOrganization;
	
	private String category;
	private String subCategory;
	private Long kendraStateId;
	private Long kendraDistrictId;
	private Long kendraBlockId;
	//Permission Approval
	private boolean isDocumentVerification;
//    private boolean isApplicationStatus;
    private boolean isDrugLicenceVerification;
    private boolean isAggrementVerification;
    private boolean isIntitailApprovalWithDSC;
    private boolean isFinalApprovalWithDSC;
	//
	
//	private boolean initialLatterIsGenarate = false;
//	private boolean drugDetailVerified = false;
//	private boolean drugLicenseVerified = false;
//  private boolean isDrugLicenceVerification = false;
//  private boolean isAggrementverification = false;
//  private boolean isFinalApprovalWithDSC = false;
	
	private Long adminId;
	private Integer applicationStatus;
	private String rejectReason;
	private Integer applicationFinalApproval;
	private String finalKendraCode;
//	private boolean initialLatterIsGenarate;
}