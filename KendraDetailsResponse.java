package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  KendraDetailsResponse {


	  	private Long id;
	  	private String userId;
	    private String applicationCategory;
	    private String applicationSubCategory;
	    private String castCertificateFile;
	    private String nameOfApplicant;
	    private String mobileNo;
	    private String alternateContactNo;
	    private String emailId;
	    private String gender;
	    private String aadharNo;
	    private String panNo;
	    private String latitude;
	    private String longitude;
	    private String proposedFile;
	    private String additionalDocumentFile;
	    private Boolean isDeleted;
	   
		private String dateOfBirth;
		private String permanentAddress;
		private Long stateId;
		private Long districtId;
		private Long blockId;
		private String pinCode;
		private String creationDate;
		private String updationDate;
		private String applicationId;
//		private List<PartyNameModelRequestPayload> partyNames;
		private String gstNumber;
		private String nameOfOrganization;
		private String pacsRegistrationNumber;
		private String pacsRegistrationDoc;
		private String paymentStatus;
		private boolean isChecked = false;
		private Integer roleId;
		private Long adminId;
		private Integer applicationStatus;
		private String rejectReason;
		
		//Permission approval
		private boolean isDocumentVerification;
//	    private boolean isApplicationStatus;
	    private boolean isDrugLicenceVerification;
	    private boolean isAggrementVerification;
	    private boolean isIntitailApprovalWithDSC;
	    private boolean isFinalApprovalWithDSC;
	    private Integer applicationFinalApproval;
	    
		private String documentVerificationDate;
		private String intitailApprovalDate;
		private String drugLicenceVerificationDate;
		private String aggrementVerificationDate;
		private String finalApprovalDate;
		//10-07-2024
		private String digitalSignFinalApprovalLetter;
		
//	    private ProposedKendraDetail proposedKendraDetail;
//	    private CheckNumber checkNumber;
//	    private DrugLicenseDetails drugLicenseDetails;
		private  boolean gstChecked;

}
