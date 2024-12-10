package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class GetAllReport {
	
	private Integer pageIndex;
	private Integer pageSize;
	private String reportType;
	private String title;
}
