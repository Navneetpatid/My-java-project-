package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

@Data
public class WhatsNewAndNewsLatterRequestPayload {
	private Long id;
	private String title;
	private String discription;
	private String type;
	private String docFile;
	private String image;
	private Integer status;
}
