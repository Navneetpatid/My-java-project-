package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class AdminDistrictPayload {
	private Long districtId;
	private Long stateId;
    private Long adminId;
}
