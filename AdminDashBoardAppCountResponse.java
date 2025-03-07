package com.janaushadhi.adminservice.responsepayload;
Subject: Urgent Leave Request for [Date]

Dear Team,

I hope you are doing well. I wanted to inform you that I will be on leave for one day on [Date] due to some urgent family matters that require me to travel.

I apologize for the short notice and will ensure that all pending tasks are managed before my leave. Please feel free to reach out if anything urgent comes up.

Thank you for your understanding.

Best regards,
[Your Name]
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
