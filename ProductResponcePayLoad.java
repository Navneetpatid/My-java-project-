package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class ProductResponcePayLoad {

	private Long productId;
	private String genericName;
	private String groupName;
	private String drugCode;
	private String unitSize;
	private Double mrp;
	private Short status;

}
