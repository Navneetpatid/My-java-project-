package com.janaushadhi.adminservice.requestpayload;


import java.util.List;

import lombok.Data;

@Data
public class ContactsDeletePayload {
	private List<Long> contactIds;
}
