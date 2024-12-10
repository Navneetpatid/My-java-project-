package com.janaushadhi.adminservice.requestpayload;
import java.util.List;

import com.janaushadhi.adminservice.responsepayload.AdminStateResponsePayload;

import lombok.Data;

@Data
public class AdminApplicationRequest {
	private String category;
	private String subCategory;
//	private Long stateId;
//	private Long districtId;
//	private Long blockId;
	private Long adminId;
	private Integer roleId;
	private Integer applicationStatus;
	 private List<AdminStateResponsePayload> adminStateMapping;
	private Integer pageNo;
	private Integer pageSize;
	//fromdate todate 10-07-2024
	private String fromDate;
	private String toDate;
	private String serachText;
}
