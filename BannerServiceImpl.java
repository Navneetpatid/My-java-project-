package com.janaushadhi.adminservice.serviceimpl;

import com.janaushadhi.adminservice.entity.Banner;
import com.janaushadhi.adminservice.repository.BannerRepository;
import com.janaushadhi.adminservice.requestpayload.BannerRequest;
import com.janaushadhi.adminservice.requestpayload.GetBannerRequest;
import com.janaushadhi.adminservice.responsepayload.BannerPageResponse;
import com.janaushadhi.adminservice.responsepayload.BannerResponse;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteBanner;
import com.janaushadhi.adminservice.responsepayload.GetAlldeleteBlackList;
import com.janaushadhi.adminservice.service.BannerService;
import com.janaushadhi.adminservice.util.DataConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @SameerKhan
 */
@Service
@Slf4j
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @Override
    public Map<Object, Object> addBanner(BannerRequest request) {
        Map<Object, Object> response = new HashMap<>();
        Banner bannerData = null;
        BannerResponse bannerResponse = new BannerResponse();
        try {
            if (request.getId() == 0) {
                Banner bannerEntity = new Banner();
                BeanUtils.copyProperties(request, bannerEntity);
                bannerEntity.setStatus((short) 1);
                bannerEntity.setCreatedDate(new Date());
                bannerRepository.save(bannerEntity);
                BeanUtils.copyProperties(bannerEntity, bannerResponse);

                response.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                response.put(DataConstant.MESSAGE, DataConstant.BANNER_ADD_SUCCESSFULLY);
                response.put(DataConstant.DATA, bannerResponse);
                // Log success message
                log.info("New product added successfully: {}", bannerResponse);
                return response;

            } else {
                bannerData = bannerRepository.findById(request.getId()).orElse(null);
                if (bannerData != null) {
                    BeanUtils.copyProperties(request, bannerData);
                    bannerData.setUpdatedDate(new Date());
                    bannerRepository.save(bannerData);

                    BeanUtils.copyProperties(bannerData, bannerResponse);
                    response.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    response.put(DataConstant.MESSAGE, DataConstant.BANNER_UPDATE_SUCCESSFULLY);
                    // Log success message
                    log.info("Product updated successfully: {}", bannerData);
                    return response;
                } else {
                    response.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    response.put(DataConstant.MESSAGE, DataConstant.BANNER_NOT_UPDATE_SUCCESSFULLY);
                    // Log failure message
                    log.error("Failed to update product: No product found with ID {}", request.getId());
                    return response;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while processing product data", e.getMessage());
            response.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            response.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            return response;
        }
    }

    /**
     * @param id
     * @return
     * @SameerKhan
     */
    @Override
    public Map<Object, Object> getBannerById(Long id) {
        Map<Object, Object> getBannerByIdMap = new HashMap<>();

        Banner bannerLoop = null;
        try {
            bannerLoop = this.bannerRepository.findById(id).orElse(null);

            if (bannerLoop != null) {
                BannerResponse bannerResponsePayLoad = new BannerResponse();
                BeanUtils.copyProperties(bannerLoop, bannerResponsePayLoad);
                if (bannerLoop.getCreatedDate() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DataConstant.Date);
                    String formattedDate = sdf.format(bannerLoop.getCreatedDate());
                    bannerResponsePayLoad.setCreatedDate(formattedDate);
                }
                if (bannerLoop.getUpdatedDate() != null) {
                    SimpleDateFormat udate = new SimpleDateFormat(DataConstant.Date);// HH:mm:ss
                    String formattedDateUd = udate.format(bannerLoop.getUpdatedDate());
                    bannerResponsePayLoad.setUpdatedDate(formattedDateUd);
                }
                getBannerByIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                getBannerByIdMap.put(DataConstant.MESSAGE, DataConstant.BANNER_FOUND);
                getBannerByIdMap.put(DataConstant.DATA, bannerResponsePayLoad);

                log.info("Products found successfully.");
            } else {
                getBannerByIdMap.put(DataConstant.OBJECT_RESPONSE, bannerLoop);
                getBannerByIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                getBannerByIdMap.put(DataConstant.MESSAGE, DataConstant.BANNER_NOT_FOUND);
                log.info("No products found.");
            }
        } catch (Exception e) {
            getBannerByIdMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getBannerByIdMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching products: " + e.getMessage());
        }
        return getBannerByIdMap;
    }

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @Override
    public Map<String, Object> getAllBanner(GetBannerRequest request) {

        Map<String, Object> getAllBannerMap = new HashMap<>();
        try {
            List<BannerResponse> bannerArrayList = new ArrayList<>();
            BannerPageResponse bannerPageResponseLoop = new BannerPageResponse();
            List<Banner> mainbannerList = new ArrayList<>();
            Pageable pageable = null;
            Page<Banner> page = null;

            if (request.getPageIndex() == 0 && request.getPageSize() == 0) {
                if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getColumnName() == null || request.getColumnName().trim().isEmpty() && request.getOrderBy() == null || request.getOrderBy().trim().isEmpty()) {
                    mainbannerList = bannerRepository.findAllNewProductroductAndStatusNot();
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.ASC)) {
                    mainbannerList = bannerRepository.getAndOrderByASC(request.getColumnName());
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.DESC)) {
                    mainbannerList = bannerRepository.getAndOrderByDESC(request.getColumnName());
                }
                if (mainbannerList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", mainbannerList);
                    for (Banner bannerDetail : mainbannerList) {
                        BannerResponse bannerResponsePayLoad = new BannerResponse();
                        BeanUtils.copyProperties(bannerDetail, bannerResponsePayLoad);
                        if (bannerDetail.getCreatedDate() != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat(DataConstant.Date);
                            String formattedDate = sdf.format(bannerDetail.getCreatedDate());
                            bannerResponsePayLoad.setCreatedDate(formattedDate);
                        }
                        if (bannerDetail.getUpdatedDate() != null) {
                            SimpleDateFormat udate = new SimpleDateFormat(DataConstant.Date);// HH:mm:ss
                            String formattedDateUd = udate.format(bannerDetail.getUpdatedDate());
                            bannerResponsePayLoad.setUpdatedDate(formattedDateUd);
                        }
                        bannerArrayList.add(bannerResponsePayLoad);
                    }
                    bannerPageResponseLoop.setBannerResponseLoopList(bannerArrayList);
                    getAllBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllBannerMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllBannerMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                    log.info("Record found! status - {}", bannerPageResponseLoop);
                    return getAllBannerMap;
                } else {
                    getAllBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllBannerMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllBannerMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                    log.info("Record not found! status - {}");
                    return getAllBannerMap;
                }
            } else if (request.getPageIndex() != null && request.getPageSize() != null) {
                if (request.getPageSize() >= 1) {
                    pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (request.getPageIndex() != null && request.getPageSize() != 0 && request.getColumnName() == null || request.getColumnName().trim().isEmpty() && request.getOrderBy() == null || request.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = bannerRepository.findAllAndStatus(pageable);
                    } else if (request.getPageIndex() != null && request.getPageSize() != null && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.ASC)) {
                        page = bannerRepository.getAndOrderByPageASC(request.getColumnName(), pageable);
                    } else if (request.getPageIndex() != null && request.getPageSize() != null && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.DESC)) {
                        page = bannerRepository.getAndOrderByPageDESC(pageable, request.getColumnName());
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", mainbannerList);
                        mainbannerList = page.getContent();
                        int index=0;
                        for (Banner bannerData : mainbannerList) {
                            BannerResponse bannerResponsePayLoad = new BannerResponse();
                            BeanUtils.copyProperties(bannerData, bannerResponsePayLoad);
                            if (bannerData.getCreatedDate() != null) {
                                SimpleDateFormat cdate = new SimpleDateFormat(DataConstant.Date);// HH:mm:ss
                                String formattedDateCd = cdate.format(bannerData.getCreatedDate());
                                bannerResponsePayLoad.setCreatedDate(formattedDateCd);
                            }
                            if (bannerData.getUpdatedDate() != null) {
                                SimpleDateFormat udate = new SimpleDateFormat(DataConstant.Date);// HH:mm:ss
                                String formattedDateUd = udate.format(bannerData.getUpdatedDate());
                                bannerResponsePayLoad.setUpdatedDate(formattedDateUd);
                            }
                            if(request.getPageIndex() == 0) {
                            	bannerResponsePayLoad.setSerialNo(index+1);
                        		index++;
                        	}else {
                        		bannerResponsePayLoad.setSerialNo((request.getPageSize()*request.getPageIndex())+(index+1));
                        		index++;
                        	}
                            bannerArrayList.add(bannerResponsePayLoad);
                        }
                        bannerPageResponseLoop.setBannerResponseLoopList(bannerArrayList);
                        bannerPageResponseLoop.setPageIndex(page.getNumber());
                        bannerPageResponseLoop.setPageSize(page.getSize());
                        bannerPageResponseLoop.setTotalElement(page.getTotalElements());
                        bannerPageResponseLoop.setTotalPages(page.getTotalPages());
                        bannerPageResponseLoop.setIsFirstPage(page.isFirst());
                        bannerPageResponseLoop.setIsLastPage(page.isLast());

                        getAllBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllBannerMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllBannerMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                        log.info("Record found! status - {}", bannerPageResponseLoop);
                    } else {
                        getAllBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllBannerMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllBannerMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    getAllBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllBannerMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info(DataConstant.PAGE_SIZE_MESSAGE);
                    return getAllBannerMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllBannerMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            getAllBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllBannerMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return getAllBannerMap;
    }

    /**
     * @param id
     * @param status
     * @return
     * @SameerKhan
     */
    @Override
    public Map<Object, Object> updateBannerStatus(Long id, short status) {
        Map<Object, Object> updateBannerMap = new HashMap<>();
        Banner getBanner = null;
        try {
            if(id!=null && id !=0) {
                getBanner = this.bannerRepository.findById(id).orElse(null);

                if (getBanner != null) {
                    getBanner.setStatus(status);
                    this.bannerRepository.save(getBanner);
//                if(status==2) {
//                    getBanner.setStatus(status);
//                    this.bannerRepository.save(getBanner);
//                }else {
//                getBanner.setStatus(status);
//                this.bannerRepository.save(getBanner);
//                }
                    if (status == 0) {
                        updateBannerMap.put(DataConstant.OBJECT_RESPONSE, getBanner);
                        updateBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        updateBannerMap.put(DataConstant.MESSAGE, DataConstant.DEACTIVATED_SUCCESSFULLY);
                    } else if (status == 1) {
                        updateBannerMap.put(DataConstant.OBJECT_RESPONSE, getBanner);
                        updateBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        updateBannerMap.put(DataConstant.MESSAGE, DataConstant.ACTIVATED_SUCCESSFULLY);
                    } else if (status == 2) {
                        updateBannerMap.put(DataConstant.OBJECT_RESPONSE, getBanner);
                        updateBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        updateBannerMap.put(DataConstant.MESSAGE, DataConstant.RECORD_DELETED_SUCCESSFULLY);
                    } else {
                        updateBannerMap.put(DataConstant.OBJECT_RESPONSE, null);
                        updateBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        updateBannerMap.put(DataConstant.MESSAGE, DataConstant.INVALID_REQUEST);
                    }
                } else {
                    updateBannerMap.put(DataConstant.OBJECT_RESPONSE, null);
                    updateBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    updateBannerMap.put(DataConstant.MESSAGE, DataConstant.BANNER_NOT_FOUND);
                    log.info("No banner found.");

                }
            }
            else {
                updateBannerMap.put(DataConstant.OBJECT_RESPONSE, null);
                updateBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                updateBannerMap.put(DataConstant.MESSAGE, DataConstant.ID_NOT_NULL_AND_ZERO);
                log.info("No banner id  found.");

            }
        } catch (Exception e) {
            updateBannerMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            updateBannerMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching products: " + e.getMessage());
        }
        return updateBannerMap;
    }


    public Map<String, Object> getAllDeleteBanner(GetAllDeleteBanner request) {

        Map<String, Object> getAllDeleteMap = new HashMap<>();
        try {
            List<BannerResponse> bannerArrayList = new ArrayList<>();
            BannerPageResponse bannerPageResponseLoop = new BannerPageResponse();
            List<Banner> mainbannerList = new ArrayList<>();
            Pageable pageable = null;
            Page<Banner> page = null;
            Short status=2;
            if (request.getPageIndex() == 0 && request.getPageSize() == 0) {
                if (request.getPageIndex() == 0 && request.getPageSize() == 0 ) {
                    mainbannerList = bannerRepository.findAllByStatus(status);
                }
                if (mainbannerList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", mainbannerList);
                    for (Banner banner : mainbannerList) {
                        BannerResponse bannerResponsePayLoad = new BannerResponse();
                        BeanUtils.copyProperties(banner, bannerResponsePayLoad);
                        if (banner.getCreatedDate() != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat(DataConstant.Date);
                            String formattedDate = sdf.format(banner.getCreatedDate());
                            bannerResponsePayLoad.setCreatedDate(formattedDate);
                        }
                        if (banner.getUpdatedDate() != null) {
                            SimpleDateFormat udate = new SimpleDateFormat(DataConstant.Date);// HH:mm:ss
                            String formattedDateUd = udate.format(banner.getUpdatedDate());
                            bannerResponsePayLoad.setUpdatedDate(formattedDateUd);
                        }
                        bannerArrayList.add(bannerResponsePayLoad);
                    }
                    bannerPageResponseLoop.setBannerResponseLoopList(bannerArrayList);
                    getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllDeleteMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                    log.info("Record found! status - {}", bannerPageResponseLoop);
                    return getAllDeleteMap;
                } else {
                    getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllDeleteMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                    log.info("Record not found! status - {}");
                    return getAllDeleteMap;
                }
            } else if (request.getPageIndex() != null && request.getPageSize() != null) {
                if (request.getPageSize() >= 1) {
                    pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (request.getPageIndex() != null && request.getPageSize() != 0 ) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = bannerRepository.findAllByStatus(status,pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", mainbannerList);
                        mainbannerList = page.getContent();
                        int index=0;
                        for (Banner bannerLoop : mainbannerList) {
                            BannerResponse bannerResponsePayLoad = new BannerResponse();
                            BeanUtils.copyProperties(bannerLoop, bannerResponsePayLoad);
                            if (bannerLoop.getCreatedDate() != null) {
                                SimpleDateFormat cdate = new SimpleDateFormat(DataConstant.Date);// HH:mm:ss
                                String formattedDateCd = cdate.format(bannerLoop.getCreatedDate());
                                bannerResponsePayLoad.setCreatedDate(formattedDateCd);
                            }
                            if (bannerLoop.getUpdatedDate() != null) {
                                SimpleDateFormat udate = new SimpleDateFormat(DataConstant.Date);// HH:mm:ss
                                String formattedDateUd = udate.format(bannerLoop.getUpdatedDate());
                                bannerResponsePayLoad.setUpdatedDate(formattedDateUd);
                            }
                            if(request.getPageIndex() == 0) {
                                bannerResponsePayLoad.setSerialNo(index+1);
                                index++;
                            }else {
                                bannerResponsePayLoad.setSerialNo((request.getPageSize()*request.getPageIndex())+(index+1));
                                index++;
                            }
                            bannerArrayList.add(bannerResponsePayLoad);
                        }
                        bannerPageResponseLoop.setBannerResponseLoopList(bannerArrayList);
                        bannerPageResponseLoop.setPageIndex(page.getNumber());
                        bannerPageResponseLoop.setPageSize(page.getSize());
                        bannerPageResponseLoop.setTotalElement(page.getTotalElements());
                        bannerPageResponseLoop.setTotalPages(page.getTotalPages());
                        bannerPageResponseLoop.setIsFirstPage(page.isFirst());
                        bannerPageResponseLoop.setIsLastPage(page.isLast());

                        getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllDeleteMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                        log.info("Record found! status - {}", bannerPageResponseLoop);
                    } else {
                        getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllDeleteMap.put(DataConstant.RESPONSE_BODY, bannerPageResponseLoop);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                  log.info( DataConstant.PAGE_SIZE_MESSAGE);
                    return getAllDeleteMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            getAllDeleteMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllDeleteMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return getAllDeleteMap;
    }

}
