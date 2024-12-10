package com.janaushadhi.adminservice.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.janaushadhi.adminservice.responsepayload.GetAllDeleteReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.janaushadhi.adminservice.entity.AnnualAndFinancialReport;
import com.janaushadhi.adminservice.repository.AnnualAndFinancialReportRepository;
import com.janaushadhi.adminservice.requestpayload.AnnualAndFinancialRequestPayload;
import com.janaushadhi.adminservice.responsepayload.AnnualAndFinancialReportPage;
import com.janaushadhi.adminservice.responsepayload.AnnualAndFinancialResponse;
import com.janaushadhi.adminservice.responsepayload.GetAllReport;
import com.janaushadhi.adminservice.service.AnnualAndFinancialReportService;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.DateUtil;

@Service
@Slf4j
public class AnnualAndFinancialReportServiceImpl implements AnnualAndFinancialReportService {

	@Autowired
	private AnnualAndFinancialReportRepository annualAndFinancialRepo;

	@Override
	public Map<String, Object> addAnnualAndFinancialReport(AnnualAndFinancialRequestPayload requestPayload) {
		Map<String, Object> map = new HashMap<>();
		AnnualAndFinancialReport report = new AnnualAndFinancialReport();
		if (requestPayload.getId() != null && requestPayload.getId() != 0) {
			report = annualAndFinancialRepo.findById(requestPayload.getId()).orElse(null);
			if (report != null) {
				if (requestPayload.getTitle() != null && !requestPayload.getTitle().trim().isEmpty()) {
					report.setTitle(requestPayload.getTitle());
				}
				if (requestPayload.getDiscription() != null && !requestPayload.getDiscription().trim().isEmpty()) {
					report.setDiscription(requestPayload.getDiscription());
				}
				if (requestPayload.getDocFile() != null && !requestPayload.getDocFile().trim().isEmpty()) {
					report.setDocFile(requestPayload.getDocFile());
				}
				if (requestPayload.getStatus() != null) {
					report.setStatus(requestPayload.getStatus());
				}
				report.setUpdatedDate(new Date());
				annualAndFinancialRepo.save(report);
				map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				map.put(DataConstant.MESSAGE, DataConstant.UPDATED_SUCCESSFULLY);
				map.put(DataConstant.RESPONSE_CODE, report);
				log.info("AnnualAndFinancialReport updated successfully",DataConstant.UPDATED_SUCCESSFULLY);
				return map;
			}
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
			map.put(DataConstant.RESPONSE_CODE, null);
			log.info("AnnualAndFinancialReport not found",DataConstant.RECORD_NOT_FOUND_MESSAGE);

			return map;
		}
		BeanUtils.copyProperties(requestPayload, report);
		report.setCreatedDate(new Date());
		report.setStatus(1);
		if(requestPayload.getReportType().equals(DataConstant.ANNUAL) || requestPayload.getReportType().equals(DataConstant.FINANCIAL)) {
			report.setReportType(requestPayload.getReportType());
		}else {
			map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
			map.put(DataConstant.MESSAGE, DataConstant.INVALID_REPORT_TYPE);
			map.put(DataConstant.RESPONSE_BODY, null);
			log.info("AnnualAndFinancialReport invaild report type",DataConstant.INVALID_REPORT_TYPE);

			return map;	
		}
		annualAndFinancialRepo.save(report);
		map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
		map.put(DataConstant.MESSAGE, DataConstant.RECORD_ADDED_SUCCESSFULLY);
		map.put(DataConstant.RESPONSE_CODE, report);
		log.info("AnnualAndFinancialReport added  successfully",DataConstant.RECORD_ADDED_SUCCESSFULLY);

		return map;
	}




