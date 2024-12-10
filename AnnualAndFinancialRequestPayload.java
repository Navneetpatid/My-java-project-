package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class AnnualAndFinancialRequestPayload {
	private Long id;
	private String title;
	private String discription;
	private String reportType;
	private String docFile;
	private Integer status;
}
