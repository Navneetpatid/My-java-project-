package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductRequestPayload {

	private Long productId;
	private String genericName;
	private String groupName;
	private Integer drugCode;
	private String unitSize;
	private Double mrp;
}


