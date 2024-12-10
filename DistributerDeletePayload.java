package com.janaushadhi.adminservice.requestpayload;


import java.util.List;

import lombok.Data;

@Data
public class DistributerDeletePayload {
	private List<Long> distributerIds;
}
