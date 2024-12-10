package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class ApplicationFinalRejectPayload {

		private String applicationId;
		private String rejectReason;
		private Integer roleId;
		private Long adminId;
		private boolean isDocumentVerification;
	    private boolean isDrugLicenceVerification;
	    private boolean isAggrementVerification;
	    private boolean isIntitailApprovalWithDSC; 
}
