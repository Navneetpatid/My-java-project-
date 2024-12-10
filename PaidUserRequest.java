package com.janaushadhi.adminservice.requestpayload;

import java.util.List;

import lombok.Data;

@Data
public class PaidUserRequest {
	private String paymentStatus;
	private String category;
	private String subCategory;
	private List<Long> stateId;
	private List<Long> districtId;
}
