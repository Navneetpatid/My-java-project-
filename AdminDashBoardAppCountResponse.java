package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class AdminDashBoardAppCountResponse {
	
		private Long totalApplication;
		private Long pendingApplication;
		private Long approveApplication;
		private Long rejectApplication;
		private Long holdApplication;
		private Long totalUser;
		private Long totalPayment;
		
}
