package com.janaushadhi.adminservice.service;

import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.AnnualAndFinancialRequestPayload;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteReport;
import com.janaushadhi.adminservice.responsepayload.GetAllReport;

public interface AnnualAndFinancialReportService {

	Map<String, Object> addAnnualAndFinancialReport(AnnualAndFinancialRequestPayload requestPayload);

	Map<String, Object> getAllReportByType(GetAllReport reportRequest);
	Map<String, Object> getAllDeleteReportByType(GetAllDeleteReport reportRequest);

}
