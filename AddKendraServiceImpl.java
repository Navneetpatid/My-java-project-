package com.janaushadhi.adminservice.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.janaushadhi.adminservice.entity.AddKendra;
import com.janaushadhi.adminservice.externalservices.KendraService;
import com.janaushadhi.adminservice.repository.AddKendraRepository;
import com.janaushadhi.adminservice.requestpayload.AddKendraRequest;
import com.janaushadhi.adminservice.requestpayload.KendraDeleteBulk;
import com.janaushadhi.adminservice.requestpayload.NearByKendraRequest;
import com.janaushadhi.adminservice.responsepayload.*;
import com.janaushadhi.adminservice.util.CsvReadder;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.DateUtil;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AddKendraServiceImpl {

    @Autowired
    private AddKendraRepository addKendraRepository;


    @Autowired
    private KendraService kendraService;

    public Map<String, Object> addKendra(AddKendraRequest addKendraRequest) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            if (addKendraRequest.getId() != null && addKendraRequest.getId() != 0) {

                Optional<AddKendra> kendraOptional = addKendraRepository.findById(addKendraRequest.getId());
                if (kendraOptional.isPresent()) {
                    AddKendra existingKendra = kendraOptional.get();
                    BeanUtils.copyProperties(addKendraRequest, existingKendra);
                    existingKendra.setUpdatedDate(new Date());
                    AddKendra savedKendra = addKendraRepository.save(existingKendra);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.KENDRA_UPDATED);
                    map.put(DataConstant.RESPONSE_BODY, savedKendra);
                    log.info("kendra updated successfully: {}", DataConstant.KENDRA_UPDATED);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, null);
                    log.info("kendra with id {} not found: {}", addKendraRequest.getId(), DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    return map;
                }

            }
            if (addKendraRequest.getContactPerson()!=null && !addKendraRequest.getContactPerson().trim().isEmpty()
                    && addKendraRequest.getKendraAddress()!=null && !addKendraRequest.getKendraAddress().trim().isEmpty()
                    && addKendraRequest.getStoreCode()!=null && !addKendraRequest.getStoreCode().trim().isEmpty()
                    && addKendraRequest.getStateId()!=null && addKendraRequest.getStateId()!=0 
                    && addKendraRequest.getDistrictId()!=null  && addKendraRequest.getDistrictId()!=0
                    && addKendraRequest.getPinCode()!=null  && addKendraRequest.getPinCode()!=0 ) {
                AddKendra kendra = new AddKendra();
                Optional<AddKendra> kendraOptional = addKendraRepository.findByStoreCode(addKendraRequest.getStoreCode());
                if(kendraOptional.isPresent()){
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.CODE_ALREADY_EXIST);
                    map.put(DataConstant.RESPONSE_BODY, null);
                    log.info( "kendra storecode already exist",DataConstant.CODE_ALREADY_EXIST);
                    return map;
                }
                kendra.setStoreCode(addKendraRequest.getStoreCode());
                kendra.setContactPerson(addKendraRequest.getContactPerson());
                kendra.setDistrictId(addKendraRequest.getDistrictId());
                kendra.setKendraAddress(addKendraRequest.getKendraAddress());
                kendra.setStateId(addKendraRequest.getStateId());
                kendra.setPinCode(addKendraRequest.getPinCode());
                kendra.setCreatedDate(new Date());
                kendra.setStatus(DataConstant.ONE);
                kendra.setLatitude(addKendraRequest.getLatitude());
                kendra.setLongitude(addKendraRequest.getLatitude());
                AddKendra save = addKendraRepository.save(kendra);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.KENDRA_ADDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.KENDRA_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                map.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory",DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return map;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());;
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.SERVER_MESSAGE, e.getMessage());
            log.error("server error ");

        }
        return map;
    }


    public Map<String, Object> kendraStatusUpdate(Long id, short status) {
        Map<String, Object> map = new HashMap<>();
        Optional<AddKendra> addKendra = addKendraRepository.findById(id);
        if (addKendra.isPresent()) {
            log.info("  distributor Record found! status - {}",addKendra );
            AddKendra kendra = addKendra.get();
            BeanUtils.copyProperties(addKendra, kendra);
            kendra.setStatus(status);

            AddKendra kendraUpdate = addKendraRepository.save(kendra);
            map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            map.put(DataConstant.MESSAGE, DataConstant.KENDRA_STATUS_UPDATE);
            map.put(DataConstant.RESPONSE_BODY, kendraUpdate);
            log.info("kendra status update- {}", kendraUpdate);
            return map;
        } else {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
            map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
            map.put(DataConstant.RESPONSE_BODY, null);
            log.error("kendra id not present ", id);
            return map;
        }
    }


    public Map<String, Object> getByKendraId(Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<AddKendra> kendra = addKendraRepository.findById(id);
            if (!kendra.isEmpty()) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.KENDRA_FOUNDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY,kendra );
                log.info(DataConstant.KENDRA_FOUNDED_SUCCESSFULLY);
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.RESPONSE_BODY, null);
                map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                log.info(DataConstant.RECORD_NOT_FOUND_MESSAGE);

                return map;
            }
        } catch (Exception e) {
            log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            return map;
        }
    }







    public Map<Object, Object> uploadCsvFileToDataBase(MultipartFile docfile) throws IOException {
        Map<Object, Object> map = new HashMap<>();
        if(docfile==null) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
            map.put(DataConstant.RESPONSE_BODY, null);
            map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
            return map;
        }
        List<AddKendra> kendraList = CsvReadder.readCsvKendra(docfile.getInputStream());
        List<AddKendra> kendraListNew =new ArrayList
                <AddKendra>();
        try {
            if (!kendraList.isEmpty()) {

                List<AddKendraResponse> addKendraResponseList = new ArrayList<>();
                for (AddKendra kendra : kendraList) {
                    AddKendra addKendra = new AddKendra();
                    addKendra.setId(kendra.getId());
                    addKendra.setKendraAddress(kendra.getKendraAddress());
                    addKendra.setContactPerson(kendra.getContactPerson());
                    addKendra.setStoreCode(kendra.getStoreCode());
                    addKendra.setPinCode(kendra.getPinCode());
                    addKendra.setDistrictId(kendra.getDistrictId());
//                    addKendra.setUpdatedDate(kendra.getUpdateddate());
//                    addKendra.setCreatedDate(kendra.getCreatedDate());
                    addKendra.setStateId(kendra.getStateId());
                    addKendra.setStatus(DataConstant.ONE);
                    addKendra.setLatitude(kendra.getLatitude());
                    addKendra.setLongitude(kendra.getLongitude());
                    kendraListNew.add(addKendra);
                   // AddKendra saved = addKendraRepository.save(addKendra);
                    AddKendraResponse responsePayload = new AddKendraResponse();
                    BeanUtils.copyProperties(addKendra,responsePayload);
                    addKendraResponseList.add(responsePayload);
                }
                addKendraRepository.saveAll(kendraListNew);
                map.put(DataConstant.RESPONSE_BODY, addKendraResponseList);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE,DataConstant.DATA_STORE_SUCCESSFULLY );
                log.info(DataConstant.DATA_STORE_SUCCESSFULLY);
            } else {
                map.put(DataConstant.RESPONSE_BODY, null);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                map.put(DataConstant.MESSAGE,DataConstant.FILE_NOT_FOUND );
                log.info(DataConstant.FILE_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Exception occurs while uploading data{}", e.getMessage());
            map.put(DataConstant.RESPONSE_BODY, null);
            map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            map.put(DataConstant.MESSAGE, "Csv file content improper data, Please enter proper image url and corresponding data !!");
        }
        return map;
    }



    public Map<String, Object> getAllKendra(GetAllKendra getAllKendra) {
        Map<String, Object> map = new HashMap<>();
        try {

            if(getAllKendra.getPageIndex() ==null &&getAllKendra. getPageSize() ==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<AddKendraResponse> addKendraResponseList = new ArrayList<>();
            AddKendraResponsePage addKendraResponsePage = new AddKendraResponsePage();
            List<AddKendra> kendras = new ArrayList<>();
            Pageable pageable = null;
            Page<AddKendra> page = null;

            if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0) {
                if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0 && getAllKendra.getSearchText() == null || getAllKendra.getSearchText().trim().isEmpty() && getAllKendra.getColumnName() == null || getAllKendra.getColumnName().trim().isEmpty() && getAllKendra.getOrderBy() == null || getAllKendra.getOrderBy().trim().isEmpty()) {
                    kendras = addKendraRepository.findAllWhereStatusNotTwo();
                } else if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0 && getAllKendra.getSearchText() != null && getAllKendra.getColumnName() == null || getAllKendra.getColumnName().trim().isEmpty() && getAllKendra.getOrderBy() == null || getAllKendra.getOrderBy().trim().isEmpty()) {
                    kendras = addKendraRepository.findAllBySearchText(getAllKendra.getSearchText());
                } else if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0 && getAllKendra.getSearchText() == null || getAllKendra.getSearchText().trim().isEmpty() && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.ASC)) {
                    kendras = addKendraRepository.searchAndOrderByASC(getAllKendra.getColumnName());
                } else if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0 && getAllKendra.getSearchText() == null || getAllKendra.getSearchText().trim().isEmpty() && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.DESC)) {
                    kendras = addKendraRepository.searchAndOrderByDESC(getAllKendra.getColumnName());
                } else if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0 && getAllKendra.getSearchText() != null && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.ASC)) {
                    kendras = addKendraRepository.findASC(getAllKendra.getSearchText(), getAllKendra.getColumnName());
                } else if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0 && getAllKendra.getSearchText() != null && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.DESC)) {
                    kendras = addKendraRepository.findDESC(getAllKendra.getSearchText(), getAllKendra.getColumnName());
                }
                if (kendras.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", kendras);
                    for (AddKendra detail : kendras) {
                        AddKendraResponse addKendraResponse = new AddKendraResponse();
                        BeanUtils.copyProperties(detail, addKendraResponse);
                        Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(detail.getDistrictId());
                        if(detail.getDistrictId()!=null && detail.getDistrictId()!=0) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get(DataConstant.RESPONSE_BODY), DistrictOfIndiaResponse.class);
                            addKendraResponse.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                            addKendraResponse.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                        }


                        addKendraResponseList.add(addKendraResponse);
                    }
                    addKendraResponsePage.setAddKendraResponseList(addKendraResponseList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                    log.info("Record found! status - {}", addKendraResponsePage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            } else if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != null) {
                if (getAllKendra.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllKendra.getPageIndex(), getAllKendra.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != 0 && getAllKendra.getSearchText() == null || getAllKendra.getSearchText().trim().isEmpty() && getAllKendra.getColumnName() == null || getAllKendra.getColumnName().trim().isEmpty() && getAllKendra.getOrderBy() == null || getAllKendra.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = addKendraRepository.findAllWhereStatusNotTwo(pageable);

                    } else if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != null && getAllKendra.getSearchText() != null && getAllKendra.getColumnName() == null || getAllKendra.getColumnName().trim().isEmpty() && getAllKendra.getOrderBy() == null || getAllKendra.getOrderBy().trim().isEmpty()) {
                        page = addKendraRepository.findAllByUserName(getAllKendra.getSearchText(), pageable);
                    } else if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != null && getAllKendra.getSearchText() == null || getAllKendra.getSearchText().trim().isEmpty() && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.ASC)) {
                        page = addKendraRepository.searchAndOrderByASC(getAllKendra.getColumnName(), pageable);
                    } else if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != null && getAllKendra.getSearchText() == null || getAllKendra.getSearchText().trim().isEmpty() && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.DESC)) {
                        page = addKendraRepository.searchAndOrderByDESC(getAllKendra.getColumnName(), pageable);
                    } else if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != null && getAllKendra.getSearchText() != null && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.ASC)) {
                        page = addKendraRepository.findASC(getAllKendra.getSearchText(), getAllKendra.getColumnName(), pageable);
                    } else if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != null && getAllKendra.getSearchText() != null && getAllKendra.getColumnName() != null && getAllKendra.getOrderBy().equals(DataConstant.DESC)) {
                        page = addKendraRepository.findDESC(getAllKendra.getSearchText(), getAllKendra.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", kendras);
                        kendras = page.getContent();
                        int index=0;
                        for (AddKendra contactDetail : kendras) {
                            AddKendraResponse addKendraResponse = new AddKendraResponse();
                            BeanUtils.copyProperties(contactDetail, addKendraResponse);

                            Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(contactDetail.getDistrictId());
                            if(contactDetail.getDistrictId()!=null && contactDetail.getDistrictId()!=0) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get(DataConstant.RESPONSE_BODY), DistrictOfIndiaResponse.class);
                                addKendraResponse.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                                addKendraResponse.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                            }
                            //for frontEnd team pagination
                            if(getAllKendra.getPageIndex() == 0) {
                            	addKendraResponse.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		addKendraResponse.setSerialNo((getAllKendra.getPageSize()*getAllKendra.getPageIndex())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
                            addKendraResponseList.add(addKendraResponse);
                        }
                        addKendraResponsePage.setAddKendraResponseList(addKendraResponseList);
                        addKendraResponsePage.setPageIndex(page.getNumber());
                        addKendraResponsePage.setPageSize(page.getSize());
                        addKendraResponsePage.setTotalElement(page.getTotalElements());
                        addKendraResponsePage.setTotalPages(page.getTotalPages());
                        addKendraResponsePage.setIsFirstPage(page.isFirst());
                        addKendraResponsePage.setIsLastPage(page.isLast());

                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                        log.info("Record found! status - {}", addKendraResponsePage);
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return map;
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllKendra);
                    return map;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return map;
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return map;
        }
        return map;
    }



    public Map<String,Object> getAllKendraByStateDistrict(GetKendra getKendra) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getKendra.getPageIndex()==null && getKendra.getPageSize()==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<AddKendraResponse> addKendraResponseList = new ArrayList<>();
            AddKendraResponsePage addKendraResponsePage = new AddKendraResponsePage();
            List<AddKendra> kendraList = new ArrayList<>();
            //List<AddKendra>pharmacistCount = addKendraRepository.findAll();
            Pageable pageable = null;
            Page<AddKendra> page = null;
            Sort sort = Sort.by(Sort.Direction.ASC, "id");


            if (getKendra.getPageIndex() == 0 && getKendra.getPageSize() == 0) {
                if (getKendra.getPageIndex() == 0 && getKendra.getPageSize() == 0 &&  (getKendra.getStateId()== 0 && getKendra.getDistrictId() == 0 && getKendra.getPinCode()==0)) {
                    kendraList = addKendraRepository.findAll(sort);
                }else if (getKendra.getPageIndex() == 0 && getKendra.getPageSize() == 0 && getKendra.getStateId() != null && getKendra.getStateId() != 0&& getKendra.getDistrictId()!=null && getKendra.getDistrictId() != 0 && getKendra.getPinCode()!=null && getKendra.getPinCode() != 0) {
                    kendraList = addKendraRepository.findAllByStateIdAndDistrictIdAndPinCode(getKendra.getStateId(),getKendra.getDistrictId(),getKendra.getPinCode(),sort);
                }
                else if (getKendra.getPageIndex() == 0 && getKendra.getPageSize() == 0 && getKendra.getStateId() != null && getKendra.getStateId() != 0 && getKendra.getDistrictId()!=null && getKendra.getDistrictId()!=0) {
                    kendraList = addKendraRepository.findAllByStateIdAndDistrictId(getKendra.getStateId(),getKendra.getDistrictId(),sort);
                }
                else if (getKendra.getPageIndex() == 0 && getKendra.getPageSize() == 0 && getKendra.getStateId() != null && getKendra.getStateId()!=0 && getKendra.getPinCode()!=null && getKendra.getPinCode()!=0) {
                    kendraList = addKendraRepository.findAllByStateIdAndPinCode(getKendra.getStateId(),getKendra.getPinCode(),sort);
                }
                else if (getKendra.getPageIndex() == 0 && getKendra.getPageSize() == 0 && getKendra.getStateId() != null && getKendra.getStateId()!=0) {
                    kendraList = addKendraRepository.findAllByStateId(getKendra.getStateId(),sort);
                }else if (getKendra.getPageIndex() == 0 && getKendra.getPageSize() == 0 && getKendra.getPinCode() != null && getKendra.getPinCode()!=0) {
                    kendraList = addKendraRepository.findAllByPinCode(getKendra.getPinCode(),sort);
                }


                if (kendraList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", kendraList);
                    for (AddKendra pharmacist : kendraList) {
                        AddKendraResponse addKendraResponse = new AddKendraResponse();
                        BeanUtils.copyProperties(pharmacist, addKendraResponse);
                        Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(pharmacist.getDistrictId());
                        if(pharmacist.getDistrictId()!=null && pharmacist.getDistrictId()!=0) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get(DataConstant.RESPONSE_BODY), DistrictOfIndiaResponse.class);
                            addKendraResponse.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                            addKendraResponse.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                        }
                        addKendraResponse.setCreatedDate(DateUtil.convertDateToStringDate(pharmacist.getCreatedDate()));
                        addKendraResponse.setUpdatedDate(DateUtil.convertDateToStringDate(pharmacist.getUpdatedDate()));
                        addKendraResponseList.add(addKendraResponse);
                    }
                    addKendraResponsePage.setAddKendraResponseList(addKendraResponseList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                    log.info("Record found! status - {}", addKendraResponsePage);
                    return map;
                } else {
                 //   pharmacistResponsePage.setTotalPharmacistCount(pharmacistCount.stream().count());
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            }
            else if (getKendra.getPageIndex() != null && getKendra.getPageSize() != null) {
                if (getKendra.getPageSize() >= 1) {
                    pageable = PageRequest.of(getKendra.getPageIndex(), getKendra.getPageSize(),sort);
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getKendra.getPageIndex() != null && getKendra.getPageSize() != 0 &&  (getKendra.getDistrictId() == 0 &&getKendra.getStateId()==0 && getKendra.getPinCode()==0)) {
                        page = addKendraRepository.findAll(pageable);
                    }else if (getKendra.getPageIndex() != null && getKendra.getPageSize() > 0 && getKendra.getStateId() != null && getKendra.getStateId() != 0&& getKendra.getDistrictId()!=null && getKendra.getDistrictId() != 0 && getKendra.getPinCode()!=null && getKendra.getPinCode() != 0) {
                        page = addKendraRepository.findAllByStateIdAndDistrictIdAndPinCode(getKendra.getStateId(),getKendra.getDistrictId(),getKendra.getPinCode(),pageable);
                    }
                    else if (getKendra.getPageIndex() != null && getKendra.getPageSize() > 0 && getKendra.getStateId() != null &&getKendra.getStateId()!=0&& getKendra.getDistrictId() != null &&getKendra.getDistrictId()!=0) {
                        page = addKendraRepository.findAllByStateIdAndDistrictId(getKendra.getStateId(), getKendra.getDistrictId(),pageable);
                    }
                    else if (getKendra.getPageIndex() != null && getKendra.getPageSize() > 0 && getKendra.getStateId() != null && getKendra.getStateId()!=0 && getKendra.getPinCode()!=null && getKendra.getPinCode()!=0) {
                        page = addKendraRepository.findAllByStateIdAndPinCode(getKendra.getStateId(),getKendra.getPinCode(),pageable);
                    }
                    else if (getKendra.getPageIndex() != null && getKendra.getPageSize() > 0 && getKendra.getStateId() != null &&getKendra.getStateId()!=0) {
                        page = addKendraRepository.findAllByStateId(getKendra.getStateId(),pageable);
                    }
                    else if (getKendra.getPageIndex() != null && getKendra.getPageSize() > 0 && getKendra.getPinCode() != null &&getKendra.getPinCode()!=0) {
                        page = addKendraRepository.findAllByPinCode(getKendra.getPinCode(),pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", kendraList);
                        kendraList = page.getContent();
                        int index=0;
                        for (AddKendra pharmacist : kendraList) {
                            AddKendraResponse adminResponsePayload = new AddKendraResponse();
                            BeanUtils.copyProperties(pharmacist, adminResponsePayload);

                            Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(pharmacist.getDistrictId());
                            if(pharmacist.getDistrictId()!=null && pharmacist.getDistrictId()!=0) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get(DataConstant.RESPONSE_BODY), DistrictOfIndiaResponse.class);
                                adminResponsePayload.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                                adminResponsePayload.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                            }
                            adminResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(pharmacist.getCreatedDate()));
                            //adminResponsePayload.setUpdatedDate(DateUtil.convertDateToStringDate(pharmacist.getUpdatedDate()));
                            //for frontEnd team pagination
                            if(getKendra.getPageIndex() == 0) {
                            	adminResponsePayload.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		adminResponsePayload.setSerialNo((getKendra.getPageSize()*getKendra.getPageIndex())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
                            addKendraResponseList.add(adminResponsePayload);
                        }
                        addKendraResponsePage.setAddKendraResponseList(addKendraResponseList);
                        addKendraResponsePage.setPageIndex(page.getNumber());
                        addKendraResponsePage.setPageSize(page.getSize());
                        addKendraResponsePage.setTotalElement(page.getTotalElements());
                        addKendraResponsePage.setTotalPages(page.getTotalPages());
                        addKendraResponsePage.setIsFirstPage(page.isFirst());
                        addKendraResponsePage.setIsLastPage(page.isLast());
                      //  pharmacistResponsePage.setAddKendraResponseList(pharmacistCount.stream().count());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                        log.info("Record found! status - {}", addKendraResponsePage);
                    } else {
                       // pharmacistResponsePage.setTotalPharmacistCount(pharmacistCount.stream().count());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getKendra);
                    return map;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return map;
    }

    public Map<Object, Object> deleteKendraBulk(KendraDeleteBulk kendraIds) {
        Map<Object, Object> map = new HashMap<>();
        List<AddKendra> kendraList = null;
        List<AddKendra> list = new ArrayList<AddKendra>();
        try {
            if(kendraIds.getKendraIds().isEmpty()) {
                map.put(DataConstant.OBJECT_RESPONSE, null);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PLEASE_ADD_ID_TO_DELETE);
                log.info(DataConstant.PLEASE_ADD_ID_TO_DELETE);
                return map;
            }
            kendraList = this.addKendraRepository.findAllById(kendraIds.getKendraIds());
            if(kendraList!=null && !kendraList.isEmpty()) {
                for(AddKendra kendra:kendraList) {
                    kendra.setStatus((short)2);
                    list.add(kendra);
                }
                list=this.addKendraRepository.saveAll(list);
                map.put(DataConstant.OBJECT_RESPONSE, list);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.KENDRA_DELETE_SUCCESSFULLY);
                log.info(DataConstant.KENDRA_DELETE_SUCCESSFULLY);
            }
            else{
                map.put(DataConstant.OBJECT_RESPONSE, null);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                map.put(DataConstant.MESSAGE, DataConstant.KENDRA_NOT_FOUND);
                log.info("No kendra found.");
            }
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching kendra: " + e.getMessage());
        }
        return map;

    }


    public Map<String, Object> getAllDeleteKendra(GetAllDeleteKendra getAllKendra) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllKendra.getPageIndex() ==null &&getAllKendra. getPageSize() ==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<AddKendraResponse> addKendraResponseList = new ArrayList<>();
            AddKendraResponsePage addKendraResponsePage = new AddKendraResponsePage();
            List<AddKendra> kendras = new ArrayList<>();
            Pageable pageable = null;
            Page<AddKendra> page = null;
            Short status=2;
            if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0) {
                if (getAllKendra.getPageIndex() == 0 && getAllKendra.getPageSize() == 0  ) {
                    kendras = addKendraRepository.findAllByStatus(status);
                }
                if (kendras.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", kendras);
                    for (AddKendra detail : kendras) {
                        AddKendraResponse addKendraResponse = new AddKendraResponse();
                        BeanUtils.copyProperties(detail, addKendraResponse);
                        Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(detail.getDistrictId());
                        if(detail.getDistrictId()!=null && detail.getDistrictId()!=0) {
                            ObjectMapper objectMapper = new ObjectMapper();
                            DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get(DataConstant.RESPONSE_BODY), DistrictOfIndiaResponse.class);
                            addKendraResponse.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                            addKendraResponse.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                        }
                        addKendraResponseList.add(addKendraResponse);
                    }
                    addKendraResponsePage.setAddKendraResponseList(addKendraResponseList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                    log.info("Record found! status - {}", addKendraResponsePage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            } else if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != null) {
                if (getAllKendra.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllKendra.getPageIndex(), getAllKendra.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllKendra.getPageIndex() != null && getAllKendra.getPageSize() != 0) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = addKendraRepository.findAllByStatus( status,pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", kendras);
                        kendras = page.getContent();
                        int index=0;
                        for (AddKendra contactDetail : kendras) {
                            AddKendraResponse addKendraResponse = new AddKendraResponse();
                            BeanUtils.copyProperties(contactDetail, addKendraResponse);

                            Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(contactDetail.getDistrictId());
                            if(contactDetail.getDistrictId()!=null && contactDetail.getDistrictId()!=0) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get(DataConstant.RESPONSE_BODY), DistrictOfIndiaResponse.class);
                                addKendraResponse.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                                addKendraResponse.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                            }
                            //for frontEnd team pagination
                            if(getAllKendra.getPageIndex() == 0) {
                                addKendraResponse.setSerialNo(index+1);
                                index++;
                                //	System.out.println("index==="+index);
                            }else {
                                addKendraResponse.setSerialNo((getAllKendra.getPageSize()*getAllKendra.getPageIndex())+(index+1));
                                index++;
                                //	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                            }
                            addKendraResponseList.add(addKendraResponse);
                        }
                        addKendraResponsePage.setAddKendraResponseList(addKendraResponseList);
                        addKendraResponsePage.setPageIndex(page.getNumber());
                        addKendraResponsePage.setPageSize(page.getSize());
                        addKendraResponsePage.setTotalElement(page.getTotalElements());
                        addKendraResponsePage.setTotalPages(page.getTotalPages());
                        addKendraResponsePage.setIsFirstPage(page.isFirst());
                        addKendraResponsePage.setIsLastPage(page.isLast());

                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, addKendraResponsePage);
                        log.info("Record found! status - {}", addKendraResponsePage);
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return map;
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllKendra);
                    return map;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return map;
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return map;
        }
        return map;
    }

    public Map<String, Object> nearByKendra(NearByKendraRequest nearByKendraRequest) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(nearByKendraRequest.getStateId()!=null && nearByKendraRequest.getStateId()!=0 && nearByKendraRequest.getDistrictId()!=null &&  nearByKendraRequest.getDistrictId()!=0
                    && nearByKendraRequest.getLatitude()!=null&&! nearByKendraRequest.getLatitude().isEmpty()&& nearByKendraRequest.getLongitude()!=null && !nearByKendraRequest.getLongitude().isEmpty()){

            GetKendra getKendraPayload = new GetKendra(0, 0, nearByKendraRequest.getStateId(), nearByKendraRequest.getDistrictId(), 0L);
            Map<String, Object> allKendraByStateDistrict = getAllKendraByStateDistrict(getKendraPayload);
            ObjectMapper objectMapper = new ObjectMapper();
            AddKendraResponsePage kendraResponseList = objectMapper.convertValue(allKendraByStateDistrict.get(DataConstant.RESPONSE_BODY), AddKendraResponsePage.class);
            List<AddKendraResponse> addKendraResponseList = new ArrayList<>();
            if(kendraResponseList!=null) {
                for (AddKendraResponse kendra : kendraResponseList.getAddKendraResponseList()) {
                    if(kendra.getLatitude()!=null &&! kendra.getLatitude().isEmpty() && kendra.getLongitude()!=null && !kendra.getLongitude().isEmpty()) {
                        double kendraLatitude = Math.toRadians(Double.parseDouble(kendra.getLatitude()));
                        double kendraLongitude = Math.toRadians(Double.parseDouble(kendra.getLongitude()));
                        double currentLatitude = Math.toRadians(Double.parseDouble((nearByKendraRequest.getLatitude())));
                        double currentLongitude = Math.toRadians(Double.parseDouble((nearByKendraRequest.getLongitude())));
                        double dLat = currentLatitude - kendraLatitude;
                        double dLon = currentLongitude - kendraLongitude;
                        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(kendraLatitude) * Math.cos(currentLatitude) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        final double EARTH_RADIUS_KM = 6371.0;
                        double distanceInKm = EARTH_RADIUS_KM * c;
// double distanceInKm =0.2;
                        double fixedDistance = 3;
                        if (distanceInKm <= fixedDistance) {
                            addKendraResponseList.add(kendra);

                            // return map;
                        }
                    }
                }
                if(addKendraResponseList!=null) {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.KENDRA_FOUNDED_SUCCESSFULLY);
                    map.put(DataConstant.RESPONSE_BODY, addKendraResponseList);
                    return map;
                }else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.KENDRA_NOT_FOUND);
                    map.put(DataConstant.RESPONSE_BODY, null);
                    return map;
                }
            }else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.KENDRA_NOT_FOUND_IN_REDIUS);
                map.put(DataConstant.RESPONSE_BODY, null);
                return map;
            }

        }
            else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.ALL_FEILDS_MANDATORY);
                map.put(DataConstant.RESPONSE_BODY, null);
                return map;
            }
        }
            catch (DataAccessResourceFailureException e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return map;
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return map;
        }

    }



}
