package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class ApplicationStatusResponse {
	
	private Integer applicationIsVerified;
	private String appVerifyDate;
	private Integer intialLatterIsGenerated;
	private Integer drugLicense;
	private String drugLicenseDate;
	private Integer drugAgreement;
	private String drugAgreementDate;
	private String intialLatterDate;
	private Integer filanApproval;
	private String filanApprovalDate;
	private String rejectReason;
}
