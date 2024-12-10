package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class UpdateAdmin {
	private String email;
	private Integer roleId;
}