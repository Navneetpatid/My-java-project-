package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageHindiRequest {

	private String name;

	private String designation;

	private String title;

	private String description;

}
