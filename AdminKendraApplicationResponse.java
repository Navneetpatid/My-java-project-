package com.janaushadhi.adminservice.responsepayload;

import java.util.List;

import lombok.Data;
@Data
public class AdminKendraApplicationResponse implements Comparable<AdminKendraApplicationResponse>{
	
	 private Long id;
	    private Integer  roleId;
	    private String roleName;
	    private String applicationId;
	    private Long kendraId;
	    private String category;
	    private Long kendraStateId;
		private Long kendraDistrictId;
		private Long kendraBlockId;
	    private String nameOfApplicant;
		private String nameOfOrganization;
		private Long adminId;
		//04-07-2024 for login access for categorywise
	    private String subCategory;
	    private List<String> partyNames;
//		private String latitude;
//		private String longitude;
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
		private Integer serialNo;
		@Override
		public int compareTo(AdminKendraApplicationResponse otherApplication) {
			 return -this.getCreatedDate().compareTo(otherApplication.getCreatedDate());//(- minus used for desending order)
		}
}
