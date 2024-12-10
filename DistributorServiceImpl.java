package com.janaushadhi.adminservice.serviceimpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.janaushadhi.adminservice.entity.LocateDistributer;
import com.janaushadhi.adminservice.externalservices.KendraService;
import com.janaushadhi.adminservice.repository.DistributorRepository;
import com.janaushadhi.adminservice.requestpayload.DistributerDeletePayload;
import com.janaushadhi.adminservice.requestpayload.DistributorRequestPayload;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class DistributorServiceImpl {

    @Autowired
    private KendraService kendraService;

    @Autowired
    private DistributorRepository distributorRepository;

    public Map<String, Object> addDistributor(DistributorRequestPayload distributorRequestPayload) throws IOException {
        Map<String, Object> addDistributorMap = new HashMap<>();
        try {

        if(distributorRequestPayload.getId()!=0 && distributorRequestPayload.getId()!=null) {
            Optional<LocateDistributer> optionalDistributor = distributorRepository.findById(distributorRequestPayload.getId());
            if (optionalDistributor.isPresent()) {
        LocateDistributer existingDistributor = optionalDistributor.get();
        Optional<LocateDistributer> locateDistributerOptional = distributorRepository.findByCodeOrEmail(distributorRequestPayload.getCode(), distributorRequestPayload.getEmail());
        if (locateDistributerOptional.isPresent()) {
            addDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
            addDistributorMap.put(DataConstant.MESSAGE, DataConstant.CODE_OR_EMAIL_ALREADY_EXIST);
            addDistributorMap.put(DataConstant.RESPONSE_BODY, null);
            log.info("Distributor code or email already exists: {}", DataConstant.CODE_OR_EMAIL_ALREADY_EXIST);
            return addDistributorMap;
        }
        BeanUtils.copyProperties(distributorRequestPayload, existingDistributor);
        existingDistributor.setUpdateddate(new Date());
        LocateDistributer savedDistributor = distributorRepository.save(existingDistributor);
        addDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
        addDistributorMap.put(DataConstant.MESSAGE, DataConstant.DISTRIBUTOR_UPDATED);
        addDistributorMap.put(DataConstant.RESPONSE_BODY, savedDistributor);
        log.info("Distributor updated successfully: {}", DataConstant.DISTRIBUTOR_UPDATED);
        return addDistributorMap;
    } else {
          addDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
          addDistributorMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                addDistributorMap.put(DataConstant.RESPONSE_BODY, null);
        log.info("Distributor with id {} not found: {}", distributorRequestPayload.getId(), DataConstant.RECORD_NOT_FOUND_MESSAGE);
        return addDistributorMap;
    }
}
            if (distributorRequestPayload.getNameOfFirm()!=null ||! distributorRequestPayload.getNameOfFirm().trim().equals("")
                    && distributorRequestPayload.getEmail()!=null||! distributorRequestPayload.getEmail().trim().equals("")
                    &&distributorRequestPayload.getDistributorAddress()!=null||!distributorRequestPayload.getDistributorAddress().trim().equals("")
                    && distributorRequestPayload.getStateId()!=null&& distributorRequestPayload.getStateId()!=0 &&distributorRequestPayload.getDistrictId()!=null  && distributorRequestPayload.getDistrictId()!=0
                    && !distributorRequestPayload.getContactNumber().trim().equals("")  && distributorRequestPayload.getPinCode()!=0 &&distributorRequestPayload.getCityId()!=null &&distributorRequestPayload.getCityId()!=0) {
                LocateDistributer distributer = new LocateDistributer();
             Optional<LocateDistributer> locateDistributerOptional = distributorRepository.findByCodeOrEmail(distributorRequestPayload.getCode(),distributorRequestPayload.getEmail());
             if(locateDistributerOptional.isPresent()){
                 addDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                 addDistributorMap.put(DataConstant.MESSAGE, DataConstant.CODE_OR_EMAIL_ALREADY_EXIST);
                 addDistributorMap.put(DataConstant.RESPONSE_BODY, null);
                 log.info( "distributor code already exist",DataConstant.CODE_OR_EMAIL_ALREADY_EXIST);
                 return addDistributorMap;
             }
                distributer.setCode(distributorRequestPayload.getCode());
                distributer.setNameOfFirm(distributorRequestPayload.getNameOfFirm());
                distributer.setDistrictId(distributorRequestPayload.getDistrictId());
                distributer.setCityId(distributorRequestPayload.getCityId());
                distributer.setDistributorAddress(distributorRequestPayload.getDistributorAddress());
                distributer.setContactNumber(distributorRequestPayload.getContactNumber());
                distributer.setStateId(distributorRequestPayload.getStateId());
                distributer.setEmail(distributorRequestPayload.getEmail());
                distributer.setPinCode(distributorRequestPayload.getPinCode());
                distributer.setCreatedDate(new Date());
                distributer.setStatus(DataConstant.ONE);
                LocateDistributer save = distributorRepository.save(distributer);
                addDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                addDistributorMap.put(DataConstant.MESSAGE, DataConstant.DISTRIBUTOR_ADDED_SUCCESSFULLY);
                addDistributorMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.DISTRIBUTOR_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return addDistributorMap;
            } else {
                addDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                addDistributorMap.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                addDistributorMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory",DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return addDistributorMap;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());	
        }
        return addDistributorMap;
    }


    public Map<String, Object> distributorStatusUpdate(Long id, short status) {
        Map<String, Object> distributorStatusMap = new HashMap<>();
        Optional<LocateDistributer> distributerupdate = distributorRepository.findById(id);
        if (distributerupdate.isPresent()) {
            log.info("  distributor Record found! status - {}",distributerupdate );
            LocateDistributer distributerData = distributerupdate.get();
            BeanUtils.copyProperties(distributerupdate, distributerData);
            distributerData.setStatus(status);

            LocateDistributer distributorstatus = distributorRepository.save(distributerData);
            distributorStatusMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            distributorStatusMap.put(DataConstant.MESSAGE, DataConstant.TENDER_STATUS_UPDATE);
            distributorStatusMap.put(DataConstant.RESPONSE_BODY, distributorstatus);
            log.info("distributor status update- {}", distributorstatus);
            return distributorStatusMap;
        } else {
            distributorStatusMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
            distributorStatusMap.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
            distributorStatusMap.put(DataConstant.RESPONSE_BODY, null);
            log.error("distributor id not present ", id);
            return distributorStatusMap;
        }
    }


    public Map<String, Object> getByDistributorId(Long id) {
        Map<String, Object> getByDistributorMap = new HashMap<>();
        try {
            Optional<LocateDistributer> distributer = distributorRepository.findById(id);
            if (!distributer.isEmpty()) {
                getByDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                getByDistributorMap.put(DataConstant.MESSAGE, DataConstant.DISTRIBUTOR_FOUNDED_SUCCESSFULLY);
                getByDistributorMap.put(DataConstant.RESPONSE_BODY,distributer );
                log.info("distributor  found- {}", distributer);
                return getByDistributorMap;
            } else {
                getByDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getByDistributorMap.put(DataConstant.RESPONSE_BODY, null);
                getByDistributorMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                log.info("distributor not found", DataConstant.RECORD_NOT_FOUND_MESSAGE);
                return getByDistributorMap;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
            getByDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getByDistributorMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            return getByDistributorMap;
        }
    }


    public Map<String,Object> getAllDistributor(GetAllDistributor getAllDistributor) {
        Map<String, Object> getAllDistributorMap = new HashMap<>();
        try {
            if(getAllDistributor.getPageIndex()==null && getAllDistributor.getPageSize()==null) {
                getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                getAllDistributorMap.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return getAllDistributorMap;
            }
            DistributorResponsePage distributorResponsePage1 = new DistributorResponsePage();
            List<LocateDistributer> locateDistributerList = new ArrayList<>();
            List<DistributorResponsePayload> responsePayloadList = new ArrayList<>();
            Pageable pageable = null;
            Page<LocateDistributer> page = null;
            Short status=2;
           // Sort sort = Sort.by(Sort.Direction.ASC, "id");

            if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0) {
                if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getSearchText() == null || getAllDistributor.getSearchText().trim().isEmpty() && getAllDistributor.getColumnName() == null || getAllDistributor.getColumnName().trim().isEmpty() && getAllDistributor.getOrderBy() == null || getAllDistributor.getOrderBy().trim().isEmpty()) {
                    locateDistributerList = distributorRepository.findAllByStatusNot(status);
                } else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getSearchText() != null && getAllDistributor.getColumnName() == null || getAllDistributor.getColumnName().trim().isEmpty() && getAllDistributor.getOrderBy() == null || getAllDistributor.getOrderBy().trim().isEmpty()) {
                    locateDistributerList = distributorRepository.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(getAllDistributor.getSearchText());
                } else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getSearchText() == null || getAllDistributor.getSearchText().trim().isEmpty() && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.ASC)) {
                    locateDistributerList = distributorRepository.searchAndOrderByASC(getAllDistributor.getColumnName());
                } else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getSearchText() == null || getAllDistributor.getSearchText().trim().isEmpty() && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.DESC)) {
                    locateDistributerList = distributorRepository.searchAndOrderByDESC(getAllDistributor.getColumnName());
                } else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getSearchText() != null && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.ASC)) {
                    locateDistributerList = distributorRepository.findASC(getAllDistributor.getSearchText(), getAllDistributor.getColumnName());
                } else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getSearchText() != null && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.DESC)) {
                    locateDistributerList = distributorRepository.findDESC(getAllDistributor.getSearchText(), getAllDistributor.getColumnName());
                }
                if (locateDistributerList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", locateDistributerList);
                    for (LocateDistributer distributerDetail : locateDistributerList) {
                        DistributorResponsePayload distributorResponsePayload = new DistributorResponsePayload();
                        BeanUtils.copyProperties(distributerDetail, distributorResponsePayload);
                        distributorResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(distributerDetail.getCreatedDate()));
                        distributorResponsePayload.setUpdateddate(DateUtil.convertDateToStringDate(distributerDetail.getUpdateddate()));
                        Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(distributerDetail.getDistrictId());
                        if(distributerDetail.getDistrictId()!=null && distributerDetail.getDistrictId()!=0) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                            distributorResponsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                            distributorResponsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                        }
                        responsePayloadList.add(distributorResponsePayload);
                    }
                    distributorResponsePage1.setDistributorResponsePayloads(responsePayloadList);
                    getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllDistributorMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                    log.info("Record found! status - {}", distributorResponsePage1);
                    return getAllDistributorMap;
                } else {
                    getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllDistributorMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                    log.info("Record not found! status - {}");
                    return getAllDistributorMap;
                }
            } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null) {
                if (getAllDistributor.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllDistributor.getPageIndex(), getAllDistributor.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != 0 && getAllDistributor.getSearchText() == null || getAllDistributor.getSearchText().trim().isEmpty() && getAllDistributor.getColumnName() == null || getAllDistributor.getColumnName().trim().isEmpty() && getAllDistributor.getOrderBy() == null || getAllDistributor.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = distributorRepository.findAllByStatusNot(status,pageable);

                    } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null && getAllDistributor.getSearchText() != null && getAllDistributor.getColumnName() == null || getAllDistributor.getColumnName().trim().isEmpty() && getAllDistributor.getOrderBy() == null || getAllDistributor.getOrderBy().trim().isEmpty()) {
                        page = distributorRepository.findAllByUserName(getAllDistributor.getSearchText(), pageable);
                    } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null &&  getAllDistributor.getSearchText().trim().isEmpty() && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.ASC)) {
                        page = distributorRepository.searchAndOrderByASC(getAllDistributor.getColumnName(), pageable);
                    } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null &&  getAllDistributor.getSearchText().trim().isEmpty() && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.DESC)) {
                        page = distributorRepository.searchAndOrderByDESC(getAllDistributor.getColumnName(), pageable);
                    } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null && getAllDistributor.getSearchText() != null && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.ASC)) {
                        page = distributorRepository.findASC(getAllDistributor.getSearchText(), getAllDistributor.getColumnName(), pageable);
                    } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null && getAllDistributor.getSearchText() != null && getAllDistributor.getColumnName() != null && getAllDistributor.getOrderBy().equals(DataConstant.DESC)) {
                        page = distributorRepository.findDESC(getAllDistributor.getSearchText(), getAllDistributor.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", locateDistributerList);
                        locateDistributerList = page.getContent();
                        int index=0;
                        for (LocateDistributer distributers : locateDistributerList) {
                            DistributorResponsePayload distributorResponsePayload = new DistributorResponsePayload();
                            BeanUtils.copyProperties(distributers, distributorResponsePayload);
                            distributorResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(distributers.getCreatedDate()));
                            distributorResponsePayload.setUpdateddate(DateUtil.convertDateToStringDate(distributers.getUpdateddate()));
                            Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(distributers.getDistrictId());
                            if(distributers.getDistrictId()!=null && distributers.getDistrictId()!=0) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                                distributorResponsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                                distributorResponsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                            }
                       	    if(getAllDistributor.getPageIndex() == 0) {
                            	distributorResponsePayload.setSerialNo(index+1);
                        		index++;
                        	}else {
                        		distributorResponsePayload.setSerialNo((getAllDistributor.getPageSize()*getAllDistributor.getPageIndex())+(index+1));
                        		index++;
                        	}
                            responsePayloadList.add(distributorResponsePayload);
                        }
                        distributorResponsePage1.setDistributorResponsePayloads(responsePayloadList);
                        distributorResponsePage1.setPageIndex(page.getNumber());
                        distributorResponsePage1.setPageSize(page.getSize());
                        distributorResponsePage1.setTotalElement(page.getTotalElements());
                        distributorResponsePage1.setTotalPages(page.getTotalPages());
                        distributorResponsePage1.setIsFirstPage(page.isFirst());
                        distributorResponsePage1.setIsLastPage(page.isLast());

                        getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllDistributorMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                        log.info("Record found! status - {}", distributorResponsePage1);
                    } else {
                        getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllDistributorMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return getAllDistributorMap;
                    }
                } else {
                    getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllDistributor);
                    return getAllDistributorMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return getAllDistributorMap;
        } catch (Exception e) {
            getAllDistributorMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllDistributorMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return getAllDistributorMap;
        }
        return getAllDistributorMap;
    }




    public Map<String,Object> getAllDistributorByStateAndDistrict(GetAllDistributors getAllDistributor) {
        Map<String, Object> stateAndDistrictMap = new HashMap<>();
        try {
            if(getAllDistributor.getPageIndex()==null && getAllDistributor.getPageSize()==null) {
                stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                stateAndDistrictMap.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return stateAndDistrictMap;
            }
            List<DistributorResponsePayload> responsePayloadList = new ArrayList<>();
            DistributorResponsePage distributorResponsePage1 = new DistributorResponsePage();
            List<LocateDistributer> locateDistributerList = new ArrayList<>();
            Pageable pageable = null;
            Page<LocateDistributer> page = null;
            Sort sort = Sort.by(Sort.Direction.ASC, "id");

            if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0) {
                if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0  && getAllDistributor.getStateId()==0 && getAllDistributor.getDistrictId()==0) {
                    locateDistributerList = distributorRepository.findAll(sort);
                }else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getStateId() != null    && getAllDistributor.getStateId() !=0  && getAllDistributor.getDistrictId() != null    && getAllDistributor.getDistrictId() !=0&& getAllDistributor.getPinCode()!=null && getAllDistributor.getPinCode()!=0) {
                    locateDistributerList = distributorRepository.findAllByStateIdAndDistrictIdAndPinCode(getAllDistributor.getStateId(),getAllDistributor.getDistrictId(),getAllDistributor.getPinCode(),sort);
                }
                else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getStateId() != null    && getAllDistributor.getStateId() !=0  && getAllDistributor.getDistrictId() != null    && getAllDistributor.getDistrictId() !=0) {
                    locateDistributerList = distributorRepository.findAllByStateIdAndDistrictId(getAllDistributor.getStateId(),getAllDistributor.getDistrictId(),sort);
                }
                else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getStateId() != null    && getAllDistributor.getStateId() !=0  && getAllDistributor.getPinCode() != null    && getAllDistributor.getPinCode() !=0) {
                    locateDistributerList = distributorRepository.findAllByStateIdAndPinCode(getAllDistributor.getStateId(),getAllDistributor.getPinCode(),sort);
                }
                else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getStateId()!=null && getAllDistributor.getStateId()!=0) {
                    locateDistributerList = distributorRepository.findAllByStateId(getAllDistributor.getStateId(),sort);
                }
                else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 && getAllDistributor.getPinCode()!=null && getAllDistributor.getPinCode()!=0) {
                    locateDistributerList = distributorRepository.findAllByPinCode(getAllDistributor.getPinCode(),sort);
                }
                if (locateDistributerList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", locateDistributerList);
                    for (LocateDistributer distributer : locateDistributerList) {
                        DistributorResponsePayload distributorResponsePayload = new DistributorResponsePayload();
                        BeanUtils.copyProperties(distributer, distributorResponsePayload);
                        distributorResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(distributer.getCreatedDate()));
                        distributorResponsePayload.setUpdateddate(DateUtil.convertDateToStringDate(distributer.getUpdateddate()));
                        Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(distributer.getDistrictId());
                        if(distributer.getDistrictId()!=null && distributer.getDistrictId()!=0) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                            distributorResponsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                            distributorResponsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                        }
                        responsePayloadList.add(distributorResponsePayload);
                    }
                    distributorResponsePage1.setDistributorResponsePayloads(responsePayloadList);
                    stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    stateAndDistrictMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                    log.info("Record found! status - {}", distributorResponsePage1);
                    return stateAndDistrictMap;
                } else {
                    stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    stateAndDistrictMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                    log.info("Record not found! status - {}");
                    return stateAndDistrictMap;
                }
            } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null) {
                if (getAllDistributor.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllDistributor.getPageIndex(), getAllDistributor.getPageSize(),sort);
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != 0  &&  getAllDistributor.getStateId()==0 &&   getAllDistributor.getDistrictId()==0 && getAllDistributor.getPinCode()==0) {
                    	page = distributorRepository.findAll(pageable);

                    } else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() > 0 && getAllDistributor.getStateId() != null    && getAllDistributor.getStateId() !=0  && getAllDistributor.getDistrictId() != null    && getAllDistributor.getDistrictId() !=0&& getAllDistributor.getPinCode()!=null && getAllDistributor.getPinCode()!=0) {
                        page = distributorRepository.findAllByStateIdAndDistrictIdAndPinCode(getAllDistributor.getStateId(),getAllDistributor.getDistrictId(),getAllDistributor.getPinCode(),pageable);
                    }
                    else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() > 0 && getAllDistributor.getStateId() != null    && getAllDistributor.getStateId() !=0  && getAllDistributor.getDistrictId() != null    && getAllDistributor.getDistrictId() !=0) {
                        page = distributorRepository.findAllByStateIdAndDistrictId(getAllDistributor.getStateId(),getAllDistributor.getDistrictId(),pageable);
                    }else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() > 0 && getAllDistributor.getStateId() != null    && getAllDistributor.getStateId() !=0  && getAllDistributor.getPinCode()!=null && getAllDistributor.getPinCode()!=0) {
                        page = distributorRepository.findAllByStateIdAndPinCode(getAllDistributor.getStateId(),getAllDistributor.getPinCode(),pageable);
                    }
                    else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() > 0 && getAllDistributor.getStateId()!=null && getAllDistributor.getStateId()!=0) {
                        page = distributorRepository.findByStateId(getAllDistributor.getStateId(),pageable);
                    }
                    else if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() > 0 && getAllDistributor.getPinCode()!=null && getAllDistributor.getPinCode()!=0) {
                        page = distributorRepository.findByPinCode(getAllDistributor.getPinCode(),pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", locateDistributerList);
                        locateDistributerList = page.getContent();
                        int index=0;
                        for (LocateDistributer distributerDetail : locateDistributerList) {
                            DistributorResponsePayload distributorResponsePayload = new DistributorResponsePayload();
                            BeanUtils.copyProperties(distributerDetail, distributorResponsePayload);
                            distributorResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(distributerDetail.getCreatedDate()));
                            distributorResponsePayload.setUpdateddate(DateUtil.convertDateToStringDate(distributerDetail.getUpdateddate()));
                            Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(distributerDetail.getDistrictId());
                            if(distributerDetail.getDistrictId()!=null && distributerDetail.getDistrictId()!=0) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                                distributorResponsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                                distributorResponsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                            }
                          //for frontEnd team pagination
                            if(getAllDistributor.getPageIndex() == 0) {
                            	distributorResponsePayload.setSerialNo(index+1);
                        		index++;
                        	}else {
                        		distributorResponsePayload.setSerialNo((getAllDistributor.getPageSize()*getAllDistributor.getPageIndex())+(index+1));
                        		index++;
                        	}
                            responsePayloadList.add(distributorResponsePayload);
                        }
                        distributorResponsePage1.setDistributorResponsePayloads(responsePayloadList);
                        distributorResponsePage1.setPageIndex(page.getNumber());
                        distributorResponsePage1.setPageSize(page.getSize());
                        distributorResponsePage1.setTotalElement(page.getTotalElements());
                        distributorResponsePage1.setTotalPages(page.getTotalPages());
                        distributorResponsePage1.setIsFirstPage(page.isFirst());
                        distributorResponsePage1.setIsLastPage(page.isLast());

                        stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        stateAndDistrictMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                        log.info("Record found! status - {}", distributorResponsePage1);
                    } else {
                        stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        stateAndDistrictMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return stateAndDistrictMap;
                    }
                } else {
                    stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllDistributor);
                    return stateAndDistrictMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return stateAndDistrictMap;
        } catch (Exception e) {
            stateAndDistrictMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            stateAndDistrictMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return stateAndDistrictMap;
        }
        return stateAndDistrictMap;
    }


    public Map<Object, Object> uploadCsvFileToDataBase(MultipartFile docfile) throws IOException {
        Map<Object, Object> csvMap = new HashMap<>();

        List<LocateDistributer> distributerList = CsvReadder.readCsvs(docfile.getInputStream());
        List<LocateDistributer> distributerLisNew=new ArrayList<>();
        try {
            if (!distributerList.isEmpty()) {

                List<DistributorResponsePayload> distributorResponsePayloadList = new ArrayList<>();
                for (LocateDistributer locateDistributer : distributerList) {
                    LocateDistributer distributer = new LocateDistributer();
                    distributer.setId(locateDistributer.getId());
                    distributer.setNameOfFirm(locateDistributer.getNameOfFirm());
                    distributer.setDistributorAddress(locateDistributer.getDistributorAddress());
                    distributer.setEmail(locateDistributer.getEmail());
                    distributer.setCode(locateDistributer.getCode());
                    distributer.setDistrictId(locateDistributer.getDistrictId());
                    distributer.setUpdateddate(locateDistributer.getUpdateddate());
                    distributer.setCreatedDate(locateDistributer.getCreatedDate());
                    distributer.setCityId(locateDistributer.getCityId());
                    distributer.setStateId(locateDistributer.getStateId());
                    distributer.setContactNumber(locateDistributer.getContactNumber());
                    distributer.setPinCode(locateDistributer.getPinCode());
                    distributer.setStatus(DataConstant.ONE);
                    distributerLisNew.add(distributer);
                   // LocateDistributer saveDistributor = distributorRepository.save(distributer);
                    DistributorResponsePayload responsePayload = new DistributorResponsePayload();
                    BeanUtils.copyProperties(distributer,responsePayload);
                    distributorResponsePayloadList.add(responsePayload);
                }
                distributorRepository.saveAll(distributerLisNew);
                csvMap.put(DataConstant.RESPONSE_BODY, distributorResponsePayloadList);
                csvMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                csvMap.put(DataConstant.MESSAGE,DataConstant.DATA_STORE_SUCCESSFULLY );
                log.info("distributor addedd successfully- {}", DataConstant.DATA_STORE_SUCCESSFULLY);
            } else {
                csvMap.put(DataConstant.RESPONSE_BODY, null);
                csvMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                csvMap.put(DataConstant.MESSAGE,DataConstant.FILE_NOT_FOUND );
                log.info("distributor not  found- {}", DataConstant.FILE_NOT_FOUND);

            }
        } catch (Exception e) {
            log.error("Exception occurs while uploading city {}", e.getMessage());
            csvMap.put(DataConstant.RESPONSE_BODY, null);
            csvMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            csvMap.put(DataConstant.MESSAGE, "Csv file content improper data, Please enter proper image url and corresponding data !!");
        }
        return csvMap;
    }


    public Map<String, Object> getDistributourByDistrictId(Long districtId) {
        Map<String, Object> DistrictIdMap = new HashMap<>();
        try {
            List<DistributorResponsePayload> distributorResponsePayloadList = new ArrayList<>();
            List<LocateDistributer> distributer = distributorRepository.findByDistrictId(districtId);
            if (!distributer.isEmpty()) {
                for (LocateDistributer distributerData : distributer) {
                    DistributorResponsePayload responsePayload = new DistributorResponsePayload();
                    BeanUtils.copyProperties(distributerData, responsePayload);
                    Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(distributerData.getDistrictId());
                    if(distributerData.getDistrictId()!=null && distributerData.getDistrictId()!=0) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                        responsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                        responsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                    }
                    distributorResponsePayloadList.add(responsePayload);
                }

                DistrictIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                DistrictIdMap.put(DataConstant.MESSAGE, DataConstant.DISTRIBUTOR_FOUNDED_SUCCESSFULLY);
                DistrictIdMap.put(DataConstant.RESPONSE_BODY,distributorResponsePayloadList );
                log.info("distributor found successfully- {}", DataConstant.DISTRIBUTOR_FOUNDED_SUCCESSFULLY);

                return DistrictIdMap;
            } else {
                DistrictIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                DistrictIdMap.put(DataConstant.RESPONSE_BODY, null);
                DistrictIdMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                log.info("distributor not found - {}", DataConstant.RECORD_NOT_FOUND_MESSAGE);

                return DistrictIdMap;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
            DistrictIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            DistrictIdMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            return DistrictIdMap;
        }
    }



	public Map<Object, Object> deleteDistributerBulk(DistributerDeletePayload distributerIds) {
		Map<Object, Object> BulkMap = new HashMap<>();
        List<LocateDistributer> distributerList = null;
        List<LocateDistributer> list = new ArrayList<>();
        try {
        	if(distributerIds.getDistributerIds().isEmpty()) {
                BulkMap.put(DataConstant.OBJECT_RESPONSE, null);
                BulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                BulkMap.put(DataConstant.MESSAGE, DataConstant.PLEASE_ADD_ID_TO_DELETE);
                log.info(DataConstant.PLEASE_ADD_ID_TO_DELETE);
                return BulkMap;
        	}
        	distributerList = this.distributorRepository.findAllById(distributerIds.getDistributerIds());
            if(distributerList!=null && !distributerList.isEmpty()) {
            for(LocateDistributer distributer:distributerList) {
            	distributorRepository.delete(distributer);
            	list.add(distributer);
            }
            BulkMap.put(DataConstant.RESPONSE_BODY, list);
            BulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            BulkMap.put(DataConstant.MESSAGE, DataConstant.RECORD_DELETED_SUCCESSFULLY);
            log.info(DataConstant.PRODUCT_DELETE_SUCCESSFULLY);
            }
            else{
                BulkMap.put(DataConstant.RESPONSE_BODY, null);
                BulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                BulkMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
            log.info("No distributers found.");
        }
        } catch (Exception e) {
            BulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            BulkMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching distributers: " + e.getMessage());
        }
        return BulkMap;

	}


    public Map<String,Object> getAllDeleteDistributor(GetAllDeleteDistributor getAllDistributor) {
        Map<String, Object> DeleteMap = new HashMap<>();
        try {
            if(getAllDistributor.getPageIndex()==null && getAllDistributor.getPageSize()==null) {
                DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                DeleteMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                DeleteMap.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return DeleteMap;
            }
            List<DistributorResponsePayload> responsePayloadList = new ArrayList<>();
            DistributorResponsePage distributorResponsePage1 = new DistributorResponsePage();
            List<LocateDistributer> locateDistributerList = new ArrayList<>();
            Pageable pageable = null;
            Page<LocateDistributer> page = null;
            Short status=2;
            // Sort sort = Sort.by(Sort.Direction.ASC, "id");

            if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0) {
                if (getAllDistributor.getPageIndex() == 0 && getAllDistributor.getPageSize() == 0 ) {
                    locateDistributerList = distributorRepository.findAllByStatus(status);
                }
                if (locateDistributerList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", locateDistributerList);
                    for (LocateDistributer distributerDelete : locateDistributerList) {
                        DistributorResponsePayload distributorResponsePayload = new DistributorResponsePayload();
                        BeanUtils.copyProperties(distributerDelete, distributorResponsePayload);
                        distributorResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(distributerDelete.getCreatedDate()));
                        distributorResponsePayload.setUpdateddate(DateUtil.convertDateToStringDate(distributerDelete.getUpdateddate()));
                        Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(distributerDelete.getDistrictId());
                        if(distributerDelete.getDistrictId()!=null && distributerDelete.getDistrictId()!=0) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                            distributorResponsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                            distributorResponsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                        }
                        responsePayloadList.add(distributorResponsePayload);
                    }
                    distributorResponsePage1.setDistributorResponsePayloads(responsePayloadList);
                    DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    DeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    DeleteMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                    log.info("Record found! status - {}", distributorResponsePage1);
                    return DeleteMap;
                } else {
                    DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    DeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    DeleteMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                    log.info("Record not found! status - {}");
                    return DeleteMap;
                }
            } else if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != null) {
                if (getAllDistributor.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllDistributor.getPageIndex(), getAllDistributor.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllDistributor.getPageIndex() != null && getAllDistributor.getPageSize() != 0 ) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = distributorRepository.findAllByStatus(status,pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", locateDistributerList);
                        locateDistributerList = page.getContent();
                        int index=0;
                        for (LocateDistributer distributersDelete : locateDistributerList) {
                            DistributorResponsePayload distributorResponsePayload = new DistributorResponsePayload();
                            BeanUtils.copyProperties(distributersDelete, distributorResponsePayload);
                            distributorResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(distributersDelete.getCreatedDate()));
                            distributorResponsePayload.setUpdateddate(DateUtil.convertDateToStringDate(distributersDelete.getUpdateddate()));
                            Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(distributersDelete.getDistrictId());
                            if(distributersDelete.getDistrictId()!=null && distributersDelete.getDistrictId()!=0) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                                distributorResponsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                                distributorResponsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                            }
                            if(getAllDistributor.getPageIndex() == 0) {
                                distributorResponsePayload.setSerialNo(index+1);
                                index++;
                            }else {
                                distributorResponsePayload.setSerialNo((getAllDistributor.getPageSize()*getAllDistributor.getPageIndex())+(index+1));
                                index++;
                            }
                            responsePayloadList.add(distributorResponsePayload);
                        }
                        distributorResponsePage1.setDistributorResponsePayloads(responsePayloadList);
                        distributorResponsePage1.setPageIndex(page.getNumber());
                        distributorResponsePage1.setPageSize(page.getSize());
                        distributorResponsePage1.setTotalElement(page.getTotalElements());
                        distributorResponsePage1.setTotalPages(page.getTotalPages());
                        distributorResponsePage1.setIsFirstPage(page.isFirst());
                        distributorResponsePage1.setIsLastPage(page.isLast());

                        DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        DeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        DeleteMap.put(DataConstant.RESPONSE_BODY, distributorResponsePage1);
                        log.info("Record found! status - {}", distributorResponsePage1);
                    } else {
                        DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        DeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        DeleteMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return DeleteMap;
                    }
                } else {
                    DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    DeleteMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllDistributor);
                    return DeleteMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            DeleteMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return DeleteMap;
        } catch (Exception e) {
            DeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            DeleteMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return DeleteMap;
        }
        return DeleteMap;
    }

}
