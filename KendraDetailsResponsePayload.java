package com.janaushadhi.adminservice.responsepayload;

import java.util.Date;
import java.util.List;

import com.janaushadhi.adminservice.requestpayload.PartyNameModelRequestPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KendraDetailsResponsePayload {

	private Long id;
	private String userId;
	private String applicationCategory;
	private String applicationSubCategory;
	private String castCertificateFile;
	private String nameOfApplicant;
	private String mobileNo;
	private String alternateContactNo;
	private String emailId;
	private String dateOfBirth;
	private String gender;
	private String aadharNo;
	private String panNo;
	private String latitude;
	private String longitude;
	private String permanentAddress;
//	private String state;
//	private String district;
//	private String block;
	private Long stateId;
	private Long districtId;
	private Long blockId;
	private String pinCode;
	private String additionalDocumentFile;
	private Date creationDate;
	private Date updationDate;
	private Boolean isDeleted;
	private String applicationId;
//	@ElementCollection
private List<PartyNameModelRequestPayload> partyNames;
	//	private List<String> partyNames;
	private String gstNumber;
	private String nameOfOrganization;
	private String pacsRegistrationNumber;
	private String pacsRegistrationDoc;
	private String paymentStatus;
	private boolean isChecked = false;
	private Integer roleId;
	private Long adminId;
	private Integer applicationStatus;
	
	//Permission Approval
	private boolean isDocumentVerification;
//    private boolean isApplicationStatus;
    private boolean isDrugLicenceVerification;
    private boolean isAggrementVerification;
    private boolean isIntitailApprovalWithDSC;
    private boolean isFinalApprovalWithDSC;
    private Integer applicationFinalApproval;
    private String finalKendraCode;
//	private ProposedKendraRequestPayload proposedKendraRequestPayload;
	
	//private ProposedKendraDetail proposedKendraDetail;
//	private CheckNumber checkNumber;
//	private ChequeNumbersRequestPayload checkNumber;
//	private DrugLicenseDetails drugLicenseDetails;
//	private DrugLicenseDetailsPayload drugLicenseDetails;
	private String finalApprovalLetter;
	//10-07-2024
	private String digitalSignFinalApprovalLetter;
	//private String storeCode;
	private  boolean gstChecked;
}
