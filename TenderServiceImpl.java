package com.janaushadhi.adminservice.serviceimpl;

import com.janaushadhi.adminservice.entity.*;
import com.janaushadhi.adminservice.entity.DebarredList;
import com.janaushadhi.adminservice.repository.*;
import com.janaushadhi.adminservice.requestpayload.*;
import com.janaushadhi.adminservice.responsepayload.*;
import com.janaushadhi.adminservice.util.CsvReadder;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class TenderServiceImpl {
    @Autowired
    AwardedTenderRepository awardedTenderRepository;
    @Autowired
    private TenderRepository tenderRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private RevocationRepository revocationRepository;
    @Autowired
    private BlackListRepository blackListRepository;
    @Autowired
    private DebarredRepository debarredRepository;

    public Map<String, Object> addTender(TenderRequestPayload tenderRequestPayload) throws IOException {
        Map<String, Object> tenderMap = new HashMap<>();
        try {
            if (tenderRequestPayload.getTenderTitle() != null || !tenderRequestPayload.getTenderTitle().trim().isEmpty()
                    && tenderRequestPayload.getPublishDate() != null && tenderRequestPayload.getClosingDate() != null
                    && tenderRequestPayload.getTenderfile() != null && !tenderRequestPayload.getTenderfile().trim().isEmpty()
                    && tenderRequestPayload.getNitfile() != null && !tenderRequestPayload.getNitfile().trim().isEmpty()
                    && tenderRequestPayload.getBoqfile() != null && !tenderRequestPayload.getBoqfile().trim().isEmpty()) {
                Tender tender = new Tender();
                tender.setTenderTitle(tenderRequestPayload.getTenderTitle());
                tender.setClosingDate(DateUtil.convertStringDateToDate(tenderRequestPayload.getClosingDate()));
                tender.setPublishDate(DateUtil.convertStringDateToDate(tenderRequestPayload.getPublishDate()));
                tender.setTenderfile(tenderRequestPayload.getTenderfile());
                tender.setNitfile(tenderRequestPayload.getNitfile());
                tender.setBoqfile(tenderRequestPayload.getBoqfile());
                tender.setCreatedDate(new Date());
                tender.setStatus(DataConstant.ONE);
                Tender save = tenderRepository.save(tender);
                tenderMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                tenderMap.put(DataConstant.MESSAGE, DataConstant.TENDER_ADDED_SUCCESSFULLY);
                tenderMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.TENDER_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return tenderMap;
            } else {
                tenderMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                tenderMap.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                tenderMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory", DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return tenderMap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }
        return tenderMap;
    }

    public Map<String, Object> updateTender(TenderUpdateRequestPayload tenderUpdateRequestPayload) throws IOException {
        Map<String, Object> updateTendermap = new HashMap<>();
        try {
            Optional<Tender> tenderId = tenderRepository.findById(tenderUpdateRequestPayload.getTenderId());
            if (tenderId.isPresent()) {
                Tender tender = tenderId.get();
                List<Amendment> amendmentList = new ArrayList<>();
                Amendment amendment = new Amendment();
                List<Amendment> amendmentListold = tenderId.get().getAmendmentList();
                for (Amendment amendmentold : amendmentListold) {
                    BeanUtils.copyProperties(amendmentListold, amendmentold);
                    amendmentList.add(amendmentold);
                }
                List<AmendmentRequestPayload> amendmentRequestPayloads = tenderUpdateRequestPayload.getAmendmentRequestPayloads();
                for (AmendmentRequestPayload amendmentRequestPayload : amendmentRequestPayloads) {
                    BeanUtils.copyProperties(amendmentRequestPayload, amendment);
                }

                amendmentList.add(amendment);
                tender.setAmendmentList(amendmentList);
                Tender save = tenderRepository.save(tender);
                log.info("data save successfully in tender");

                updateTendermap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                updateTendermap.put(DataConstant.MESSAGE, DataConstant.TENDER_UPDATE_SUCCESSFULLY);
                updateTendermap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.TENDER_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return updateTendermap;

            } else {
                updateTendermap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                updateTendermap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                updateTendermap.put(DataConstant.RESPONSE_BODY, null);
                log.info("rocord not found", DataConstant.RECORD_NOT_FOUND_MESSAGE);
                return updateTendermap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }
        return updateTendermap;
    }

	public Map<String, Object> tenderStatusUpdate(Long id, short status) {
		Map<String, Object> map = new HashMap<>();
		Optional<Tender> tenderupdate = tenderRepository.findById(id);
		if (tenderupdate.isPresent()) {
			log.info("  tender Record found! status - {}", tenderupdate);
			Tender admin = tenderupdate.get();
			BeanUtils.copyProperties(tenderupdate, admin);
			admin.setStatus(status);

			Tender tenderstatus = tenderRepository.save(admin);
			map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
			map.put(DataConstant.MESSAGE, DataConstant.TENDER_STATUS_UPDATE);
			map.put(DataConstant.RESPONSE_BODY, tenderstatus);
			log.info("tender status update- {}", tenderstatus);
			return map;
		} else {
			map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
			map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
			map.put(DataConstant.RESPONSE_BODY, null);
			log.error("tender id not present ", id);
			return map;
		}
	}
	
    public Map<String, Object> getByTenderId(Long id) {
        Map<String, Object> getMap = new HashMap<>();
        try {
            Optional<Tender> tenderDetails = tenderRepository.findById(id);
            if (!tenderDetails.isEmpty()) {

                getMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                getMap.put(DataConstant.MESSAGE, DataConstant.TENDER_FOUNDED_SUCCESSFULLY);
                getMap.put(DataConstant.RESPONSE_BODY, tenderDetails);
                log.info(DataConstant.TENDER_FOUNDED_SUCCESSFULLY);

                return getMap;
            } else {
                getMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getMap.put(DataConstant.RESPONSE_BODY, null);
                getMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                log.info("Data Not Found in tender", DataConstant.RECORD_NOT_FOUND_MESSAGE);
                return getMap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
            getMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            return getMap;
        }
    }


    public Map<String, Object> getAllTender(GetAllTender getAllTender) {
        Map<String, Object> tenderMapAll = new HashMap<>();
        try {
            if (getAllTender.getPageIndex() == null && getAllTender.getPageSize() == null) {
                tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                tenderMapAll.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                tenderMapAll.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return tenderMapAll;
            }
            List<TenderResponsePayload> tenderResponsePayloadList = new ArrayList<>();
            TenderResponsePage tenderResponsePage = new TenderResponsePage();
            List<Tender> tenderList = new ArrayList<>();
            Pageable pageable = null;
            Page<Tender> page = null;
            Short status = 2;
            if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0) {
                if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().trim().isEmpty()) {
                    tenderList = tenderRepository.findAllByStatusNot(status);
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText() != null && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().trim().isEmpty()) {
                    tenderList = tenderRepository.findAllBySearchTextContainingIgnoreCase(getAllTender.getSearchText());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                    tenderList = tenderRepository.searchAndOrderByASC(getAllTender.getColumnName());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                    tenderList = tenderRepository.searchAndOrderByDESC(getAllTender.getColumnName());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText() != null && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                    tenderList = tenderRepository.findASC(getAllTender.getSearchText(), getAllTender.getColumnName());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText() != null && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                    tenderList = tenderRepository.findDESC(getAllTender.getSearchText(), getAllTender.getColumnName());
                }
                if (tenderList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", tenderList);
                    for (Tender tender : tenderList) {
                        TenderResponsePayload tenderResponsePayload = new TenderResponsePayload();
                        BeanUtils.copyProperties(tender, tenderResponsePayload);
                        // add amendment
                        List<Amendment> amendmentList = tender.getAmendmentList();
                        tenderResponsePayload.setAmendments(amendmentList);
                        tenderResponsePayload.setClosingDate(DateUtil.convertDateToStringDate(tender.getClosingDate()));
                        tenderResponsePayload.setPublishDate(DateUtil.convertDateToStringDate(tender.getPublishDate()));
                        tenderResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(tender.getCreatedDate()));
                        // tenderResponsePayload.setUpdatedDate(DateUtil.convertDateToStringDate(tender.getUpdatedDate()));
                        tenderResponsePayloadList.add(tenderResponsePayload);
                    }
                    tenderResponsePage.setTenderResponsePayloadList(tenderResponsePayloadList);
                    tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    tenderMapAll.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    tenderMapAll.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record found! status - {}", tenderResponsePage);
                    return tenderMapAll;
                } else {
                    tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    tenderMapAll.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    tenderMapAll.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record not found! status - {}");
                    return tenderMapAll;
                }
            } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null) {
                if (getAllTender.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllTender.getPageIndex(), getAllTender.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = tenderRepository.findAllByStatusNot(status,pageable);

                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText() != null && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().trim().isEmpty()) {
                        page = tenderRepository.findAllByUserName(getAllTender.getSearchText(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                        page = tenderRepository.searchAndOrderByASC(getAllTender.getColumnName(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                        page = tenderRepository.searchAndOrderByDESC(getAllTender.getColumnName(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText() != null && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                        page = tenderRepository.findASC(getAllTender.getSearchText(), getAllTender.getColumnName(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText() != null && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                        page = tenderRepository.findDESC(getAllTender.getSearchText(), getAllTender.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", tenderList);
                        tenderList = page.getContent();
                        int index = 0;
                        for (Tender tenderData : tenderList) {
                            TenderResponsePayload tenderResponsePayload = new TenderResponsePayload();
                            BeanUtils.copyProperties(tenderData, tenderResponsePayload);
                            // add amendment
                            List<Amendment> amendmentList = tenderData.getAmendmentList();
                            tenderResponsePayload.setAmendments(amendmentList);
                            tenderResponsePayload.setClosingDate(DateUtil.convertDateToStringDate(tenderData.getClosingDate()));
                            tenderResponsePayload.setPublishDate(DateUtil.convertDateToStringDate(tenderData.getPublishDate()));
                            tenderResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(tenderData.getCreatedDate()));
                            //for frontEnd team pagination
                            if (getAllTender.getPageIndex() == 0) {
                                tenderResponsePayload.setSerialNo(index + 1);
                                index++;
                            } else {
                                tenderResponsePayload.setSerialNo((getAllTender.getPageSize() * getAllTender.getPageIndex()) + (index + 1));
                                index++;
                            }
                            tenderResponsePayloadList.add(tenderResponsePayload);
                        }
                        tenderResponsePage.setTenderResponsePayloadList(tenderResponsePayloadList);
                        tenderResponsePage.setPageIndex(page.getNumber());
                        tenderResponsePage.setPageSize(page.getSize());
                        tenderResponsePage.setTotalElement(page.getTotalElements());
                        tenderResponsePage.setTotalPages(page.getTotalPages());
                        tenderResponsePage.setIsFirstPage(page.isFirst());
                        tenderResponsePage.setIsLastPage(page.isLast());

                        tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        tenderMapAll.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        tenderMapAll.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record found! status - {}", tenderResponsePage);
                    } else {
                        tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        tenderMapAll.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        tenderMapAll.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    tenderMapAll.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllTender);
                    return tenderMapAll;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            tenderMapAll.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            tenderMapAll.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            tenderMapAll.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return tenderMapAll;
    }


    public Map<String, Object> getAllDeleteTender(GetAllDeleteTender getAllTender) {
        Map<String, Object> deleteMap = new HashMap<>();
        try {
            if (getAllTender.getPageIndex() == null && getAllTender.getPageSize() == null) {
                deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                deleteMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                deleteMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return deleteMap;
            }
            List<TenderResponsePayload> tenderResponsePayloadList = new ArrayList<>();
            TenderResponsePage tenderResponsePage = new TenderResponsePage();
            List<Tender> tenderList = new ArrayList<>();
            Pageable pageable = null;
            Page<Tender> page = null;
            Short status = 2;
            if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0) {
                if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0) {
                    tenderList = tenderRepository.findAllByStatus(status);
                }
                if (tenderList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", tenderList);
                    for (Tender tenders : tenderList) {
                        TenderResponsePayload tenderResponsePayload = new TenderResponsePayload();
                        BeanUtils.copyProperties(tenders, tenderResponsePayload);
                        // add amendment
                        List<Amendment> amendmentList = tenders.getAmendmentList();
                        tenderResponsePayload.setAmendments(amendmentList);
                        tenderResponsePayload.setClosingDate(DateUtil.convertDateToStringDate(tenders.getClosingDate()));
                        tenderResponsePayload.setPublishDate(DateUtil.convertDateToStringDate(tenders.getPublishDate()));
                        tenderResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(tenders.getCreatedDate()));
                        // tenderResponsePayload.setUpdatedDate(DateUtil.convertDateToStringDate(tender.getUpdatedDate()));
                        tenderResponsePayloadList.add(tenderResponsePayload);
                    }
                    tenderResponsePage.setTenderResponsePayloadList(tenderResponsePayloadList);
                    deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    deleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    deleteMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record found! status - {}", tenderResponsePage);
                    return deleteMap;
                } else {
                    deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    deleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    deleteMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record not found! status - {}");
                    return deleteMap;
                }
            } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null) {
                if (getAllTender.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllTender.getPageIndex(), getAllTender.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != 0) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = tenderRepository.findAllByStatus(status, pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", tenderList);
                        tenderList = page.getContent();
                        int index = 0;
                        for (Tender tenderAmend : tenderList) {
                            TenderResponsePayload tenderResponsePayload = new TenderResponsePayload();
                            BeanUtils.copyProperties(tenderAmend, tenderResponsePayload);
                            // add amendment
                            List<Amendment> amendmentList = tenderAmend.getAmendmentList();
                            tenderResponsePayload.setAmendments(amendmentList);
                            tenderResponsePayload.setClosingDate(DateUtil.convertDateToStringDate(tenderAmend.getClosingDate()));
                            tenderResponsePayload.setPublishDate(DateUtil.convertDateToStringDate(tenderAmend.getPublishDate()));
                            tenderResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(tenderAmend.getCreatedDate()));
                            //for frontEnd team pagination
                            if (getAllTender.getPageIndex() == 0) {
                                tenderResponsePayload.setSerialNo(index + 1);
                                index++;
                            } else {
                                tenderResponsePayload.setSerialNo((getAllTender.getPageSize() * getAllTender.getPageIndex()) + (index + 1));
                                index++;
                            }
                            tenderResponsePayloadList.add(tenderResponsePayload);
                        }
                        tenderResponsePage.setTenderResponsePayloadList(tenderResponsePayloadList);
                        tenderResponsePage.setPageIndex(page.getNumber());
                        tenderResponsePage.setPageSize(page.getSize());
                        tenderResponsePage.setTotalElement(page.getTotalElements());
                        tenderResponsePage.setTotalPages(page.getTotalPages());
                        tenderResponsePage.setIsFirstPage(page.isFirst());
                        tenderResponsePage.setIsLastPage(page.isLast());

                        deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        deleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        deleteMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record found! status - {}", tenderResponsePage);
                    } else {
                        deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        deleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        deleteMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    deleteMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllTender);
                    return deleteMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            deleteMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            deleteMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return deleteMap;
    }

    public Map<String, Object> tenderDeleteById(Long id) {
        Map<String, Object> deleteByIdMap = new HashMap<>();
        try {
            Optional<Tender> tender = tenderRepository.findById(id);
            if (tender.isPresent()) {
                Tender data = tender.get();
                data.setStatus(DataConstant.TWO);
                tenderRepository.save(data);
                log.info("tender deleted successfully ");
                deleteByIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                deleteByIdMap.put(DataConstant.MESSAGE, DataConstant.TENDER_DELETE_SUCCESSFULLY);
                return deleteByIdMap;
            } else {
                log.error("tender id not found ");
                deleteByIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                deleteByIdMap.put(DataConstant.MESSAGE, DataConstant.TENDER_ID_NOT_FOUND);
                return deleteByIdMap;
            }
        } catch (Exception e) {
            log.error("exception in tender");
        }
        return deleteByIdMap;
    }


    public Map<String, Object> addDebarredList(DebarredRequest debarredRequest) throws IOException {
        Map<String, Object> debarredMap = new HashMap<>();
        try {

            if (debarredRequest.getId() != 0 && debarredRequest.getId() != null) {
                Optional<DebarredList> optionalDebarredList = debarredRepository.findById(debarredRequest.getId());
                if (optionalDebarredList.isPresent()) {
                    DebarredList debarredList = optionalDebarredList.get();
                    BeanUtils.copyProperties(debarredRequest, debarredList);
                    debarredList.setUpdatedDate(new Date());
                    DebarredList savedDistributor = debarredRepository.save(debarredList);
                    debarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    debarredMap.put(DataConstant.MESSAGE, DataConstant.DEBARRED_UPDATED);
                    debarredMap.put(DataConstant.RESPONSE_BODY, savedDistributor);
                    log.info("Debarred updated successfully: {}", DataConstant.DEBARRED_UPDATED);
                    return debarredMap;
                } else {
                    debarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    debarredMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    debarredMap.put(DataConstant.RESPONSE_BODY, null);
                    log.info("Debarred with id {} not found: {}", debarredRequest.getId(), DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    return debarredMap;
                }
            }
            if (debarredRequest.getDrugName() != null || !debarredRequest.getDrugName().trim().equals("")
                    && debarredRequest.getDrugCode() != null || !debarredRequest.getDrugCode().trim().equals("")
                    && debarredRequest.getManufacturedBy() != null || !debarredRequest.getManufacturedBy().trim().equals("")
                    && debarredRequest.getReason() != null || !debarredRequest.getReason().trim().equals("")
                    && debarredRequest.getFromDate() != null || !debarredRequest.getFromDate().trim().equals("") && debarredRequest.getToDate() != null || debarredRequest.getToDate().trim().equals("")) {
                DebarredList debarredList = new DebarredList();
                debarredList.setDrugCode(debarredRequest.getDrugCode());
                debarredList.setDrugName(debarredRequest.getDrugName());
                debarredList.setReason(debarredRequest.getReason());
                debarredList.setManufacturedBy(debarredRequest.getManufacturedBy());
                debarredList.setFromDate(debarredRequest.getFromDate());
                debarredList.setToDate(debarredRequest.getToDate());
                debarredList.setCreatedDate(new Date());
                debarredList.setStatus(DataConstant.ONE);
                DebarredList save = debarredRepository.save(debarredList);
                debarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                debarredMap.put(DataConstant.MESSAGE, DataConstant.DEBARRED_ADDED_SUCCESSFULLY);
                debarredMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.DEBARRED_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return debarredMap;
            } else {
                debarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                debarredMap.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                debarredMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory", DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return debarredMap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }
        return debarredMap;
    }


    public Map<String, Object> getAllDebarredList(GetAllDebarred getAllDebarred) {
        Map<String, Object> getAllDebarredMap = new HashMap<>();
        try {
            if (getAllDebarred.getPageIndex() == null && getAllDebarred.getPageSize() == null) {
                getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                getAllDebarredMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return getAllDebarredMap;
            }
            List<DebarredResponse> responsePayloadList = new ArrayList<>();
            DebarredResponsePage debarredResponsePage = new DebarredResponsePage();
            List<DebarredList> debarredLists = new ArrayList<>();
            Pageable pageable = null;
            Page<DebarredList> page = null;
            Short status = 2;

            if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0) {
                if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0 && getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().trim().isEmpty()) {
                    debarredLists = debarredRepository.findAllByStatusNot(status);
                } else if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0 && getAllDebarred.getSearchText() != null && !getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().trim().isEmpty()) {
                    debarredLists = debarredRepository.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(getAllDebarred.getSearchText());
                } else if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0 && getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && !getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().equals(DataConstant.ASC)) {
                    debarredLists = debarredRepository.searchAndOrderByASC(getAllDebarred.getColumnName());
                } else if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0 && getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && !getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().equals(DataConstant.DESC)) {
                    debarredLists = debarredRepository.searchAndOrderByDESC(getAllDebarred.getColumnName());
                } else if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0 && getAllDebarred.getSearchText() != null && !getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && !getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().equals(DataConstant.ASC)) {
                    debarredLists = debarredRepository.findASC(getAllDebarred.getSearchText(), getAllDebarred.getColumnName());
                } else if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0 && getAllDebarred.getSearchText() != null && !getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && !getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().equals(DataConstant.DESC)) {
                    debarredLists = debarredRepository.findDESC(getAllDebarred.getSearchText(), getAllDebarred.getColumnName());
                }
                if (debarredLists.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", debarredLists);
                    for (DebarredList debarredList : debarredLists) {
                        DebarredResponse debarredResponse = new DebarredResponse();
                        BeanUtils.copyProperties(debarredList, debarredResponse);
                        debarredResponse.setCreatedDate(DateUtil.convertDateToStringDate(debarredList.getCreatedDate()));
                        debarredResponse.setUpdatedDate(DateUtil.convertDateToStringDate(debarredList.getUpdatedDate()));
                        responsePayloadList.add(debarredResponse);
                    }
                    debarredResponsePage.setDebarredResponseList(responsePayloadList);
                    getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllDebarredMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record found! status - {}", debarredResponsePage);
                    return getAllDebarredMap;
                } else {
                    getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllDebarredMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record not found! status - {}");
                    return getAllDebarredMap;
                }
            } else if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != null) {
                if (getAllDebarred.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllDebarred.getPageIndex(), getAllDebarred.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != 0 && getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().trim().isEmpty()) {
                        page = debarredRepository.findAllByStatusNot(status,pageable);

                    } else if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != null && getAllDebarred.getSearchText() != null && !getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().trim().isEmpty()) {
                        page = debarredRepository.findAllByUserName(getAllDebarred.getSearchText(), pageable);
                    } else if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != null && getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && getAllDebarred.getOrderBy().equals(DataConstant.ASC)) {
                        page = debarredRepository.searchAndOrderByASC(getAllDebarred.getColumnName(), pageable);
                    } else if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != null && getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && getAllDebarred.getOrderBy().equals(DataConstant.DESC)) {
                        page = debarredRepository.searchAndOrderByDESC(getAllDebarred.getColumnName(), pageable);
                    } else if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != null && getAllDebarred.getSearchText() != null && !getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && !getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().equals(DataConstant.ASC)) {
                        page = debarredRepository.findASC(getAllDebarred.getSearchText(), getAllDebarred.getColumnName(), pageable);
                    } else if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != null && getAllDebarred.getSearchText() != null && !getAllDebarred.getSearchText().trim().isEmpty() && getAllDebarred.getColumnName() != null && !getAllDebarred.getColumnName().trim().isEmpty() && getAllDebarred.getOrderBy().equals(DataConstant.DESC)) {
                        page = debarredRepository.findDESC(getAllDebarred.getSearchText(), getAllDebarred.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", debarredLists);
                        debarredLists = page.getContent();
                        int index = 0;
                        for (DebarredList debarredList : debarredLists) {
                            DebarredResponse debarredResponse = new DebarredResponse();
                            BeanUtils.copyProperties(debarredList, debarredResponse);
                            debarredResponse.setCreatedDate(DateUtil.convertDateToStringDate(debarredList.getCreatedDate()));
                            debarredResponse.setUpdatedDate(DateUtil.convertDateToStringDate(debarredList.getUpdatedDate()));
                            //for frontEnd team pagination
                            if (getAllDebarred.getPageIndex() == 0) {
                                debarredResponse.setSerialNo(index + 1);
                                index++;
                            } else {
                                debarredResponse.setSerialNo((getAllDebarred.getPageSize() * getAllDebarred.getPageIndex()) + (index + 1));
                                index++;
                            }
                            responsePayloadList.add(debarredResponse);
                        }
                        debarredResponsePage.setDebarredResponseList(responsePayloadList);
                        debarredResponsePage.setPageIndex(page.getNumber());
                        debarredResponsePage.setPageSize(page.getSize());
                        debarredResponsePage.setTotalElement(page.getTotalElements());
                        debarredResponsePage.setTotalPages(page.getTotalPages());
                        debarredResponsePage.setIsFirstPage(page.isFirst());
                        debarredResponsePage.setIsLastPage(page.isLast());

                        getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllDebarredMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                        log.info("Record found! status - {}", debarredResponsePage);
                    } else {
                        getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllDebarredMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return getAllDebarredMap;
                    }
                } else {
                    getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllDebarred);
                    return getAllDebarredMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return getAllDebarredMap;
        } catch (Exception e) {
            getAllDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllDebarredMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return getAllDebarredMap;
        }
        return getAllDebarredMap;
    }

    public Map<String, Object> debarredDeleteById(Long id) {
        Map<String, Object> debarredMap = new HashMap<>();
        try {
            Optional<DebarredList> debarredList = debarredRepository.findById(id);
            if (debarredList.isPresent()) {
                DebarredList debarred = debarredList.get();
                debarred.setStatus(DataConstant.TWO);
                debarredRepository.save(debarred);
                log.info("debarred list deleted successfully ");
                debarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                debarredMap.put(DataConstant.MESSAGE, DataConstant.DEBARRED_LIST_DELETE_SUCCESSFULLY);
                return debarredMap;
            } else {
                log.error("debarred  id not found ");
                debarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                debarredMap.put(DataConstant.MESSAGE, DataConstant.DEBARRED_ID_NOT_FOUND);
                return debarredMap;
            }
        } catch (Exception e) {
            log.error("exception in debarred");
        }
        return debarredMap;
    }

    public Map<String, Object> getAllDeleteDebarredList(GetAllDeleteDebarred getAllDebarred) {
        Map<String, Object> DebarredListMap = new HashMap<>();
        try {
            if (getAllDebarred.getPageIndex() == null && getAllDebarred.getPageSize() == null) {
                DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                DebarredListMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                DebarredListMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return DebarredListMap;
            }
            List<DebarredResponse> responsePayloadList = new ArrayList<>();
            DebarredResponsePage debarredResponsePage = new DebarredResponsePage();
            List<DebarredList> debarredLists = new ArrayList<>();
            Pageable pageable = null;
            Page<DebarredList> page = null;
            Short status = 2;
            if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0) {
                if (getAllDebarred.getPageIndex() == 0 && getAllDebarred.getPageSize() == 0) {
                    debarredLists = debarredRepository.findAllByStatus(status);
                }
                if (debarredLists.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", debarredLists);
                    for (DebarredList debarredList : debarredLists) {
                        DebarredResponse debarredResponse = new DebarredResponse();
                        BeanUtils.copyProperties(debarredList, debarredResponse);
                        debarredResponse.setCreatedDate(DateUtil.convertDateToStringDate(debarredList.getCreatedDate()));
                        debarredResponse.setUpdatedDate(DateUtil.convertDateToStringDate(debarredList.getUpdatedDate()));
                        responsePayloadList.add(debarredResponse);
                    }
                    debarredResponsePage.setDebarredResponseList(responsePayloadList);
                    DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    DebarredListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    DebarredListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record found! status - {}", debarredResponsePage);
                    return DebarredListMap;
                } else {
                    DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    DebarredListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    DebarredListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record not found! status - {}");
                    return DebarredListMap;
                }
            } else if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != null) {
                if (getAllDebarred.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllDebarred.getPageIndex(), getAllDebarred.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllDebarred.getPageIndex() != null && getAllDebarred.getPageSize() != 0) {
                        page = debarredRepository.findAllByStatus(status, pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", debarredLists);
                        debarredLists = page.getContent();
                        int index = 0;
                        for (DebarredList debarredList : debarredLists) {
                            DebarredResponse debarredResponse = new DebarredResponse();
                            BeanUtils.copyProperties(debarredList, debarredResponse);
                            debarredResponse.setCreatedDate(DateUtil.convertDateToStringDate(debarredList.getCreatedDate()));
                            debarredResponse.setUpdatedDate(DateUtil.convertDateToStringDate(debarredList.getUpdatedDate()));
                            //for frontEnd team pagination
                            if (getAllDebarred.getPageIndex() == 0) {
                                debarredResponse.setSerialNo(index + 1);
                                index++;
                            } else {
                                debarredResponse.setSerialNo((getAllDebarred.getPageSize() * getAllDebarred.getPageIndex()) + (index + 1));
                                index++;
                            }
                            responsePayloadList.add(debarredResponse);
                        }
                        debarredResponsePage.setDebarredResponseList(responsePayloadList);
                        debarredResponsePage.setPageIndex(page.getNumber());
                        debarredResponsePage.setPageSize(page.getSize());
                        debarredResponsePage.setTotalElement(page.getTotalElements());
                        debarredResponsePage.setTotalPages(page.getTotalPages());
                        debarredResponsePage.setIsFirstPage(page.isFirst());
                        debarredResponsePage.setIsLastPage(page.isLast());

                        DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        DebarredListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        DebarredListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                        log.info("Record found! status - {}", debarredResponsePage);
                    } else {
                        DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        DebarredListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        DebarredListMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return DebarredListMap;
                    }
                } else {
                    DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    DebarredListMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllDebarred);
                    return DebarredListMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            DebarredListMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return DebarredListMap;
        } catch (Exception e) {
            DebarredListMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            DebarredListMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return DebarredListMap;
        }
        return DebarredListMap;
    }

    public Map<Object, Object> uploadCsvForDebarredList(MultipartFile docfile) throws IOException {
        Map<Object, Object> csvDebarredMap = new HashMap<>();

        List<DebarredList> debarredList = CsvReadder.readCsvForDebarred(docfile.getInputStream());
        List<DebarredList> debarredListNew = new ArrayList<DebarredList>();
        try {
            if (!debarredList.isEmpty()) {

                List<DebarredResponse> debarredResponses = new ArrayList<>();
                for (DebarredList list : debarredList) {
                    DebarredList distributer = new DebarredList();
                    distributer.setId(list.getId());
                    distributer.setDrugName(list.getDrugName());
                    distributer.setDrugCode(list.getDrugCode());
                    distributer.setReason(list.getReason());
                    distributer.setFromDate(list.getFromDate());
                    distributer.setToDate(list.getToDate());
                    distributer.setManufacturedBy(list.getManufacturedBy());
                    distributer.setCreatedDate(list.getCreatedDate());
                    distributer.setUpdatedDate(list.getUpdatedDate());
                    distributer.setStatus(DataConstant.ONE);
//                    DebarredList saveDistributor = debarredRepository.save(distributer);
                    debarredListNew.add(distributer);
                    DebarredResponse responsePayload = new DebarredResponse();
                    BeanUtils.copyProperties(distributer, responsePayload);
                    debarredResponses.add(responsePayload);
                }
                debarredRepository.saveAll(debarredListNew);
                csvDebarredMap.put(DataConstant.RESPONSE_BODY, debarredResponses);
                csvDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                csvDebarredMap.put(DataConstant.MESSAGE, DataConstant.DATA_STORE_SUCCESSFULLY);
                log.error("Data added Successfully in debarred List", DataConstant.DATA_STORE_SUCCESSFULLY);
            } else {
                csvDebarredMap.put(DataConstant.RESPONSE_BODY, null);
                csvDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                csvDebarredMap.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
                log.error("Data not found  for debarred List", DataConstant.DATA_NOT_FOUND);

            }
        } catch (Exception e) {
            log.error("Exception occurs while uploading city {}", e.getMessage());
            csvDebarredMap.put(DataConstant.RESPONSE_BODY, null);
            csvDebarredMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            csvDebarredMap.put(DataConstant.MESSAGE, "Csv file content improper data, Please enter proper image url and corresponding data !!");
        }
        return csvDebarredMap;
    }

    public Map<String, Object> addBlackList(BlackListRequest blackListRequest) throws IOException {
        Map<String, Object> blackListMap = new HashMap<>();
        try {

            if (blackListRequest.getId() != 0 && blackListRequest.getId() != null) {
                Optional<BlackList> blackListOptional = blackListRepository.findById(blackListRequest.getId());
                if (blackListOptional.isPresent()) {
                    BlackList blackList = blackListOptional.get();
                    BeanUtils.copyProperties(blackListRequest, blackList);
                    blackList.setUpdatedDate(new Date());
                    blackList.setStatus(DataConstant.ONE);
                    BlackList savedDistributor = blackListRepository.save(blackList);
                    blackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    blackListMap.put(DataConstant.MESSAGE, DataConstant.BLACK_LIST_UPDATED);
                    blackListMap.put(DataConstant.RESPONSE_BODY, savedDistributor);
                    log.info("black list updated successfully: {}", DataConstant.BLACK_LIST_UPDATED);
                    return blackListMap;
                } else {
                    blackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    blackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    blackListMap.put(DataConstant.RESPONSE_BODY, null);
                    log.info("black list with id {} not found: {}", blackListRequest.getId(), DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    return blackListMap;
                }
            }
            if (blackListRequest.getDrugName() != null || !blackListRequest.getDrugName().trim().equals("")
                    && blackListRequest.getDrugCode() != null || !blackListRequest.getDrugCode().trim().equals("")
                    && blackListRequest.getManufacturedBy() != null || !blackListRequest.getManufacturedBy().trim().equals("")
                    && blackListRequest.getReason() != null || !blackListRequest.getReason().trim().equals("")
                    && blackListRequest.getFromDate() != null || !blackListRequest.getFromDate().trim().equals("") && blackListRequest.getToDate() != null || blackListRequest.getToDate().trim().equals("")) {
                BlackList blackList = new BlackList();
                blackList.setDrugCode(blackListRequest.getDrugCode());
                blackList.setDrugName(blackListRequest.getDrugName());
                blackList.setReason(blackListRequest.getReason());
                blackList.setManufacturedBy(blackListRequest.getManufacturedBy());
                blackList.setUploadImage(blackListRequest.getUploadImage());
                blackList.setFromDate(blackListRequest.getFromDate());
                blackList.setToDate(blackListRequest.getToDate());
                blackList.setCreatedDate(new Date());
                blackList.setStatus(DataConstant.ONE);
                BlackList save = blackListRepository.save(blackList);
                blackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                blackListMap.put(DataConstant.MESSAGE, DataConstant.DEBARRED_ADDED_SUCCESSFULLY);
                blackListMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.DEBARRED_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return blackListMap;
            } else {
                blackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                blackListMap.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                blackListMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory", DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return blackListMap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }
        return blackListMap;
    }

    public Map<Object, Object> uploadCsvForBlackList(MultipartFile docfile) throws IOException {
        Map<Object, Object> blackMap = new HashMap<>();
        List<BlackList> blackLists = CsvReadder.readCsvForBlackList(docfile.getInputStream());
        List<BlackList> blackListNew = new ArrayList<>();
        try {
            if (!blackLists.isEmpty()) {
                List<BlackListResponse> blackListResponses = new ArrayList<>();
                for (BlackList list : blackLists) {
                    BlackList blackList = new BlackList();
                    blackList.setId(list.getId());
                    blackList.setDrugName(list.getDrugName());
                    blackList.setDrugCode(list.getDrugCode());
                    blackList.setReason(list.getReason());
                    blackList.setFromDate(list.getFromDate());
                    blackList.setToDate(list.getToDate());
                    blackList.setManufacturedBy(list.getManufacturedBy());
                    blackList.setStatus(DataConstant.ONE);
//                    blackList.setCreatedDate(list.getCreatedDate());
//                    blackList.setUpdatedDate(list.getUpdatedDate());
                    blackListNew.add(blackList);
//                    BlackList saveDistributor = blackListRepository.save(blackList);
                    BlackListResponse responsePayload = new BlackListResponse();
                    BeanUtils.copyProperties(blackList, responsePayload);
                    blackListResponses.add(responsePayload);
                }
                blackListRepository.saveAll(blackListNew);
                blackMap.put(DataConstant.RESPONSE_BODY, blackListResponses);
                blackMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                blackMap.put(DataConstant.MESSAGE, DataConstant.DATA_STORE_SUCCESSFULLY);
                log.error("Data added Successfully in black List", DataConstant.DATA_STORE_SUCCESSFULLY);

            } else {
                blackMap.put(DataConstant.RESPONSE_BODY, null);
                blackMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                blackMap.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
                log.error("Data not  added in black List", DataConstant.DATA_NOT_FOUND);

            }
        } catch (Exception e) {
            log.error("Exception occurs while uploading city {}", e.getMessage());
            blackMap.put(DataConstant.RESPONSE_BODY, null);
            blackMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            blackMap.put(DataConstant.MESSAGE, "Csv file content improper data, Please enter proper image url and corresponding data !!");
        }
        return blackMap;
    }

    public Map<String, Object> getAllBlackList(GetAllBlackList getAllBlackList) {
        Map<String, Object> allBlackListMap = new HashMap<>();
        try {
            if (getAllBlackList.getPageIndex() == null && getAllBlackList.getPageSize() == null) {
                allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                allBlackListMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                allBlackListMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return allBlackListMap;
            }
            List<BlackListResponse> responsePayloadList = new ArrayList<>();
            BlackListResponsePage debarredResponsePage = new BlackListResponsePage();
            List<BlackList> blackLists = new ArrayList<>();
            Pageable pageable = null;
            Page<BlackList> page = null;
            Short status =2;
            //   Sort sort = Sort.by(Sort.Direction.ASC, "id");

            if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0) {
                if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0 && getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().trim().isEmpty()) {
                    blackLists = blackListRepository.findAllByStatusNot(status);
                } else if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0 && getAllBlackList.getSearchText() != null && !getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().trim().isEmpty()) {
                    blackLists = blackListRepository.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(getAllBlackList.getSearchText());
                } else if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0 && getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && !getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().equals(DataConstant.ASC)) {
                    blackLists = blackListRepository.searchAndOrderByASC(getAllBlackList.getColumnName());
                } else if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0 && getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && !getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().equals(DataConstant.DESC)) {
                    blackLists = blackListRepository.searchAndOrderByDESC(getAllBlackList.getColumnName());
                } else if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0 && getAllBlackList.getSearchText() != null && !getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && !getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().equals(DataConstant.ASC)) {
                    blackLists = blackListRepository.findASC(getAllBlackList.getSearchText(), getAllBlackList.getColumnName());
                } else if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0 && getAllBlackList.getSearchText() != null && !getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && !getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().equals(DataConstant.DESC)) {
                    blackLists = blackListRepository.findDESC(getAllBlackList.getSearchText(), getAllBlackList.getColumnName());
                }
                if (blackLists.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", blackLists);
                    for (BlackList blackList : blackLists) {
                        BlackListResponse blackListResponse = new BlackListResponse();
                        BeanUtils.copyProperties(blackList, blackListResponse);
                        blackListResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                        blackListResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                        responsePayloadList.add(blackListResponse);
                    }
                    debarredResponsePage.setBlackListResponseList(responsePayloadList);
                    allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    allBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    allBlackListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record found! status - {}", debarredResponsePage);
                    return allBlackListMap;
                } else {
                    allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    allBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    allBlackListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record not found! status - {}");
                    return allBlackListMap;
                }
            } else if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != null) {
                if (getAllBlackList.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllBlackList.getPageIndex(), getAllBlackList.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != 0 && getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = blackListRepository.findAllByStatusNot( status,pageable);

                    } else if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != null && getAllBlackList.getSearchText() != null && !getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().trim().isEmpty()) {
                        page = blackListRepository.findAllByUserName(getAllBlackList.getSearchText(), pageable);
                    } else if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != null && getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && getAllBlackList.getOrderBy().equals(DataConstant.ASC)) {
                        page = blackListRepository.searchAndOrderByASC(getAllBlackList.getColumnName(), pageable);
                    } else if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != null && getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && getAllBlackList.getOrderBy().equals(DataConstant.DESC)) {
                        page = blackListRepository.searchAndOrderByDESC(getAllBlackList.getColumnName(), pageable);
                    } else if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != null && getAllBlackList.getSearchText() != null && !getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && !getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().equals(DataConstant.ASC)) {
                        page = blackListRepository.findASC(getAllBlackList.getSearchText(), getAllBlackList.getColumnName(), pageable);
                    } else if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != null && getAllBlackList.getSearchText() != null && !getAllBlackList.getSearchText().trim().isEmpty() && getAllBlackList.getColumnName() != null && !getAllBlackList.getColumnName().trim().isEmpty() && getAllBlackList.getOrderBy().equals(DataConstant.DESC)) {
                        page = blackListRepository.findDESC(getAllBlackList.getSearchText(), getAllBlackList.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", blackLists);
                        blackLists = page.getContent();
                        int index = 0;
                        for (BlackList blackList : blackLists) {
                            BlackListResponse blackListResponse = new BlackListResponse();
                            BeanUtils.copyProperties(blackList, blackListResponse);
                            blackListResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                            blackListResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                            //for frontEnd team pagination
                            if (getAllBlackList.getPageIndex() == 0) {
                                blackListResponse.setSerialNo(index + 1);
                                index++;
                            } else {
                                blackListResponse.setSerialNo((getAllBlackList.getPageSize() * getAllBlackList.getPageIndex()) + (index + 1));
                                index++;
                            }
                            responsePayloadList.add(blackListResponse);
                        }
                        debarredResponsePage.setBlackListResponseList(responsePayloadList);
                        debarredResponsePage.setPageIndex(page.getNumber());
                        debarredResponsePage.setPageSize(page.getSize());
                        debarredResponsePage.setTotalElement(page.getTotalElements());
                        debarredResponsePage.setTotalPages(page.getTotalPages());
                        debarredResponsePage.setIsFirstPage(page.isFirst());
                        debarredResponsePage.setIsLastPage(page.isLast());

                        allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        allBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        allBlackListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                        log.info("Record found! status - {}", debarredResponsePage);
                    } else {
                        allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        allBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        allBlackListMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return allBlackListMap;
                    }
                } else {
                    allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    allBlackListMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllBlackList);
                    return allBlackListMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            allBlackListMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return allBlackListMap;
        } catch (Exception e) {
            allBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            allBlackListMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return allBlackListMap;
        }
        return allBlackListMap;
    }

    public Map<String, Object> blackListDeleteById(Long id) {
        Map<String, Object> blackListDeleteMap = new HashMap<>();
        try {
            Optional<BlackList> blackList = blackListRepository.findById(id);
            if (blackList.isPresent()) {
                BlackList blackListData = blackList.get();
                blackListData.setStatus(DataConstant.TWO);
                blackListRepository.save(blackListData);
                log.info("black list  tender deleted successfully ");
                blackListDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                blackListDeleteMap.put(DataConstant.MESSAGE, DataConstant.BLACK_LIST_DELETE_SUCCESSFULLY);
                return blackListDeleteMap;
            } else {
                log.error("black list tender id not found ");
                blackListDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                blackListDeleteMap.put(DataConstant.MESSAGE, DataConstant.BLACK_LIST_ID_NOT_FOUND);
                return blackListDeleteMap;
            }
        } catch (Exception e) {
            log.error("exception in blackList tender");
        }
        return blackListDeleteMap;
    }

    public Map<String, Object> getAllDeleteBlackList(GetAlldeleteBlackList getAllBlackList) {
        Map<String, Object> DeleteBlackListMap = new HashMap<>();
        try {
            if (getAllBlackList.getPageIndex() == null && getAllBlackList.getPageSize() == null) {
                DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                DeleteBlackListMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return DeleteBlackListMap;
            }
            List<BlackListResponse> responsePayloadList = new ArrayList<>();
            BlackListResponsePage debarredResponsePage = new BlackListResponsePage();
            List<BlackList> blackLists = new ArrayList<>();
            Pageable pageable = null;
            Page<BlackList> page = null;
            //   Sort sort = Sort.by(Sort.Direction.ASC, "id");
            Short status = 2;
            if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0) {
                if (getAllBlackList.getPageIndex() == 0 && getAllBlackList.getPageSize() == 0) {
                    blackLists = blackListRepository.findAllByStatus(status);
                }
                if (blackLists.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", blackLists);
                    for (BlackList blackList : blackLists) {
                        BlackListResponse blackListResponse = new BlackListResponse();
                        BeanUtils.copyProperties(blackList, blackListResponse);
                        blackListResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                        blackListResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                        responsePayloadList.add(blackListResponse);
                    }
                    debarredResponsePage.setBlackListResponseList(responsePayloadList);
                    DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    DeleteBlackListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record found! status - {}", debarredResponsePage);
                    return DeleteBlackListMap;
                } else {
                    DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    DeleteBlackListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                    log.info("Record not found! status - {}");
                    return DeleteBlackListMap;
                }
            } else if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != null) {
                if (getAllBlackList.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllBlackList.getPageIndex(), getAllBlackList.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllBlackList.getPageIndex() != null && getAllBlackList.getPageSize() != 0) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = blackListRepository.findAllByStatus(status, pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", blackLists);
                        blackLists = page.getContent();
                        int index = 0;
                        for (BlackList blackList : blackLists) {
                            BlackListResponse blackListResponse = new BlackListResponse();
                            BeanUtils.copyProperties(blackList, blackListResponse);
                            blackListResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                            blackListResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                            //for frontEnd team pagination
                            if (getAllBlackList.getPageIndex() == 0) {
                                blackListResponse.setSerialNo(index + 1);
                                index++;
                            } else {
                                blackListResponse.setSerialNo((getAllBlackList.getPageSize() * getAllBlackList.getPageIndex()) + (index + 1));
                                index++;
                            }
                            responsePayloadList.add(blackListResponse);
                        }
                        debarredResponsePage.setBlackListResponseList(responsePayloadList);
                        debarredResponsePage.setPageIndex(page.getNumber());
                        debarredResponsePage.setPageSize(page.getSize());
                        debarredResponsePage.setTotalElement(page.getTotalElements());
                        debarredResponsePage.setTotalPages(page.getTotalPages());
                        debarredResponsePage.setIsFirstPage(page.isFirst());
                        debarredResponsePage.setIsLastPage(page.isLast());

                        DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        DeleteBlackListMap.put(DataConstant.RESPONSE_BODY, debarredResponsePage);
                        log.info("Record found! status - {}", debarredResponsePage);
                    } else {
                        DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        DeleteBlackListMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return DeleteBlackListMap;
                    }
                } else {
                    DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllBlackList);
                    return DeleteBlackListMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return DeleteBlackListMap;
        } catch (Exception e) {
            DeleteBlackListMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            DeleteBlackListMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return DeleteBlackListMap;
        }
        return DeleteBlackListMap;
    }

    public Map<String, Object> addRevocationorder(RevocationRequest revocationRequest) throws IOException {
        Map<String, Object> RevocationMap = new HashMap<>();
        try {

            if (revocationRequest.getId() != 0 && revocationRequest.getId() != null) {
                Optional<RevocationOrder> optionalRevocationOrder = revocationRepository.findById(revocationRequest.getId());
                if (optionalRevocationOrder.isPresent()) {
                    RevocationOrder revocationOrder = optionalRevocationOrder.get();
                    BeanUtils.copyProperties(revocationRequest, revocationOrder);
                    revocationOrder.setUpdatedDate(new Date());
                    revocationOrder.setStatus(DataConstant.ONE);
                    RevocationOrder save = revocationRepository.save(revocationOrder);
                    RevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    RevocationMap.put(DataConstant.MESSAGE, DataConstant.REVOCATION_ORDER_UPDATED);
                    RevocationMap.put(DataConstant.RESPONSE_BODY, save);
                    log.info("black list  vendor updated successfully: {}", DataConstant.REVOCATION_ORDER_UPDATED);
                    return RevocationMap;
                } else {
                    RevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    RevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    RevocationMap.put(DataConstant.RESPONSE_BODY, null);
                    log.info("black list vendor with id {} not found: {}", revocationRequest.getId(), DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    return RevocationMap;
                }
            }
            if (revocationRequest.getDrugName() != null || !revocationRequest.getDrugName().trim().isEmpty()
                    && revocationRequest.getDrugCode() != null || !revocationRequest.getDrugCode().trim().isEmpty()
                    && revocationRequest.getManufacturedBy() != null || !revocationRequest.getManufacturedBy().trim().isEmpty()
                    && revocationRequest.getReason() != null || !revocationRequest.getReason().trim().isEmpty()
                    && revocationRequest.getBlackListDate() != null || !revocationRequest.getBlackListDate().trim().isEmpty()) {
                RevocationOrder blackList = new RevocationOrder();
                blackList.setDrugCode(revocationRequest.getDrugCode());
                blackList.setDrugName(revocationRequest.getDrugName());
                blackList.setReason(revocationRequest.getReason());
                blackList.setManufacturedBy(revocationRequest.getManufacturedBy());
                blackList.setUploadImage(revocationRequest.getUploadImage());
                blackList.setBlackListDate(revocationRequest.getBlackListDate());
                blackList.setCreatedDate(new Date());
                blackList.setStatus(DataConstant.ONE);
                RevocationOrder save = revocationRepository.save(blackList);
                RevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                RevocationMap.put(DataConstant.MESSAGE, DataConstant.REVOCATION_ORDER_ADDED_SUCCESSFULLY);
                RevocationMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.REVOCATION_ORDER_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return RevocationMap;
            } else {
                RevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                RevocationMap.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                RevocationMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory", DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return RevocationMap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }
        return RevocationMap;
    }


    public Map<Object, Object> uploadCsvForRevocationOrder(MultipartFile docfile) throws IOException {
        Map<Object, Object> CsvForRevocationMap = new HashMap<>();
        List<RevocationOrder> blackLists = CsvReadder.readCsvForRevocationOrder(docfile.getInputStream());
        List<RevocationOrder> blackListsList = new ArrayList<>();
        try {
            if (!blackLists.isEmpty()) {
                List<RevocationResponse> blackListResponses = new ArrayList<>();
                for (RevocationOrder list : blackLists) {
                    RevocationOrder revocationOrder = new RevocationOrder();
                    revocationOrder.setId(list.getId());
                    revocationOrder.setDrugName(list.getDrugName());
                    revocationOrder.setDrugCode(list.getDrugCode());
                    revocationOrder.setReason(list.getReason());
                    revocationOrder.setManufacturedBy(list.getManufacturedBy());
                    revocationOrder.setBlackListDate(list.getBlackListDate());
                    revocationOrder.setCreatedDate(list.getCreatedDate());
                    revocationOrder.setUpdatedDate(list.getUpdatedDate());
                    revocationOrder.setStatus(DataConstant.ONE);
//                    RevocationOrder saveDistributor = revocationRepository.save(revocationOrder);
                    blackListsList.add(revocationOrder);
                    RevocationResponse revocationResponse = new RevocationResponse();
                    BeanUtils.copyProperties(revocationOrder, revocationResponse);
                    blackListResponses.add(revocationResponse);
                }
                revocationRepository.saveAll(blackListsList);
                CsvForRevocationMap.put(DataConstant.RESPONSE_BODY, blackListResponses);
                CsvForRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                CsvForRevocationMap.put(DataConstant.MESSAGE, DataConstant.DATA_STORE_SUCCESSFULLY);
                log.info(DataConstant.DATA_STORE_SUCCESSFULLY + "in revocation Order  csv" + "! status - {}", DataConstant.OK);

            } else {
                CsvForRevocationMap.put(DataConstant.RESPONSE_BODY, null);
                CsvForRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                CsvForRevocationMap.put(DataConstant.MESSAGE, DataConstant.FILE_NOT_FOUND);
                log.info(DataConstant.FILE_NOT_FOUND + "! status - {}", DataConstant.OK);

            }
        } catch (Exception e) {
            log.error("Exception occurs while uploading city {}", e.getMessage());
            CsvForRevocationMap.put(DataConstant.RESPONSE_BODY, null);
            CsvForRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            CsvForRevocationMap.put(DataConstant.MESSAGE, "Csv file content improper data, Please enter proper image url and corresponding data !!");
        }
        return CsvForRevocationMap;
    }

    public Map<String, Object> getAllRevocationOrder(GetAllRevocation getAllRevocation) {
        Map<String, Object> getAllRevocationMap = new HashMap<>();
        try {
            if (getAllRevocation.getPageIndex() == null && getAllRevocation.getPageSize() == null) {
                getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                getAllRevocationMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return getAllRevocationMap;
            }
            List<RevocationResponse> responsePayloadList = new ArrayList<>();
            RevocationResponsePage revocationResponsePage = new RevocationResponsePage();
            List<RevocationOrder> revocationOrders = new ArrayList<>();
            Pageable pageable = null;
            Page<RevocationOrder> page=null;
            Short status = 2;
            //     Sort sort = Sort.by(Sort.Direction.ASC, "id");

            if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0) {
                if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0 && getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().trim().isEmpty()) {
                    revocationOrders = revocationRepository.findAllByStatusNot( status);
                } else if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0 && getAllRevocation.getSearchText() != null && !getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().trim().isEmpty()) {
                    revocationOrders = revocationRepository.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(getAllRevocation.getSearchText());
                } else if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0 && getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.ASC)) {
                    revocationOrders = revocationRepository.searchAndOrderByASC(getAllRevocation.getColumnName());
                } else if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0 && getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.DESC)) {
                    revocationOrders = revocationRepository.searchAndOrderByDESC(getAllRevocation.getColumnName());
                } else if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0 && getAllRevocation.getSearchText() != null && !getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.ASC)) {
                    revocationOrders = revocationRepository.findASC(getAllRevocation.getSearchText(), getAllRevocation.getColumnName());
                } else if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0 && getAllRevocation.getSearchText() != null && !getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.DESC)) {
                    revocationOrders = revocationRepository.findDESC(getAllRevocation.getSearchText(), getAllRevocation.getColumnName());
                }
                if (revocationOrders.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", revocationOrders);
                    for (RevocationOrder blackList : revocationOrders) {
                        RevocationResponse revocationResponse = new RevocationResponse();
                        BeanUtils.copyProperties(blackList, revocationResponse);
                        revocationResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                        revocationResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                        responsePayloadList.add(revocationResponse);
                    }
                    revocationResponsePage.setRevocationResponseList(responsePayloadList);
                    getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllRevocationMap.put(DataConstant.RESPONSE_BODY, revocationResponsePage);
                    log.info("Record found! status - {}", revocationResponsePage);
                    return getAllRevocationMap;
                } else {
                    getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllRevocationMap.put(DataConstant.RESPONSE_BODY, revocationResponsePage);
                    log.info("Record not found! status - {}");
                    return getAllRevocationMap;
                }
            } else if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != null) {
                if (getAllRevocation.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllRevocation.getPageIndex(), getAllRevocation.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != 0 && getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().trim().isEmpty()) {
                        page = revocationRepository.findAllByStatusNot(status,pageable);

                    } else if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != null && getAllRevocation.getSearchText() != null && !getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().trim().isEmpty()) {
                        page = revocationRepository.findAllByUserName(getAllRevocation.getSearchText(), pageable);
                    } else if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != null && getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.ASC)) {
                        page = revocationRepository.searchAndOrderByASC(getAllRevocation.getColumnName(), pageable);
                    } else if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != null && getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.DESC)) {
                        page = revocationRepository.searchAndOrderByDESC(getAllRevocation.getColumnName(), pageable);
                    } else if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != null && getAllRevocation.getSearchText() != null && !getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.ASC)) {
                        page = revocationRepository.findASC(getAllRevocation.getSearchText(), getAllRevocation.getColumnName(), pageable);
                    } else if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != null && getAllRevocation.getSearchText() != null && !getAllRevocation.getSearchText().trim().isEmpty() && getAllRevocation.getColumnName() != null && !getAllRevocation.getColumnName().trim().isEmpty() && getAllRevocation.getOrderBy().equals(DataConstant.DESC)) {
                        page = revocationRepository.findDESC(getAllRevocation.getSearchText(), getAllRevocation.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", revocationOrders);
                        revocationOrders = page.getContent();
                        int index = 0;
                        for (RevocationOrder blackList : revocationOrders) {
                            RevocationResponse revocationResponse = new RevocationResponse();
                            BeanUtils.copyProperties(blackList, revocationResponse);
                            revocationResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                            revocationResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                            //for frontEnd team pagination
                            if (getAllRevocation.getPageIndex() == 0) {
                                revocationResponse.setSerialNo(index + 1);
                                index++;
                            } else {
                                revocationResponse.setSerialNo((getAllRevocation.getPageSize() * getAllRevocation.getPageIndex()) + (index + 1));
                                index++;
                            }
                            responsePayloadList.add(revocationResponse);
                        }
                        revocationResponsePage.setRevocationResponseList(responsePayloadList);
                        revocationResponsePage.setPageIndex(page.getNumber());
                        revocationResponsePage.setPageSize(page.getSize());
                        revocationResponsePage.setTotalElement(page.getTotalElements());
                        revocationResponsePage.setTotalPages(page.getTotalPages());
                        revocationResponsePage.setIsFirstPage(page.isFirst());
                        revocationResponsePage.setIsLastPage(page.isLast());

                        getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllRevocationMap.put(DataConstant.RESPONSE_BODY, revocationResponsePage);
                        log.info("Record found! status - {}", revocationResponsePage);
                    } else {
                        getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllRevocationMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return getAllRevocationMap;
                    }
                } else {
                    getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllRevocation);
                    return getAllRevocationMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return getAllRevocationMap;
        } catch (Exception e) {
            getAllRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllRevocationMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return getAllRevocationMap;
        }
        return getAllRevocationMap;
    }

    public Map<String, Object> revocationOrderDeleteById(Long id) {
        Map<String, Object> deleteMap = new HashMap<>();
        try {
            Optional<RevocationOrder> optionalRevocationOrder = revocationRepository.findById(id);
            if (optionalRevocationOrder.isPresent()) {
                RevocationOrder revocationOrder = optionalRevocationOrder.get();
                revocationOrder.setStatus(DataConstant.TWO);
                revocationRepository.save(revocationOrder);
                log.info("revocation   tender deleted successfully ");
                deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                deleteMap.put(DataConstant.MESSAGE, DataConstant.REVOCATION_ORDER_DELETE_SUCCESSFULLY);
                return deleteMap;
            } else {
                log.error("revocation tender id not found ");
                deleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                deleteMap.put(DataConstant.MESSAGE, DataConstant.REVOCATION_ORDER_ID_NOT_FOUND);
                return deleteMap;
            }
        } catch (Exception e) {
            log.error("exception in revocation tender");
        }
        return deleteMap;
    }

    public Map<String, Object> getAllDeleteRevocationOrder(GetAllDeleteRevocation getAllRevocation) {
        Map<String, Object> DeleteRevocationMap = new HashMap<>();
        try {
            if (getAllRevocation.getPageIndex() == null && getAllRevocation.getPageSize() == null) {
                DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                DeleteRevocationMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return DeleteRevocationMap;
            }
            List<RevocationResponse> responsePayloadList = new ArrayList<>();
            RevocationResponsePage revocationResponsePage = new RevocationResponsePage();
            List<RevocationOrder> revocationOrders = new ArrayList<>();
            Pageable pageable = null;
            Page<RevocationOrder> page = null;
            //     Sort sort = Sort.by(Sort.Direction.ASC, "id");
            Short status = 2;
            if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0) {
                if (getAllRevocation.getPageIndex() == 0 && getAllRevocation.getPageSize() == 0) {
                    revocationOrders = revocationRepository.findAllByStatus(status);
                }
                if (revocationOrders.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", revocationOrders);
                    for (RevocationOrder blackList : revocationOrders) {
                        RevocationResponse revocationResponse = new RevocationResponse();
                        BeanUtils.copyProperties(blackList, revocationResponse);
                        revocationResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                        revocationResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                        responsePayloadList.add(revocationResponse);
                    }
                    revocationResponsePage.setRevocationResponseList(responsePayloadList);
                    DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    DeleteRevocationMap.put(DataConstant.RESPONSE_BODY, revocationResponsePage);
                    log.info("Record found! status - {}", revocationResponsePage);
                    return DeleteRevocationMap;
                } else {
                    DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    DeleteRevocationMap.put(DataConstant.RESPONSE_BODY, revocationResponsePage);
                    log.info("Record not found! status - {}");
                    return DeleteRevocationMap;
                }
            } else if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != null) {
                if (getAllRevocation.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllRevocation.getPageIndex(), getAllRevocation.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllRevocation.getPageIndex() != null && getAllRevocation.getPageSize() != 0) {
                        page = revocationRepository.findAllByStatus(status, pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", revocationOrders);
                        revocationOrders = page.getContent();
                        int index = 0;
                        for (RevocationOrder blackList : revocationOrders) {
                            RevocationResponse revocationResponse = new RevocationResponse();
                            BeanUtils.copyProperties(blackList, revocationResponse);
                            revocationResponse.setCreatedDate(DateUtil.convertDateToStringDate(blackList.getCreatedDate()));
                            revocationResponse.setUpdatedDate(DateUtil.convertDateToStringDate(blackList.getUpdatedDate()));
                            //for frontEnd team pagination
                            if (getAllRevocation.getPageIndex() == 0) {
                                revocationResponse.setSerialNo(index + 1);
                                index++;
                            } else {
                                revocationResponse.setSerialNo((getAllRevocation.getPageSize() * getAllRevocation.getPageIndex()) + (index + 1));
                                index++;
                            }
                            responsePayloadList.add(revocationResponse);
                        }
                        revocationResponsePage.setRevocationResponseList(responsePayloadList);
                        revocationResponsePage.setPageIndex(page.getNumber());
                        revocationResponsePage.setPageSize(page.getSize());
                        revocationResponsePage.setTotalElement(page.getTotalElements());
                        revocationResponsePage.setTotalPages(page.getTotalPages());
                        revocationResponsePage.setIsFirstPage(page.isFirst());
                        revocationResponsePage.setIsLastPage(page.isLast());

                        DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        DeleteRevocationMap.put(DataConstant.RESPONSE_BODY, revocationResponsePage);
                        log.info("Record found! status - {}", revocationResponsePage);
                    } else {
                        DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        DeleteRevocationMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return DeleteRevocationMap;
                    }
                } else {
                    DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllRevocation);
                    return DeleteRevocationMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return DeleteRevocationMap;
        } catch (Exception e) {
            DeleteRevocationMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            DeleteRevocationMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return DeleteRevocationMap;
        }
        return DeleteRevocationMap;
    }


    public Map<String, Object> addAwardedTender(AwardedTenderRequest awardedTenderRequest) throws IOException {
        Map<String, Object> awardedMap = new HashMap<>();
        try {
            if (awardedTenderRequest.getDepartment() != null || !awardedTenderRequest.getDepartment().trim().isEmpty()
                    && awardedTenderRequest.getYearOfPurchase() != null && !awardedTenderRequest.getYearOfPurchase().trim().isEmpty()
                    && awardedTenderRequest.getNitFile() != null || !awardedTenderRequest.getNitFile().trim().isEmpty()) {
                AwardedTender tender = new AwardedTender();
                tender.setDepartment(awardedTenderRequest.getDepartment());
                tender.setNitFile(awardedTenderRequest.getNitFile());
                tender.setYearOfPurchase(awardedTenderRequest.getYearOfPurchase());
                tender.setCreatedDate(new Date());
                tender.setStatus(DataConstant.ONE);
                AwardedTender save = awardedTenderRepository.save(tender);
                awardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                awardedMap.put(DataConstant.MESSAGE, DataConstant.AWARDED_TENDER_ADDED_SUCCESSFULLY);
                awardedMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.AWARDED_TENDER_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return awardedMap;
            } else {
                awardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                awardedMap.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                awardedMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory", DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return awardedMap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }
        return awardedMap;
    }


    public Map<String, Object> updateAwardedTender(AwardedUpdateRequest awardedUpdateRequest) throws IOException {
        Map<String, Object> updateMap = new HashMap<>();
        try {
            Optional<AwardedTender> tenderId = awardedTenderRepository.findById(awardedUpdateRequest.getAwardedTenderId());
            if (tenderId.isPresent()) {
                AwardedTender awardedTender = tenderId.get();
                List<Purchase> purchaseList = new ArrayList<>();
                Purchase purchase = new Purchase();
                List<Purchase> amendmentListold = tenderId.get().getPurchaseList();
                for (Purchase amendmentold : amendmentListold) {
                    BeanUtils.copyProperties(amendmentListold, amendmentold);
                    purchaseList.add(amendmentold);
                }
                List<PurchaseRequest> amendmentRequestPayloads = awardedUpdateRequest.getPurchaseRequests();
                for (PurchaseRequest purchaseRequest : amendmentRequestPayloads) {

                    BeanUtils.copyProperties(purchaseRequest, purchase);

                    purchaseRepository.save(purchase);
                    purchaseList.add(purchase);
                }
                if (awardedUpdateRequest.getNitFile() != null && !awardedUpdateRequest.getNitFile().trim().isEmpty()) {
                    awardedTender.setNitFile(awardedUpdateRequest.getNitFile());
                }
                if (awardedUpdateRequest.getYearOfPurchase() != null && !awardedUpdateRequest.getYearOfPurchase().trim().isEmpty()) {
                    awardedTender.setYearOfPurchase(awardedUpdateRequest.getYearOfPurchase());
                }
                awardedTender.setPurchaseList(purchaseList);
                AwardedTender save = awardedTenderRepository.save(awardedTender);
                updateMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                updateMap.put(DataConstant.MESSAGE, DataConstant.AWARDED_TENDER_UPDATE_SUCCESSFULLY);
                updateMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.AWARDED_TENDER_UPDATE_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return updateMap;

            } else {
                updateMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                updateMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                updateMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("rocord not found", DataConstant.RECORD_NOT_FOUND_MESSAGE);
                return updateMap;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());

        }
        return updateMap;
    }


    public Map<String, Object> getAllAwardedTender(GetAllAwardedTender getAllTender) {
        Map<String, Object> getAllAwardedMap = new HashMap<>();
        try {
            if (getAllTender.getPageIndex() == null && getAllTender.getPageSize() == null) {
                getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                getAllAwardedMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return getAllAwardedMap;
            }
            List<AwardedTenderResponse> tenderResponsePayloadList = new ArrayList<>();
            AwardedResponsePage tenderResponsePage = new AwardedResponsePage();
            List<AwardedTender> tenderList = new ArrayList<>();
            Pageable pageable = null;
            Page<AwardedTender> page = null;
            Short status =2;

            if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0) {
                if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().trim().isEmpty()) {
                    tenderList = awardedTenderRepository.findAllByStatusNot(status);
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText() != null && !getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy() == null || getAllTender.getOrderBy().trim().isEmpty()) {
                    tenderList = awardedTenderRepository.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(getAllTender.getSearchText());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && !getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                    tenderList = awardedTenderRepository.searchAndOrderByASC(getAllTender.getColumnName());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && !getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                    tenderList = awardedTenderRepository.searchAndOrderByDESC(getAllTender.getColumnName());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText() != null && !getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && !getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                    tenderList = awardedTenderRepository.findASC(getAllTender.getSearchText(), getAllTender.getColumnName());
                } else if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0 && getAllTender.getSearchText() != null && !getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                    tenderList = awardedTenderRepository.findDESC(getAllTender.getSearchText(), getAllTender.getColumnName());
                }
                if (tenderList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", tenderList);
                    for (AwardedTender tender : tenderList) {
                        AwardedTenderResponse tenderResponsePayload = new AwardedTenderResponse();
                        BeanUtils.copyProperties(tender, tenderResponsePayload);
                        // add amendment
                        List<Purchase> amendmentList = tender.getPurchaseList();
                        tenderResponsePayload.setPurchaseList(amendmentList);
                        tenderResponsePayloadList.add(tenderResponsePayload);
                    }
                    tenderResponsePage.setAwardedTenderResponseList(tenderResponsePayloadList);
                    getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record found! status - {}", tenderResponsePage);
                    return getAllAwardedMap;
                } else {
                    getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record not found! status - {}");
                    return getAllAwardedMap;
                }
            } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null) {
                if (getAllTender.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllTender.getPageIndex(), getAllTender.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != 0 && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().trim().isEmpty()) {
                        page = awardedTenderRepository.findAllByStatusNot(status,pageable);

                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText() != null && !getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().trim().isEmpty()) {
                        page = awardedTenderRepository.findAllByUserName(getAllTender.getSearchText(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && !getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                        page = awardedTenderRepository.searchAndOrderByASC(getAllTender.getColumnName(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && !getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                        page = awardedTenderRepository.searchAndOrderByDESC(getAllTender.getColumnName(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText() != null && !getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && !getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().equals(DataConstant.ASC)) {
                        page = awardedTenderRepository.findASC(getAllTender.getSearchText(), getAllTender.getColumnName(), pageable);
                    } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null && getAllTender.getSearchText() != null && !getAllTender.getSearchText().trim().isEmpty() && getAllTender.getColumnName() != null && !getAllTender.getColumnName().trim().isEmpty() && getAllTender.getOrderBy().equals(DataConstant.DESC)) {
                        page = awardedTenderRepository.findDESC(getAllTender.getSearchText(), getAllTender.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", tenderList);
                        tenderList = page.getContent();
                        int index = 0;
                        for (AwardedTender tender : tenderList) {
                            AwardedTenderResponse tenderResponsePayload = new AwardedTenderResponse();
                            BeanUtils.copyProperties(tender, tenderResponsePayload);
                            // add amendment
                            List<Purchase> amendmentList = tender.getPurchaseList();
                            tenderResponsePayload.setPurchaseList(amendmentList);
//                           //for frontEnd team pagination
                            if (getAllTender.getPageIndex() == 0) {
                                tenderResponsePayload.setSerialNo(index + 1);
                                index++;
                            } else {
                                tenderResponsePayload.setSerialNo((getAllTender.getPageSize() * getAllTender.getPageIndex()) + (index + 1));
                                index++;
                            }
                            tenderResponsePayloadList.add(tenderResponsePayload);
                        }
                        tenderResponsePage.setAwardedTenderResponseList(tenderResponsePayloadList);
                        tenderResponsePage.setPageIndex(page.getNumber());
                        tenderResponsePage.setPageSize(page.getSize());
                        tenderResponsePage.setTotalElement(page.getTotalElements());
                        tenderResponsePage.setTotalPages(page.getTotalPages());
                        tenderResponsePage.setIsFirstPage(page.isFirst());
                        tenderResponsePage.setIsLastPage(page.isLast());

                        getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record found! status - {}", tenderResponsePage);
                    } else {
                        getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllTender);
                    return getAllAwardedMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            getAllAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllAwardedMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return getAllAwardedMap;
    }

    public Map<String, Object> awrdedTenderDeleteById(Long id) {
        Map<String, Object> awrdedDeleteMap = new HashMap<>();
        try {
            Optional<AwardedTender> byId = awardedTenderRepository.findById(id);
            if (byId.isPresent()) {
                AwardedTender awardedTender = byId.get();
                awardedTender.setStatus(DataConstant.TWO);
                awardedTenderRepository.save(awardedTender);
                log.info("awarded  tender deleted successfully ");
                awrdedDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                awrdedDeleteMap.put(DataConstant.MESSAGE, DataConstant.AWARDED_TENDER_DELETED_SUCCESSFULLY);
                return awrdedDeleteMap;
            } else {
                log.error("awarded tender id not found ");
                awrdedDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                awrdedDeleteMap.put(DataConstant.MESSAGE, DataConstant.AWARDED_TENDER_ID_NOT_FOUND);
                return awrdedDeleteMap;
            }
        } catch (Exception e) {
            log.error("exception in awarded tender");
        }
        return awrdedDeleteMap;
    }

    public Map<String, Object> getAllDeleteAwardedTender(GetAllDeleteAwardedTender getAllTender) {
        Map<String, Object> getAllDeleteAwardedMap = new HashMap<>();
        try {
            if (getAllTender.getPageIndex() == null && getAllTender.getPageSize() == null) {
                getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                getAllDeleteAwardedMap.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return getAllDeleteAwardedMap;
            }
            List<AwardedTenderResponse> tenderResponsePayloadList = new ArrayList<>();
            AwardedResponsePage tenderResponsePage = new AwardedResponsePage();
            List<AwardedTender> tenderList = new ArrayList<>();
            Pageable pageable = null;
            Page<AwardedTender> page = null;
            Short status = 2;
            if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0) {
                if (getAllTender.getPageIndex() == 0 && getAllTender.getPageSize() == 0) {
                    tenderList = awardedTenderRepository.findAllByStatus(status);
                }
                if (tenderList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", tenderList);
                    for (AwardedTender tender : tenderList) {
                        AwardedTenderResponse tenderResponsePayload = new AwardedTenderResponse();
                        BeanUtils.copyProperties(tender, tenderResponsePayload);
                        // add amendment
                        List<Purchase> amendmentList = tender.getPurchaseList();
                        tenderResponsePayload.setPurchaseList(amendmentList);
                        tenderResponsePayloadList.add(tenderResponsePayload);
                    }
                    tenderResponsePage.setAwardedTenderResponseList(tenderResponsePayloadList);
                    getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllDeleteAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record found! status - {}", tenderResponsePage);
                    return getAllDeleteAwardedMap;
                } else {
                    getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllDeleteAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                    log.info("Record not found! status - {}");
                    return getAllDeleteAwardedMap;
                }
            } else if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != null) {
                if (getAllTender.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllTender.getPageIndex(), getAllTender.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllTender.getPageIndex() != null && getAllTender.getPageSize() != 0) {
                        page = awardedTenderRepository.findAllByStatus(status, pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", tenderList);
                        tenderList = page.getContent();
                        int index = 0;
                        for (AwardedTender tender : tenderList) {
                            AwardedTenderResponse tenderResponsePayload = new AwardedTenderResponse();
                            BeanUtils.copyProperties(tender, tenderResponsePayload);
                            // add amendment
                            List<Purchase> amendmentList = tender.getPurchaseList();
                            tenderResponsePayload.setPurchaseList(amendmentList);
//                           //for frontEnd team pagination
                            if (getAllTender.getPageIndex() == 0) {
                                tenderResponsePayload.setSerialNo(index + 1);
                                index++;
                            } else {
                                tenderResponsePayload.setSerialNo((getAllTender.getPageSize() * getAllTender.getPageIndex()) + (index + 1));
                                index++;
                            }
                            tenderResponsePayloadList.add(tenderResponsePayload);
                        }
                        tenderResponsePage.setAwardedTenderResponseList(tenderResponsePayloadList);
                        tenderResponsePage.setPageIndex(page.getNumber());
                        tenderResponsePage.setPageSize(page.getSize());
                        tenderResponsePage.setTotalElement(page.getTotalElements());
                        tenderResponsePage.setTotalPages(page.getTotalPages());
                        tenderResponsePage.setIsFirstPage(page.isFirst());
                        tenderResponsePage.setIsLastPage(page.isLast());

                        getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllDeleteAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record found! status - {}", tenderResponsePage);
                    } else {
                        getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllDeleteAwardedMap.put(DataConstant.RESPONSE_BODY, tenderResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllTender);
                    return getAllDeleteAwardedMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            getAllDeleteAwardedMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllDeleteAwardedMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return getAllDeleteAwardedMap;
    }


}
