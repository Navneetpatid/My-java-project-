package com.janaushadhi.adminservice.serviceimpl;


import com.janaushadhi.adminservice.entity.*;
import com.janaushadhi.adminservice.repository.ExecutiveCouncilRepository;
import com.janaushadhi.adminservice.repository.GovernanceCounsilRepository;
import com.janaushadhi.adminservice.repository.ManagementRepository;
import com.janaushadhi.adminservice.requestpayload.ExecutiveCouncilRequest;
import com.janaushadhi.adminservice.requestpayload.GoverningCouncilRequest;
import com.janaushadhi.adminservice.requestpayload.ManagementRequest;
import com.janaushadhi.adminservice.responsepayload.*;
import com.janaushadhi.adminservice.util.DataConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class GovernanceServiceImpl {

    @Autowired
    private GovernanceCounsilRepository governanceCounsilRepository;

    @Autowired
    private ManagementRepository managementRepository;

    @Autowired
    private ExecutiveCouncilRepository councilRepository;

public Map<String,Object> addGoverningCouncil(GoverningCouncilRequest governingCouncilRequest){
    Map<String, Object> map = new HashMap<>();
    try {
        if(governingCouncilRequest.getDesignation()!=null && !governingCouncilRequest.getDesignation().trim().equals("")
        && governingCouncilRequest.getGcMember()!=null && !governingCouncilRequest.getGcMember().trim().equals("")){

            GoverningCouncil governingCouncil = new GoverningCouncil();
            governingCouncil.setDesignation(governingCouncilRequest.getDesignation());
            governingCouncil.setGcMember(governingCouncilRequest.getGcMember());
            GoverningCouncil save = governanceCounsilRepository.save(governingCouncil);
            map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            map.put(DataConstant.MESSAGE, DataConstant.GOVERNING_ADDED_SUCCESSFULLY);
            map.put(DataConstant.RESPONSE_BODY, save);
            log.info(DataConstant.GOVERNING_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
            return map;
        } else {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
            map.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
            map.put(DataConstant.RESPONSE_BODY, null);
            log.info("All feilds are mandatory",DataConstant.ALL_FIELDS_ARE_MANDATORY);
            return map;
        }


    } catch (Exception e){
    	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
	}
    return map;
}



    public Map<String,Object> getAllCouncil(GetAllGoverning getAllGoverning) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllGoverning.getPageIndex() ==null && getAllGoverning.getPageSize() ==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<GovernanceConcilResponse> governanceConcilResponselist = new ArrayList<>();
            GovernanceResponsepage governanceResponsepage = new GovernanceResponsepage();
            List<GoverningCouncil> governingCouncilList = new ArrayList<>();
            Pageable pageable = null;
            Page<GoverningCouncil> page = null;
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            if (getAllGoverning.getPageIndex()== 0 && getAllGoverning.getPageSize() == 0) {
                    governingCouncilList = governanceCounsilRepository.findAll(sort);

                if (governingCouncilList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", governingCouncilList);
                    for (GoverningCouncil council : governingCouncilList) {
                        GovernanceConcilResponse governanceConcilResponse = new GovernanceConcilResponse();
                        BeanUtils.copyProperties(council, governanceConcilResponse);
                        governanceConcilResponselist.add(governanceConcilResponse);
                    }
                    governanceResponsepage.setGovernanceConcilResponseList(governanceConcilResponselist);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, governanceResponsepage);
                    log.info("Record found! status - {}", governanceResponsepage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, governanceResponsepage);
                    log.info("Record not found! status - {}");
                    return map;
                }
                }
            else {
                if (getAllGoverning.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllGoverning.getPageIndex(), getAllGoverning.getPageSize(), sort);
                    page = governanceCounsilRepository.findAll(pageable);

                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", governingCouncilList);
                        governingCouncilList = page.getContent();
                        int index=0;
                        for (GoverningCouncil council : governingCouncilList) {
                            GovernanceConcilResponse governanceConcilResponse= new GovernanceConcilResponse();
                            BeanUtils.copyProperties(council, governanceConcilResponse);
                            
							// for frontEnd team pagination
							if (getAllGoverning.getPageIndex() == 0) {
								governanceConcilResponse.setSerialNo(index + 1);
								index++;
							} else {
								governanceConcilResponse.setSerialNo((getAllGoverning.getPageSize() * getAllGoverning.getPageIndex()) + (index + 1));
								index++;
							}
                            governanceConcilResponselist.add(governanceConcilResponse);
                        }
                        governanceResponsepage.setGovernanceConcilResponseList(governanceConcilResponselist);
                        governanceResponsepage.setPageIndex(page.getNumber());
                        governanceResponsepage.setPageSize(page.getSize());
                        governanceResponsepage.setTotalElement(page.getTotalElements());
                        governanceResponsepage.setTotalPages(page.getTotalPages());
                        governanceResponsepage.setIsFirstPage(page.isFirst());
                        governanceResponsepage.setIsLastPage(page.isLast());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, governanceResponsepage);
                        log.info("Record found! status - {}", governanceResponsepage);
                        return map;
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, governanceResponsepage);
                        log.info("Record not found! status - {}");
                        return map;
                    }
                }  else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}");
                    return map;
                }
            }
        }catch (Exception e){
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }
        return map;
    }




    public Map<String,Object> addExecutiveCouncil(ExecutiveCouncilRequest executiveCouncilRequest){
        Map<String, Object> map = new HashMap<>();
        try {
            if(executiveCouncilRequest.getDesignation()!=null && !executiveCouncilRequest.getDesignation().trim().isEmpty()
                    && executiveCouncilRequest.getEcMember()!=null && !executiveCouncilRequest.getEcMember().trim().isEmpty()){

                ExecutiveCouncil executiveCouncil = new ExecutiveCouncil();
                executiveCouncil.setDesignation(executiveCouncilRequest.getDesignation());
                executiveCouncil.setEcMember(executiveCouncilRequest.getEcMember());
                //  GoverningCouncil save=  governanceCounsilRepository.save(executiveCouncil);

                ExecutiveCouncil save = councilRepository.save(executiveCouncil);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.EXECUTIVE_ADDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.GOVERNING_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                map.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory",DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return map;
            }


        } catch (Exception e){
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
			
        }
        return map;
    }



    public Map<String,Object> getAllExecutiveCouncil(GetAllExcecutive getAllExcecutive) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllExcecutive.getPageIndex() ==null &&getAllExcecutive.getPageSize() ==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<ExecutiveResponse> executiveResponseList = new ArrayList<>();
            ExecutiveResponsePage executiveResponsePage = new ExecutiveResponsePage();
            List<ExecutiveCouncil> executiveCouncilList = new ArrayList<>();
            Pageable pageable = null;
            Page<ExecutiveCouncil> page = null;


            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            if (getAllExcecutive.getPageIndex()== 0 && getAllExcecutive.getPageSize() == 0) {
                executiveCouncilList = councilRepository.findAll(sort);

                if (executiveCouncilList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", executiveCouncilList);
                    for (ExecutiveCouncil council : executiveCouncilList) {
                        ExecutiveResponse executiveResponse = new ExecutiveResponse();
                        BeanUtils.copyProperties(council, executiveResponse);
                        executiveResponseList.add(executiveResponse);
                    }
                    executiveResponsePage.setExecutiveResponseList(executiveResponseList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, executiveResponsePage);
                    log.info("Record found! status - {}", executiveResponsePage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, executiveResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            }
            else {
                if (getAllExcecutive.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllExcecutive.getPageIndex(), getAllExcecutive.getPageSize(), sort);
                    page = councilRepository.findAll(pageable);

                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", executiveCouncilList);
                        executiveCouncilList = page.getContent();
                        int index =0;
                        for (ExecutiveCouncil council : executiveCouncilList) {
                            ExecutiveResponse executiveResponse= new ExecutiveResponse();
                            BeanUtils.copyProperties(council, executiveResponse);
                           // for frontEnd team pagination
							if (getAllExcecutive.getPageIndex() == 0) {
								executiveResponse.setSerialNo(index + 1);
								index++;
							} else {
								executiveResponse.setSerialNo((getAllExcecutive.getPageSize() * getAllExcecutive.getPageIndex()) + (index + 1));
								index++;
							}

                            executiveResponseList.add(executiveResponse);
                        }
                        executiveResponsePage.setExecutiveResponseList(executiveResponseList);
                        executiveResponsePage.setPageIndex(page.getNumber());
                        executiveResponsePage.setPageSize(page.getSize());
                        executiveResponsePage.setTotalElement(page.getTotalElements());
                        executiveResponsePage.setTotalPages(page.getTotalPages());
                        executiveResponsePage.setIsFirstPage(page.isFirst());
                        executiveResponsePage.setIsLastPage(page.isLast());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, executiveResponsePage);
                        log.info("Record found! status - {}", executiveResponsePage);
                        return map;
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, executiveResponsePage);
                        log.info("Record not found! status - {}");
                        return map;
                    }
                }  else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}");
                    return map;
                }
            }
        }catch (Exception e){
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
			
        }
        return map;
    }








    public Map<String, Object> addManagementTeam(ManagementRequest managementRequest) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {

            if(managementRequest.getId()!=0 && managementRequest.getId()!=null) {
                Optional<ManagementTeam> teamOptional = managementRepository.findById(managementRequest.getId());
                if (teamOptional.isPresent()) {
                    ManagementTeam managementTeam = teamOptional.get();
                    BeanUtils.copyProperties(managementRequest, managementTeam);
                    managementTeam.setUpdatedDate(new Date());
                    managementTeam.setImageIsActive(true);
                    ManagementTeam savedDistributor = managementRepository.save(managementTeam);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.MANAGEMENT_UPDATED);
                    map.put(DataConstant.RESPONSE_BODY, savedDistributor);
                    log.info("management updated successfully: {}", DataConstant.MANAGEMENT_UPDATED);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, null);
                    log.info("management with id {} not found: {}", managementRequest.getId(), DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    return map;
                }
            }
            if (managementRequest.getName()!=null ||! managementRequest.getName().trim().isEmpty()
                    && managementRequest.getMail()!=null||! managementRequest.getMail().trim().isEmpty()
                    &&managementRequest.getDesignation()!=null||!managementRequest.getDesignation().trim().isEmpty()
                    && managementRequest.getFax()!=null&& !managementRequest.getFax().trim().isEmpty() &&managementRequest.getContactNo()!=null  && !managementRequest.getContactNo().trim().isEmpty()
                    && managementRequest.getPhoto()!=null && !managementRequest.getPhoto().trim().isEmpty() &&managementRequest.getEduQualification()!=null && !managementRequest.getEduQualification().trim().isEmpty()) {
                ManagementTeam managementTeam = new ManagementTeam();
                managementTeam.setName(managementRequest.getName());
                managementTeam.setFax(managementRequest.getFax());
                managementTeam.setContactNo(managementRequest.getContactNo());
                managementTeam.setEduQualification(managementRequest.getEduQualification());
                managementTeam.setCreatedDate(new Date());
                managementTeam.setPhoto(managementRequest.getPhoto());
                managementTeam.setMail(managementRequest.getMail());
                managementTeam.setDesignation(managementRequest.getDesignation());
                managementTeam.setCreatedDate(new Date());
                managementTeam.setStatus(DataConstant.ONE);
                managementTeam.setImageIsActive(true);
                ManagementTeam save = managementRepository.save(managementTeam);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.MANAGEMENT_ADDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.MANAGEMENT_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                map.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory",DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return map;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
			
        }
        return map;
    }



    public Map<String, Object> getByManagementId(Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<ManagementTeam> teamOptional = managementRepository.findById(id);
            if (!teamOptional.isEmpty()) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.MANAGEMENT_FOUNDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY,teamOptional );
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.RESPONSE_BODY, null);
                map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                return map;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            return map;
        }
    }


    public Map<String,Object> getAllManagement(GetAllManagement getAllManagement) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllManagement.getPageIndex()==null && getAllManagement.getPageSize()==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<ManagementResponse> responsePayloadList = new ArrayList<>();
            ManagementResponsePage managementResponsePage = new ManagementResponsePage();
            List<ManagementTeam> managementTeamList = new ArrayList<>();
            Pageable pageable = null;
            Page<ManagementTeam> page = null;


            if (getAllManagement.getPageIndex() == 0 && getAllManagement.getPageSize() == 0) {
                if (getAllManagement.getPageIndex() == 0 && getAllManagement.getPageSize() == 0 && getAllManagement.getSearchText() == null || getAllManagement.getSearchText().trim().isEmpty() && getAllManagement.getColumnName() == null || getAllManagement.getColumnName().trim().isEmpty() && getAllManagement.getOrderBy() == null || getAllManagement.getOrderBy().trim().isEmpty()) {
                    managementTeamList = managementRepository.findAllWhereStatusNotTwo();
                } else if (getAllManagement.getPageIndex() == 0 && getAllManagement.getPageSize() == 0 && getAllManagement.getSearchText() != null && getAllManagement.getColumnName() == null || getAllManagement.getColumnName().trim().isEmpty() && getAllManagement.getOrderBy() == null || getAllManagement.getOrderBy().trim().isEmpty()) {
                    managementTeamList = managementRepository.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(getAllManagement.getSearchText());
                } else if (getAllManagement.getPageIndex() == 0 && getAllManagement.getPageSize() == 0 && getAllManagement.getSearchText() == null || getAllManagement.getSearchText().trim().isEmpty() && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.ASC)) {
                    managementTeamList = managementRepository.searchAndOrderByASC(getAllManagement.getColumnName());
                } else if (getAllManagement.getPageIndex() == 0 && getAllManagement.getPageSize() == 0 && getAllManagement.getSearchText() == null || getAllManagement.getSearchText().trim().isEmpty() && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.DESC)) {
                    managementTeamList = managementRepository.searchAndOrderByDESC(getAllManagement.getColumnName());
                } else if (getAllManagement.getPageIndex() == 0 && getAllManagement.getPageSize() == 0 && getAllManagement.getSearchText() != null && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.ASC)) {
                    managementTeamList = managementRepository.findASC(getAllManagement.getSearchText(), getAllManagement.getColumnName());
                } else if (getAllManagement.getPageIndex() == 0 && getAllManagement.getPageSize() == 0 && getAllManagement.getSearchText() != null && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.DESC)) {
                    managementTeamList = managementRepository.findDESC(getAllManagement.getSearchText(), getAllManagement.getColumnName());
                }
                if (managementTeamList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", managementTeamList);
                    for (ManagementTeam managementTeam : managementTeamList) {
                        ManagementResponse managementResponse = new ManagementResponse();
                        BeanUtils.copyProperties(managementTeam, managementResponse);
//                        managementResponse.setCreatedDate(DateUtil.convertDateToStringDate(managementTeam.getCreatedDate()));
//                        managementResponse.setUpdatedDate(DateUtil.convertDateToStringDate(managementTeam.getUpdatedDate()));
                        responsePayloadList.add(managementResponse);
                    }
                    managementResponsePage.setManagementResponseList(responsePayloadList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, managementResponsePage);
                    log.info("Record found! status - {}", managementResponsePage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, managementResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            } else if (getAllManagement.getPageIndex() != null && getAllManagement.getPageSize() != null) {
                if (getAllManagement.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllManagement.getPageIndex(), getAllManagement.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllManagement.getPageIndex() != null && getAllManagement.getPageSize() != 0 && getAllManagement.getSearchText() == null || getAllManagement.getSearchText().trim().isEmpty() && getAllManagement.getColumnName() == null || getAllManagement.getColumnName().trim().isEmpty() && getAllManagement.getOrderBy() == null || getAllManagement.getOrderBy().trim().isEmpty()) {
                        page = managementRepository.findAllWhereStatusNotTwo(pageable);
                    } else if (getAllManagement.getPageIndex() != null && getAllManagement.getPageSize() != null && getAllManagement.getSearchText() != null && getAllManagement.getColumnName() == null || getAllManagement.getColumnName().trim().isEmpty() && getAllManagement.getOrderBy() == null || getAllManagement.getOrderBy().trim().isEmpty()) {
                        page = managementRepository.findAllByUserName(getAllManagement.getSearchText(), pageable);
                    } else if (getAllManagement.getPageIndex() != null && getAllManagement.getPageSize() != null && getAllManagement.getSearchText() == null || getAllManagement.getSearchText().trim().isEmpty() && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.ASC)) {
                        page = managementRepository.searchAndOrderByASC(getAllManagement.getColumnName(), pageable);
                    } else if (getAllManagement.getPageIndex() != null && getAllManagement.getPageSize() != null && getAllManagement.getSearchText() == null || getAllManagement.getSearchText().trim().isEmpty() && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.DESC)) {
                        page = managementRepository.searchAndOrderByDESC(getAllManagement.getColumnName(), pageable);
                    } else if (getAllManagement.getPageIndex() != null && getAllManagement.getPageSize() != null && getAllManagement.getSearchText() != null && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.ASC)) {
                        page = managementRepository.findASC(getAllManagement.getSearchText(), getAllManagement.getColumnName(), pageable);
                    } else if (getAllManagement.getPageIndex() != null && getAllManagement.getPageSize() != null && getAllManagement.getSearchText() != null && getAllManagement.getColumnName() != null && getAllManagement.getOrderBy().equals(DataConstant.DESC)) {
                        page = managementRepository.findDESC(getAllManagement.getSearchText(), getAllManagement.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", managementTeamList);
                        managementTeamList = page.getContent();
                       int index=0;
                        for (ManagementTeam managementTeam : managementTeamList) {
                            ManagementResponse managementResponsePayload = new ManagementResponse();
                            BeanUtils.copyProperties(managementTeam, managementResponsePayload);
//                            distributorResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(managementTeam.getCreatedDate()));
//                            distributorResponsePayload.setUpdateddate(DateUtil.convertDateToStringDate(managementTeam.getUpdateddate()));
                            // for frontEnd team pagination
							if (getAllManagement.getPageIndex() == 0) {
								managementResponsePayload.setSerialNo(index + 1);
								index++;
							} else {
								managementResponsePayload.setSerialNo((getAllManagement.getPageSize() * getAllManagement.getPageIndex()) + (index + 1));
								index++;
							}
                            responsePayloadList.add(managementResponsePayload);
                        }
                        managementResponsePage.setManagementResponseList(responsePayloadList);
                        managementResponsePage.setPageIndex(page.getNumber());
                        managementResponsePage.setPageSize(page.getSize());
                        managementResponsePage.setTotalElement(page.getTotalElements());
                        managementResponsePage.setTotalPages(page.getTotalPages());
                        managementResponsePage.setIsFirstPage(page.isFirst());
                        managementResponsePage.setIsLastPage(page.isLast());

                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, managementResponsePage);
                        log.info("Record found! status - {}", managementResponsePage);
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
                    log.info("Page size can't be less then one! status - {}", getAllManagement);
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



	public Map<String, Object> deleteByManagementId(Long id) {
		Map<String, Object> map = new HashMap<>();
        ManagementTeam team = null;
        try {
        	team   = managementRepository.findById(id).orElse(null);

            if (team != null) {

                managementRepository.delete(team);
                map.put(DataConstant.RESPONSE_BODY, team);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.RECORD_DELETED_SUCCESSFULLY);
            } else {
                map.put(DataConstant.RESPONSE_BODY, null);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                log.info("No products found.");
            }
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching management team: " + e.getMessage());
        }
        return map;

	}



	public Map<String, Object> managementTeamImageActiveDeactiveById(Long id, boolean imageIsActive) {
		Map<String, Object> map = new HashMap<>();
        ManagementTeam team = null;
        try {
        	team   = managementRepository.findById(id).orElse(null);

            if (team != null) {
            	team.setImageIsActive(imageIsActive);
            	team.setUpdatedDate(new Date());
                managementRepository.save(team);
                map.put(DataConstant.RESPONSE_BODY, team);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.UPDATED_SUCCESSFULLY);
            } else {
                map.put(DataConstant.RESPONSE_BODY, null);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                log.info("No products found.");
            }
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching management team: " + e.getMessage());
        }
        return map;
	}

}
