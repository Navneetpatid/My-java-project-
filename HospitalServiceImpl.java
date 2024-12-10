package com.janaushadhi.adminservice.serviceimpl;

import com.janaushadhi.adminservice.entity.Hospital;
import com.janaushadhi.adminservice.repository.HospitalRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl {


    @Autowired
    private HospitalRepository hospitalRepository;

    public Map<String,Object> getAllHospital(GetAllHospital getAllHospital) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllHospital.getPageIndex()==null && getAllHospital.getPageSize()==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<HospitalResponse> hospitalResponseList  = new ArrayList<>();
            HospitalResponsepage mediaResponsePage = new HospitalResponsepage();
            List<Hospital> hospitalList = new ArrayList<>();
            //  List<Media>pharmacistCount = mediaRepository.findAll();
            Pageable pageable = null;
            Page<Hospital> page = null;
            Sort sort = Sort.by(Sort.Direction.ASC, "id");


            if (getAllHospital.getPageIndex() == 0 && getAllHospital.getPageSize() == 0) {
                if (getAllHospital.getPageIndex() == 0 && getAllHospital.getPageSize() == 0
                        && getAllHospital.getDistrict()!=null && getAllHospital.getDistrict().trim().equals("")
                        && getAllHospital.getState()!=null && getAllHospital.getState().trim().equals("")
                        && getAllHospital.getHsplType()!=null && getAllHospital.getHsplType().trim().equals("")) {
                    hospitalList = hospitalRepository.findAll(sort);
                }
                else if (getAllHospital.getPageIndex() == 0 && getAllHospital.getPageSize() == 0 && getAllHospital.getDistrict()!= null  &&! getAllHospital.getDistrict().trim().equals("")  && getAllHospital.getState()!= null  &&! getAllHospital.getState().trim().equals("")
                        && getAllHospital.getHsplType()!= null  &&! getAllHospital.getHsplType().trim().equals("")) {
                    hospitalList = hospitalRepository.findAllByStateAndDistrictAndHsplType(getAllHospital.getState(), getAllHospital.getDistrict(),getAllHospital.getHsplType(),sort);
                }
                else if (getAllHospital.getPageIndex() == 0 && getAllHospital.getPageSize() == 0  && getAllHospital.getDistrict() != null  &&! getAllHospital.getDistrict().trim().equals("")&&  getAllHospital.getState()!=null &&! getAllHospital.getState().trim().equals("")) {
                    hospitalList = hospitalRepository.findAllByStateAndDistrict(getAllHospital.getState(),getAllHospital.getDistrict(),sort);
                }
                else if (getAllHospital.getPageIndex() == 0 && getAllHospital.getPageSize() == 0 &&   getAllHospital.getState()!=null&& !getAllHospital.getState().trim().equals("")&& getAllHospital.getHsplType()!=null  &&  !getAllHospital.getHsplType().trim().equals("") ) {
                    hospitalList = hospitalRepository.findAllByStateAndHsplType( getAllHospital.getState(),getAllHospital.getHsplType(),sort);
                }
                else if (getAllHospital.getPageIndex() == 0 && getAllHospital.getPageSize() == 0   &&  getAllHospital.getState()!=null &&! getAllHospital.getState().trim().equals("")) {
                    hospitalList = hospitalRepository.findAllByState(getAllHospital.getState(),sort);
                }
                else if (getAllHospital.getPageIndex() == 0 && getAllHospital.getPageSize() == 0 &&  getAllHospital.getHsplType()!=null  &&  !getAllHospital.getHsplType().trim().equals("")) {
                    hospitalList = hospitalRepository.findAllByHsplType(getAllHospital.getHsplType(),sort);
                }


                if (hospitalList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", hospitalList);
                    for (Hospital hospital : hospitalList) {
                        HospitalResponse hospitalResponse = new HospitalResponse();
                        BeanUtils.copyProperties(hospital, hospitalResponse);
                        hospitalResponseList.add(hospitalResponse);
                    }
                    mediaResponsePage.setHospitalResponseList(hospitalResponseList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                    log.info("Record found! status - {}", mediaResponsePage);
                    return map;
                } else {
                    //  mediaResponsePage.setTotalPharmacistCount(pharmacistCount.stream().count());
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            }
            else if (getAllHospital.getPageIndex() != null && getAllHospital.getPageSize() != null) {
                if (getAllHospital.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllHospital.getPageIndex(), getAllHospital.getPageSize(),sort);
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllHospital.getPageIndex() != null && getAllHospital.getPageSize() != 0
                            && getAllHospital.getDistrict()!=null && getAllHospital.getDistrict().trim().equals("")
                            && getAllHospital.getState()!=null && getAllHospital.getState().trim().equals("")
                            && getAllHospital.getHsplType()!=null && getAllHospital.getHsplType().trim().equals("")) {
                        page = hospitalRepository.findAll(pageable);
                    }
                    else if ( getAllHospital.getPageSize() !=null && getAllHospital.getDistrict()!= null  &&! getAllHospital.getDistrict().trim().equals("")  && getAllHospital.getState()!= null  &&! getAllHospital.getState().trim().equals("")
                            && getAllHospital.getHsplType()!= null  &&! getAllHospital.getHsplType().trim().equals("")) {
                        page = hospitalRepository.findAllByStateAndDistrictAndHsplType(getAllHospital.getState(), getAllHospital.getDistrict(),getAllHospital.getHsplType(),pageable);
                    }
                    else if (getAllHospital.getPageIndex() !=null && getAllHospital.getPageSize() != 0  && getAllHospital.getDistrict() != null  &&! getAllHospital.getDistrict().trim().equals("")&&  getAllHospital.getState()!=null &&! getAllHospital.getState().trim().equals("")) {
                        page = hospitalRepository.findAllByStateAndDistrict(getAllHospital.getState(),getAllHospital.getDistrict(),pageable);
                    }
                    else if (getAllHospital.getPageIndex() !=null && getAllHospital.getPageSize() != 0 &&   getAllHospital.getState()!=null&& !getAllHospital.getState().trim().equals("")&& getAllHospital.getHsplType()!=null  &&  !getAllHospital.getHsplType().trim().equals("") ) {
                        page = hospitalRepository.findAllByStateAndHsplType( getAllHospital.getState(),getAllHospital.getHsplType(),pageable);
                    }
                    else if (getAllHospital.getPageIndex() !=null && getAllHospital.getPageSize() != 0  &&  getAllHospital.getState()!=null &&! getAllHospital.getState().trim().equals("")) {
                        page = hospitalRepository.findAllByState(getAllHospital.getState(),pageable);
                    }
                    else if (getAllHospital.getPageIndex() !=null && getAllHospital.getPageSize() != 0 &&  getAllHospital.getHsplType()!=null  &&  !getAllHospital.getHsplType().trim().equals("") ) {
                        page = hospitalRepository.findAllByHsplType(getAllHospital.getHsplType(),pageable);
                    }

//
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", hospitalList);
                        hospitalList = page.getContent();
                       int index=0;
                        for (Hospital hospital : hospitalList) {
                            HospitalResponse hospitalResponse = new HospitalResponse();
                            BeanUtils.copyProperties(hospital, hospitalResponse);
                          //for frontEnd team pagination
                            if(getAllHospital.getPageIndex() == 0) {
                            	hospitalResponse.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		hospitalResponse.setSerialNo((getAllHospital.getPageSize()*getAllHospital.getPageIndex())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
                            hospitalResponseList.add(hospitalResponse);
                        }
                        mediaResponsePage.setHospitalResponseList(hospitalResponseList);
                        mediaResponsePage.setPageIndex(page.getNumber());
                        mediaResponsePage.setPageSize(page.getSize());
                        mediaResponsePage.setTotalElement(page.getTotalElements());
                        mediaResponsePage.setTotalPages(page.getTotalPages());
                        mediaResponsePage.setIsFirstPage(page.isFirst());
                        mediaResponsePage.setIsLastPage(page.isLast());
                        //   mediaResponsePage.setTotalPharmacistCount(pharmacistCount.stream().count());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                        log.info("Record found! status - {}", mediaResponsePage);
                    } else {
                        //   mediaResponsePage.setTotalPharmacistCount(pharmacistCount.stream().count());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllHospital);
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
}
