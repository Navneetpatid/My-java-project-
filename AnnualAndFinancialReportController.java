package com.janaushadhi.adminservice.controller;
import java.util.Map;

import com.janaushadhi.adminservice.responsepayload.GetAllDeleteReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.janaushadhi.adminservice.requestpayload.AnnualAndFinancialRequestPayload;
import com.janaushadhi.adminservice.responsepayload.GetAllReport;
import com.janaushadhi.adminservice.service.AnnualAndFinancialReportService;

@RestController
@RequestMapping("/api/v1/admin/annualAndFinancial")
public class AnnualAndFinancialReportController {
	
	@Autowired
	private AnnualAndFinancialReportService annualAndFinancialService;
	
	@PostMapping(value = "/addAnnualAndFinancialReport")
    public Map<String, Object> addAnnualAndFinancialReport(@RequestBody AnnualAndFinancialRequestPayload requestPayload) {
        return  annualAndFinancialService.addAnnualAndFinancialReport(requestPayload) ;
    }
	
	@PostMapping(value = "/getAllReportByType")
    public Map<String, Object> getAllReportByType(@RequestBody GetAllReport reportRequest) {
        return  annualAndFinancialService.getAllReportByType(reportRequest) ;
    }

    @PostMapping(value = "/getAllDeleteReportByType")
    public Map<String, Object> getAllDeleteReportByType(@RequestBody GetAllDeleteReport reportRequest) {
        return  annualAndFinancialService.getAllDeleteReportByType(reportRequest) ;
    }
}
