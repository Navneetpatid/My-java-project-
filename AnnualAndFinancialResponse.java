package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class AnnualAndFinancialResponse implements Comparable<AnnualAndFinancialResponse> {
	private Long id;
	private String title;
	private String discription;
	private String reportType;
	private String docFile;
	private Integer status;
	private String createdDate;
	private String updatedDate;
	private Integer serialNo;
	
	@Override
	public int compareTo(AnnualAndFinancialResponse otherApplication) {
		 return -this.getCreatedDate().compareTo(otherApplication.getCreatedDate());//(- minus used for desending order)
	}
}
