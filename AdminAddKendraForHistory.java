package com.janaushadhi.adminservice.requestpayload;

import java.util.List;

import lombok.Data;

@Data
public class AdminAddKendraForHistory {
	
	private String applicationId;
	private String nameOfApplicant;
	private String nameOfOrganization;
	private String category;
	private Long kendraStateId;
	private Long kendraDistrictId;
	private Long kendraBlockId;
	private Integer applicationStatus;
	//04-07-2024 for login access for categorywise
    private String subCategory;
    private List<String> partyNames;
//	private String latitude;
//	private String longitude;
}