	@Override
	public Map<String, Object> getAllReportByType(GetAllReport reportRequest) {
		Map<String, Object> ReportByTypeMap = new HashMap<>();
		AnnualAndFinancialReportPage responsePage = new AnnualAndFinancialReportPage();
		List<AnnualAndFinancialResponse> responseList = new ArrayList<>();
		if (reportRequest.getPageIndex() != null && reportRequest.getPageSize() != null && reportRequest.getPageSize() > 0 && reportRequest.getReportType() != null
				&& !reportRequest.getReportType().trim().isEmpty()) {
//			List<AnnualAndFinancialReport> repostList=null;
			Page<AnnualAndFinancialReport> repostList=null;
			Pageable pageable = PageRequest.of(reportRequest.getPageIndex(), reportRequest.getPageSize());
			if(reportRequest.getTitle()!=null && !reportRequest.getTitle().trim().isEmpty()) {
				repostList = annualAndFinancialRepo.findAllByReportTypeAndTitleContainingIgnoreCaseAndStatusOrderByIdDesc(reportRequest.getReportType(),reportRequest.getTitle(),1,pageable);
			}else {
			 repostList = annualAndFinancialRepo.findAllByReportTypeAndStatusOrderByIdDesc(reportRequest.getReportType(),1,pageable);
			}if (repostList.getContent() != null) {
				int index = 0;
				for (AnnualAndFinancialReport report : repostList.getContent()) {
					AnnualAndFinancialResponse response = new AnnualAndFinancialResponse();
					BeanUtils.copyProperties(report, response);
					response.setCreatedDate(DateUtil.convertUtcToIst(report.getCreatedDate()));
					if (report.getUpdatedDate() != null) {
						response.setUpdatedDate(DateUtil.convertUtcToIst(report.getUpdatedDate()));
					}
					// for frontEnd team pagination
					if (reportRequest.getPageIndex() == 0) {
						response.setSerialNo(index + 1);
						index++;
					} else {
						response.setSerialNo((reportRequest.getPageIndex() * reportRequest.getPageSize()) + (index + 1));
						index++;
					}
					responseList.add(response);
				}
				Collections.sort(responseList);
				//Pageable pageable = PageRequest.of(reportRequest.getPageIndex(), reportRequest.getPageSize());
//				PagedListHolder<AnnualAndFinancialResponse> page = new PagedListHolder<>(responseList);
//				page.setPageSize(pageable.getPageSize()); // number of items per page
//				page.setPage(pageable.getPageNumber());
				
				responsePage.setAnnualAndFinancialResponseList(responseList);
				responsePage.setPageIndex(repostList.getNumber());
				responsePage.setPageSize(repostList.getSize());
				responsePage.setTotalElement(repostList.getTotalElements());
				responsePage.setTotalPages(repostList.getTotalPages());
				responsePage.setIsLastPage(repostList.isLast());
				responsePage.setIsFirstPage(repostList.isFirst());

				ReportByTypeMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
				ReportByTypeMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
				ReportByTypeMap.put(DataConstant.RESPONSE_BODY, responsePage);
				log.info("AnnualAndFinancialReport recored found",DataConstant.RECORD_FOUND_MESSAGE);

				return ReportByTypeMap;

			}
			ReportByTypeMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			ReportByTypeMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
			ReportByTypeMap.put(DataConstant.RESPONSE_BODY, responsePage);
			log.info("AnnualAndFinancialReport recored  not found",DataConstant.RECORD_NOT_FOUND_MESSAGE);

			return ReportByTypeMap;
		}
		ReportByTypeMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		ReportByTypeMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_AND_REPORT_TYPE_CANT_NULL);
		ReportByTypeMap.put(DataConstant.RESPONSE_BODY, null);
		log.info(DataConstant.PAGE_SIZE_AND_INDEX_AND_REPORT_TYPE_CANT_NULL);

		return ReportByTypeMap;

	}





	public Map<String, Object> getAllDeleteReportByType(GetAllDeleteReport reportRequest) {
		Map<String, Object> deleteReportMap = new HashMap<>();
		AnnualAndFinancialReportPage responsePage = new AnnualAndFinancialReportPage();
		List<AnnualAndFinancialResponse> responseList = new ArrayList<>();
		if (reportRequest.getPageIndex() != null && reportRequest.getPageSize() != null && reportRequest.getPageSize() > 0 && reportRequest.getReportType() != null
				&& !reportRequest.getReportType().trim().isEmpty()) {
			//List<AnnualAndFinancialReport> repostList=null;
			Page<AnnualAndFinancialReport> repostList=null;
			Integer status= 2;
			Pageable pageable = PageRequest.of(reportRequest.getPageIndex(), reportRequest.getPageSize());
			repostList = annualAndFinancialRepo.findAllByReportTypeAndStatus(reportRequest.getReportType(),status,pageable);
		if (repostList.getContent() != null) {
				int index = 0;
				for (AnnualAndFinancialReport report : repostList.getContent()) {
					AnnualAndFinancialResponse response = new AnnualAndFinancialResponse();
					BeanUtils.copyProperties(report, response);
					response.setCreatedDate(DateUtil.convertUtcToIst(report.getCreatedDate()));
					if (report.getUpdatedDate() != null) {
						response.setUpdatedDate(DateUtil.convertUtcToIst(report.getUpdatedDate()));
					}
					// for frontEnd team pagination
					if (reportRequest.getPageIndex() == 0) {
						response.setSerialNo(index + 1);
						index++;
					} else {
						response.setSerialNo((reportRequest.getPageIndex() * reportRequest.getPageSize()) + (index + 1));
						index++;
					}
					responseList.add(response);
				}
//				Collections.sort(responseList);
//				Pageable pageable = PageRequest.of(reportRequest.getPageIndex(), reportRequest.getPageSize());
//				PagedListHolder<AnnualAndFinancialResponse> page = new PagedListHolder<>(responseList);
//				page.setPageSize(pageable.getPageSize()); // number of items per page
//				page.setPage(pageable.getPageNumber());
				responsePage.setAnnualAndFinancialResponseList(responseList);
				responsePage.setPageIndex(repostList.getNumber());
				responsePage.setPageSize(repostList.getSize());
				responsePage.setTotalElement(repostList.getTotalElements());
				responsePage.setTotalPages(repostList.getTotalPages());
				responsePage.setIsLastPage(repostList.isLast());
				responsePage.setIsFirstPage(repostList.isFirst());

			deleteReportMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
			deleteReportMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
			deleteReportMap.put(DataConstant.RESPONSE_BODY, responsePage);
			log.info("AnnualAndFinancialReport recored   found",DataConstant.RECORD_FOUND_MESSAGE);

			return deleteReportMap;

			}
			deleteReportMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			deleteReportMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
			deleteReportMap.put(DataConstant.RESPONSE_BODY, responsePage);
			log.info("AnnualAndFinancialReport recored  not  found",DataConstant.RECORD_NOT_FOUND_MESSAGE);

			return deleteReportMap;
		}
		deleteReportMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
		deleteReportMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_AND_REPORT_TYPE_CANT_NULL);
		deleteReportMap.put(DataConstant.RESPONSE_BODY, null);
		log.info(DataConstant.PAGE_SIZE_AND_INDEX_AND_REPORT_TYPE_CANT_NULL);

		return deleteReportMap;

	}
}
