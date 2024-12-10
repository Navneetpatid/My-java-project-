package com.janaushadhi.adminservice.responsepayload;

import java.util.List;

import lombok.Data;

@Data
public class AdminStateResponsePayload {
	private Long stateId;
    private List<Long> districtId;
}
