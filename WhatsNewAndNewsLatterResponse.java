package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class WhatsNewAndNewsLatterResponse implements Comparable<WhatsNewAndNewsLatterResponse> {
	private Long id;
	private String title;
	private String discription;
	private String type;
	private String docFile;
	private String image;
	private Integer status;
	private String createdDate;
	private String updatedDate;
	private Integer serialNo;
	
	@Override
	public int compareTo(WhatsNewAndNewsLatterResponse otherApplication) {
		return -this.getCreatedDate().compareTo(otherApplication.getCreatedDate());//(- minus used for desending order)
	}

}
