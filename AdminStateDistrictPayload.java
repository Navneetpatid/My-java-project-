package com.janaushadhi.adminservice.requestpayload;

import java.util.List;

import lombok.Data;

@Data
public class AdminStateDistrictPayload {

	private Long stateId;
	private List<Long> districtList;
}
