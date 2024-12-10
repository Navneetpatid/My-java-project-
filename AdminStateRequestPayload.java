package com.janaushadhi.adminservice.requestpayload;

import java.util.List;

import lombok.Data;

@Data
public class AdminStateRequestPayload {
	private List<Integer> stateIds;

}
