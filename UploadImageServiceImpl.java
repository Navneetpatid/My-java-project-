package com.janaushadhi.adminservice.serviceimpl;

import com.janaushadhi.adminservice.entity.UploadImage;
import com.janaushadhi.adminservice.entity.UploadImageHindi;
import com.janaushadhi.adminservice.repository.UploadImageHindiRepository;
import com.janaushadhi.adminservice.repository.UploadImageRepository;
import com.janaushadhi.adminservice.requestpayload.GetImageRequest;
import com.janaushadhi.adminservice.requestpayload.UploadImageRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllDeleteImage;
import com.janaushadhi.adminservice.responsepayload.UploadImageHindiResponse;
import com.janaushadhi.adminservice.responsepayload.UploadImagePageResponse;
import com.janaushadhi.adminservice.responsepayload.UploadImageResponse;
import com.janaushadhi.adminservice.service.UploadImageService;
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

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class UploadImageServiceImpl implements UploadImageService {


    @Autowired
    UploadImageRepository uploadImageRepository;
    
    @Autowired
    UploadImageHindiRepository uploadImageHindiRepository;

    /**
     * @param request
     * @return
     * @SameerKhan
     */
    @Override
    public Map<Object, Object> addImage(UploadImageRequest request) {
        Map<Object, Object> response = new HashMap<>();
        UploadImage uploadImageData = null;
        UploadImageResponse uploadImageResponse = new UploadImageResponse();
        UploadImageHindiResponse uploadImageHindiResponse = new UploadImageHindiResponse();
        try {
            if (request.getId() == 0) {
                UploadImage uploadImageEntity = new UploadImage();
                BeanUtils.copyProperties(request, uploadImageEntity);
                UploadImageHindi uploadImageHindi=new UploadImageHindi(); 
                BeanUtils.copyProperties(request.getUploadImageHindirequest(), uploadImageHindi);
                uploadImageHindi=uploadImageHindiRepository.save(uploadImageHindi);
                uploadImageEntity.setUploadImageHindi(uploadImageHindi);
                uploadImageEntity.setStatus((short) 1);
                uploadImageEntity.setCreatedDate(new Date());
                uploadImageRepository.save(uploadImageEntity);
                BeanUtils.copyProperties(uploadImageEntity, uploadImageResponse);
                BeanUtils.copyProperties(uploadImageEntity.getUploadImageHindi(), uploadImageHindiResponse);
                uploadImageResponse.setUploadImageHindiResponse(uploadImageHindiResponse);
                response.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                response.put(DataConstant.MESSAGE, DataConstant.IMAGE_ADD_SUCCESSFULLY);
                response.put(DataConstant.DATA, uploadImageResponse);
                // Log success message
                log.info("Image added successfully: {}", uploadImageResponse);
                return response;

            } else {
                uploadImageData = uploadImageRepository.findById(request.getId()).orElse(null);
                if (uploadImageData != null) {
                    BeanUtils.copyProperties(request, uploadImageData);
                    UploadImageHindi uploadImageHindi=new UploadImageHindi();
                    //BeanUtils.copyProperties(request.getUploadImageHindirequest(), uploadImageHindi);
                    if(uploadImageData.getUploadImageHindi()!=null) {
                    uploadImageHindi = uploadImageHindiRepository.findById(uploadImageData.getUploadImageHindi().getId()).orElse(null);
                    }
                    	BeanUtils.copyProperties(request.getUploadImageHindirequest(), uploadImageHindi);
                        uploadImageHindi=uploadImageHindiRepository.save(uploadImageHindi);
                        uploadImageData.setUploadImageHindi(uploadImageHindi);
                         	
                    
                    uploadImageData.setUpdatedDate(new Date());
                    uploadImageRepository.save(uploadImageData);

                    BeanUtils.copyProperties(uploadImageData, uploadImageResponse);
                    BeanUtils.copyProperties(uploadImageData.getUploadImageHindi(), uploadImageHindiResponse);
                    uploadImageResponse.setUploadImageHindiResponse(uploadImageHindiResponse);
                    response.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    response.put(DataConstant.MESSAGE, DataConstant.Update_ADD_SUCCESSFULLY);
                    // Log success message
                    log.info("Image updated successfully: {}", uploadImageResponse);
                    return response;
                } else {
                    response.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    response.put(DataConstant.MESSAGE, DataConstant.IMAGE_NOT_UPDATE_SUCCESSFULLY);
                    // Log failure message
                    log.error("Failed to update Image: No Image found with ID {}", request.getId());
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
     * @param request
     * @return
     * @SameerKhan
     */
    @Override
    public Map<String, Object> getAllImage(GetImageRequest request) {

        Map<String, Object> getAllImageMap = new HashMap<>();
        try {
            List<UploadImageResponse> uploadImageArrayList = new ArrayList<>();
            UploadImagePageResponse uploadImagePageResponseLoop = new UploadImagePageResponse();
            List<UploadImage> mainUploadImageList = new ArrayList<>();
            Pageable pageable = null;
            Page<UploadImage> page = null;

            if (request.getPageIndex() == 0 && request.getPageSize() == 0) {
                if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText().trim().isEmpty() && request.getColumnName().trim().isEmpty() &&  request.getOrderBy().trim().isEmpty()) {
                    mainUploadImageList = uploadImageRepository.findAllNewProductroductAndStatusNot();
                }else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText()!=null && !request.getSearchText().trim().isEmpty() && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.ASC)) {
                    mainUploadImageList = uploadImageRepository.getSearchTextAndOrderByASC(request.getSearchText(),request.getColumnName());
                }
                else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText()!=null && !request.getSearchText().trim().isEmpty() && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.DESC)) {
                    mainUploadImageList = uploadImageRepository.getSearchTextAndOrderByDESC(request.getSearchText(),request.getColumnName());
                }
                else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.ASC)) {
                    mainUploadImageList = uploadImageRepository.getAndOrderByASC(request.getColumnName());
                } else if (request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.DESC)) {
                    mainUploadImageList = uploadImageRepository.getAndOrderByDESC(request.getColumnName());
                }else if(request.getPageIndex() == 0 && request.getPageSize() == 0 && request.getSearchText()!=null && !request.getSearchText().trim().isEmpty()) {
                	mainUploadImageList = uploadImageRepository.getSearchText(request.getSearchText());
                }
                
                if (mainUploadImageList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", mainUploadImageList);
                    for (UploadImage uploadImage : mainUploadImageList) {
                        UploadImageResponse uploadImageResponsePayLoad = new UploadImageResponse();
                        BeanUtils.copyProperties(uploadImage, uploadImageResponsePayLoad);

                        if (uploadImage.getCreatedDate() != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDate = sdf.format(uploadImage.getCreatedDate());
                            uploadImageResponsePayLoad.setCreatedDate(formattedDate);
                        }
                        if (uploadImage.getUpdatedDate() != null) {
                            SimpleDateFormat udate = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                            String formattedDateUd = udate.format(uploadImage.getUpdatedDate());
                            uploadImageResponsePayLoad.setUpdatedDate(formattedDateUd);
                        }
                        uploadImageArrayList.add(uploadImageResponsePayLoad);
                    }
                    uploadImagePageResponseLoop.setUploadImageLoopList(uploadImageArrayList);
                    getAllImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                    log.info("Record found! status - {}", uploadImagePageResponseLoop);
                    return getAllImageMap;
                } else {
                    getAllImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                    log.info("Record not found! status - {}");
                    return getAllImageMap;
                }
            } else if (request.getPageIndex() != null && request.getPageSize() != null) {
                if (request.getPageSize() >= 1) {
                    pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (request.getPageIndex() != null && request.getPageSize() != 0 && request.getSearchText().trim().isEmpty() && request.getColumnName().trim().isEmpty() && request.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = uploadImageRepository.findAllAndStatus(pageable);
                    }else if (request.getPageIndex() != null && request.getPageSize() != null && request.getSearchText()!=null && !request.getSearchText().trim().isEmpty() && request.getColumnName() != null && !request.getColumnName().trim().isEmpty()&& request.getOrderBy().equals(DataConstant.ASC)) {
                        page = uploadImageRepository.getSearchTextAndOrderByPageASC(request.getSearchText(),request.getColumnName(), pageable);
                    } else if (request.getPageIndex() != null && request.getPageSize() != null && request.getSearchText()!=null && !request.getSearchText().trim().isEmpty() && request.getColumnName() != null && !request.getColumnName().trim().isEmpty()&& request.getOrderBy().equals(DataConstant.DESC)) {
                        page = uploadImageRepository.getSearchTextAndOrderByPageDESC(request.getSearchText(),pageable, request.getColumnName());
                    }
                    else if (request.getPageIndex() != null && request.getPageSize() != null && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.ASC)) {
                        page = uploadImageRepository.getAndOrderByPageASC(request.getColumnName(), pageable);
                    } else if (request.getPageIndex() != null && request.getPageSize() != null && request.getColumnName() != null && request.getOrderBy().equals(DataConstant.DESC)) {
                        page = uploadImageRepository.getAndOrderByPageDESC(pageable, request.getColumnName());
                    }else if(request.getPageIndex() !=null && request.getPageSize() !=null  && request.getSearchText()!=null && !request.getSearchText().trim().isEmpty()) {
                    	page = uploadImageRepository.getSearchTextPage(request.getSearchText(),pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", mainUploadImageList);
                        mainUploadImageList = page.getContent();
                        int index=0;
                        for (UploadImage uploadImageLoop : mainUploadImageList) {
                            UploadImageResponse uploadImageResponsePayLoad = new UploadImageResponse();
                            UploadImageHindiResponse uploadImageHindiResponse = new UploadImageHindiResponse();
                            BeanUtils.copyProperties(uploadImageLoop, uploadImageResponsePayLoad);
                            if(uploadImageLoop.getUploadImageHindi()!=null) {
                            BeanUtils.copyProperties(uploadImageLoop.getUploadImageHindi(), uploadImageHindiResponse);
                            uploadImageResponsePayLoad.setUploadImageHindiResponse(uploadImageHindiResponse);
                            }
                            if (uploadImageLoop.getCreatedDate() != null) {
                                uploadImageResponsePayLoad.setCreatedDate(DateUtil.convertUtcToIst(uploadImageLoop.getCreatedDate()));
                            }
                            if (uploadImageLoop.getUpdatedDate() != null) {
                                uploadImageResponsePayLoad.setUpdatedDate(DateUtil.convertUtcToIst(uploadImageLoop.getUpdatedDate()));
                            }
                            //for frontEnd team pagination
                            if(request.getPageIndex() == 0) {
                            	uploadImageResponsePayLoad.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		uploadImageResponsePayLoad.setSerialNo((request.getPageSize()*request.getPageIndex())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
                            uploadImageArrayList.add(uploadImageResponsePayLoad);
                        }
                        uploadImagePageResponseLoop.setUploadImageLoopList(uploadImageArrayList);
                        uploadImagePageResponseLoop.setPageIndex(page.getNumber());
                        uploadImagePageResponseLoop.setPageSize(page.getSize());
                        uploadImagePageResponseLoop.setTotalElement(page.getTotalElements());
                        uploadImagePageResponseLoop.setTotalPages(page.getTotalPages());
                        uploadImagePageResponseLoop.setIsFirstPage(page.isFirst());
                        uploadImagePageResponseLoop.setIsLastPage(page.isLast());

                        getAllImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                        log.info("Record found! status - {}", uploadImagePageResponseLoop);
                    } else {
                        getAllImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    log.info("Invaild Page Size - {}");
                    getAllImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllImageMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    return getAllImageMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllImageMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception in getAll Image  : " + e.getMessage());
        } catch (Exception e) {
            getAllImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllImageMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception in getAll Image: " + e.getMessage());
        }
        return getAllImageMap;
    }

    @Override
    public Map<Object, Object> deleteImage(Long id) {
        Map<Object, Object> deleteImageMap = new HashMap<>();
        UploadImage uploadImage = null;
        try {
            uploadImage = this.uploadImageRepository.findByIdAndStatusNot(id, (short) 2).orElse(null);

            if (uploadImage != null) {

                uploadImage.setStatus((short) 2);
                this.uploadImageRepository.save(uploadImage);
                deleteImageMap.put(DataConstant.OBJECT_RESPONSE, uploadImage);
                deleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                deleteImageMap.put(DataConstant.MESSAGE, DataConstant.IMAGE_DELETE_SUCCESSFULLY);
                log.info("Image Delete Successfully .");
            } else {
                deleteImageMap.put(DataConstant.OBJECT_RESPONSE, null);
                deleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                deleteImageMap.put(DataConstant.MESSAGE, DataConstant.IMAGE_NOT_FOUND);
                log.info("image Not  found.");
            }
        } catch (Exception e) {
            deleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            deleteImageMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching products: " + e.getMessage());
        }
        return deleteImageMap;

    }

    public Map<String, Object> getAllDeleteImage(GetAllDeleteImage request) {

        Map<String, Object> getAllDeleteImageMap = new HashMap<>();
        try {
            List<UploadImageResponse> uploadImageArrayList = new ArrayList<>();
            UploadImagePageResponse uploadImagePageResponseLoop = new UploadImagePageResponse();
            List<UploadImage> mainUploadImageList = new ArrayList<>();
            Pageable pageable = null;
            Page<UploadImage> page = null;
            Short status= 2;
            if (request.getPageIndex() == 0 && request.getPageSize() == 0) {
                if (request.getPageIndex() == 0 && request.getPageSize() == 0 ) {
                    mainUploadImageList = uploadImageRepository.findAllByStatus(status);
                }

                if (mainUploadImageList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", mainUploadImageList);
                    for (UploadImage uploadImageDetail : mainUploadImageList) {
                        UploadImageResponse uploadImageResponsePayLoad = new UploadImageResponse();
                        BeanUtils.copyProperties(uploadImageDetail, uploadImageResponsePayLoad);

                        if (uploadImageDetail.getCreatedDate() != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDate = sdf.format(uploadImageDetail.getCreatedDate());
                            uploadImageResponsePayLoad.setCreatedDate(formattedDate);
                        }
                        if (uploadImageDetail.getUpdatedDate() != null) {
                            SimpleDateFormat udate = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                            String formattedDateUd = udate.format(uploadImageDetail.getUpdatedDate());
                            uploadImageResponsePayLoad.setUpdatedDate(formattedDateUd);
                        }
                        uploadImageArrayList.add(uploadImageResponsePayLoad);
                    }
                    uploadImagePageResponseLoop.setUploadImageLoopList(uploadImageArrayList);
                    getAllDeleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllDeleteImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllDeleteImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                    log.info("Record found! status - {}", uploadImagePageResponseLoop);
                    return getAllDeleteImageMap;
                } else {
                    getAllDeleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllDeleteImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllDeleteImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                    log.info("Record not found! status - {}");
                    return getAllDeleteImageMap;
                }
            } else if (request.getPageIndex() != null && request.getPageSize() != null) {
                if (request.getPageSize() >= 1) {
                    pageable = PageRequest.of(request.getPageIndex(), request.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (request.getPageIndex() != null && request.getPageSize() != 0 ) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = uploadImageRepository.findAllByStatus(status,pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", mainUploadImageList);
                        mainUploadImageList = page.getContent();
                        int index=0;
                        for (UploadImage uploadImageData : mainUploadImageList) {
                            UploadImageResponse uploadImageResponsePayLoad = new UploadImageResponse();
                            UploadImageHindiResponse uploadImageHindiResponse = new UploadImageHindiResponse();
                            BeanUtils.copyProperties(uploadImageData, uploadImageResponsePayLoad);
                            if(uploadImageData.getUploadImageHindi()!=null) {
                                BeanUtils.copyProperties(uploadImageData.getUploadImageHindi(), uploadImageHindiResponse);
                                uploadImageResponsePayLoad.setUploadImageHindiResponse(uploadImageHindiResponse);
                            }
                            if (uploadImageData.getCreatedDate() != null) {
                                uploadImageResponsePayLoad.setCreatedDate(DateUtil.convertUtcToIst(uploadImageData.getCreatedDate()));
                            }
                            if (uploadImageData.getUpdatedDate() != null) {
                                uploadImageResponsePayLoad.setUpdatedDate(DateUtil.convertUtcToIst(uploadImageData.getUpdatedDate()));
                            }
                            //for frontEnd team pagination
                            if(request.getPageIndex() == 0) {
                                uploadImageResponsePayLoad.setSerialNo(index+1);
                                index++;
                                //	System.out.println("index==="+index);
                            }else {
                                uploadImageResponsePayLoad.setSerialNo((request.getPageSize()*request.getPageIndex())+(index+1));
                                index++;
                                //	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                            }
                            uploadImageArrayList.add(uploadImageResponsePayLoad);
                        }
                        uploadImagePageResponseLoop.setUploadImageLoopList(uploadImageArrayList);
                        uploadImagePageResponseLoop.setPageIndex(page.getNumber());
                        uploadImagePageResponseLoop.setPageSize(page.getSize());
                        uploadImagePageResponseLoop.setTotalElement(page.getTotalElements());
                        uploadImagePageResponseLoop.setTotalPages(page.getTotalPages());
                        uploadImagePageResponseLoop.setIsFirstPage(page.isFirst());
                        uploadImagePageResponseLoop.setIsLastPage(page.isLast());

                        getAllDeleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllDeleteImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllDeleteImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                        log.info("Record found! status - {}", uploadImagePageResponseLoop);
                    } else {
                        getAllDeleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllDeleteImageMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllDeleteImageMap.put(DataConstant.RESPONSE_BODY, uploadImagePageResponseLoop);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    getAllDeleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllDeleteImageMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    return getAllDeleteImageMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllDeleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllDeleteImageMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
        } catch (Exception e) {
            getAllDeleteImageMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllDeleteImageMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
        }
        return getAllDeleteImageMap;
    }

}
