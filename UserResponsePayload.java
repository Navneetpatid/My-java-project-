package com.janaushadhi.adminservice.responsepayload;

import java.util.Date;

import lombok.Data;

@Data
public class UserResponsePayload {
	  private Long id;
	    private String userId;
	    private String name;
	    private String email;
	  
	    private String mobileNumber;
	  //  private String captcha;
	    private Integer roleId;
	    private Date createdDate;
	    private Date updatedDate;
}
