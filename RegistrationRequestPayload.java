package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestPayload {
	 private String userId;
	    private String name;
	    private String email;
	    private String password;
	    private String mobileNumber;
	    //private String otp;
	    private String captcha;
	    private Integer roleId;
	  //  private Short isVerified;
}
