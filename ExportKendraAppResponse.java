package com.janaushadhi.adminservice.responsepayload;

import com.janaushadhi.adminservice.requestpayload.PartyNameModelRequestPayload;
import lombok.Data;

import java.util.List;

@Data
public class ExportKendraAppResponse {
	private Long id;
    private Integer  roleId;
    private String roleName;
    private String applicationId;
    private Long kendraId;
    private String category;
//    private Long kendraStateId;
//	private Long kendraDistrictId;
//	private Long kendraBlockId;
	private String latitude;
	private String longitude;
	private List<PartyNameModelRequestPayload> partyNames;

	private DistrictOfIndiaResponse userStateDistrict;
    private String nameOfApplicant;
	private String nameOfOrganization;
	private Long adminId;
	
	//Permission Approval
	private boolean isDocumentVerification;
    private boolean isDrugLicenceVerification;
    private boolean isAggrementVerification;
    private boolean isIntitailApprovalWithDSC;
    private boolean isFinalApprovalWithDSC;
	//
	

	private Integer applicationStatus;
	private boolean isLatestApplication;
	private Integer applicationFinalApproval;
	private String rejectedReason;
	private String createdDate;
	private String updated;
	
	private DistrictOfIndiaResponse proposedStateDistrict;
	private String fieldOfficer;
	private String mobileNo;//applicant no.
}
