package com.janaushadhi.adminservice.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AdminKendraApplications {
		
		@Id
	    @GeneratedValue(strategy =  GenerationType.IDENTITY)
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
		
		
		private Integer applicationStatus;
		private boolean isLatestApplication;
		private Integer applicationFinalApproval;
		private String finalKendraCode;
		private String rejectedReason;
		private Date createdDate;
		private Date updated;
}
