package com.janaushadhi.adminservice.serviceimpl;

import com.janaushadhi.adminservice.entity.ContactDetail;
import com.janaushadhi.adminservice.entity.EventGallery;
import com.janaushadhi.adminservice.entity.MediaCalender;
import com.janaushadhi.adminservice.repository.EventRepositiory;
import com.janaushadhi.adminservice.repository.MediaRepository;
import com.janaushadhi.adminservice.requestpayload.EventGalleryRequest;
import com.janaushadhi.adminservice.requestpayload.EventRequest;
import com.janaushadhi.adminservice.requestpayload.GetAllDeleteGallery;
import com.janaushadhi.adminservice.responsepayload.*;
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
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class MediaServiceImpl {


    @Autowired
    private EventRepositiory eventRepositiory;
    @Autowired
    private MediaRepository mediaRepository;

    public Map<String, Object> addEventCalender(EventRequest eventRequest) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {

            if (eventRequest.getEventTitle()!=null ||! eventRequest.getEventTitle().trim().isEmpty()
                    && eventRequest.getEventDate()!=null||! eventRequest.getEventDate().trim().isEmpty()
                    &&eventRequest.getYear()!=null|| !eventRequest.getYear().equals(0)
                    && eventRequest.getMonth()!=null || !eventRequest.getMonth().trim().isEmpty()) {
                MediaCalender calender = new MediaCalender();

                calender.setEventsTitle(eventRequest.getEventTitle());
                calender.setEventDate(eventRequest.getEventDate());
                calender.setMonth(eventRequest.getMonth());
                calender.setYear(eventRequest.getYear());
                calender.setCreatedDate(new Date());
                calender.setStatus(DataConstant.ONE);
                MediaCalender save = mediaRepository.save(calender);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.MEDIA_EVENT_ADDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.MEDIA_EVENT_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
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


    public Map<String,Object> getAllMediaEventByMonthAndYear(GetAllMedia getAllMedia) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllMedia.getPageIndex()==null && getAllMedia.getPageSize()==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<MediaResponse> mediaResponseList = new ArrayList<>();
            MediaResponsePage mediaResponsePage = new MediaResponsePage();
            List<MediaCalender> mediaList = new ArrayList<>();
            Pageable pageable = null;
            Page<MediaCalender> page = null;
            Sort sort = Sort.by(Sort.Direction.ASC, "id");


            if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0) {
                if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 &&  getAllMedia.getYear()== 0 &&   getAllMedia.getMonth().trim().isEmpty()) {
                    mediaList = mediaRepository.findAll(sort);
                }else if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0  && getAllMedia.getYear() != 0 &&  getAllMedia.getMonth()!=null &&! getAllMedia.getMonth().trim().isEmpty()) {
                    mediaList = mediaRepository.findAllByYearAndMonth(getAllMedia.getYear(),getAllMedia.getMonth(),sort);
                }
                if (mediaList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", mediaList);
                    int index=0;
                    for (MediaCalender media : mediaList) {
                        MediaResponse mediaResponse = new MediaResponse();
                        BeanUtils.copyProperties(media, mediaResponse);
                        mediaResponse.setCreatedDate(DateUtil.convertDateToStringDate(media.getCreatedDate()));
                        mediaResponse.setUpdatedDate(DateUtil.convertDateToStringDate(media.getUpdatedDate()));
                     // for frontEnd team pagination
    					if (getAllMedia.getPageIndex() == 0) {
    						mediaResponse.setSerialNo(index + 1);
    						index++;
    					} else {
    						mediaResponse.setSerialNo((getAllMedia.getPageIndex() * getAllMedia.getPageSize()) + (index + 1));
    						index++;
    					}
                        mediaResponseList.add(mediaResponse);
                    }
                    mediaResponsePage.setMediaResponseList(mediaResponseList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                    log.info("Record found! status - {}", mediaResponsePage);
                    return map;
                } else {
                	map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            }
            else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null) {
                if (getAllMedia.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllMedia.getPageIndex(), getAllMedia.getPageSize(),sort);
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != 0 &&  getAllMedia.getYear() == 0 &&   getAllMedia.getMonth().trim().isEmpty()) {
                        page = mediaRepository.findAll(pageable);
                    }else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() !=null  &&getAllMedia.getYear()!=0   &&  getAllMedia.getMonth()!=null && !getAllMedia.getMonth().trim().isEmpty()) {
                        page = mediaRepository.findAllByYearAndMonth(getAllMedia.getYear(), getAllMedia.getMonth(),pageable);
                    }

                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", mediaList);
                        mediaList = page.getContent();
                        for (MediaCalender mediaCalender : mediaList) {
                            MediaResponse mediaResponse = new MediaResponse();
                            BeanUtils.copyProperties(mediaCalender, mediaResponse);
                            mediaResponse.setCreatedDate(DateUtil.convertDateToStringDate(mediaCalender.getCreatedDate()));
                            mediaResponse.setUpdatedDate(DateUtil.convertDateToStringDate(mediaCalender.getUpdatedDate()));
                            mediaResponseList.add(mediaResponse);
                        }
                        mediaResponsePage.setMediaResponseList(mediaResponseList);
                        mediaResponsePage.setPageIndex(page.getNumber());
                        mediaResponsePage.setPageSize(page.getSize());
                        mediaResponsePage.setTotalElement(page.getTotalElements());
                        mediaResponsePage.setTotalPages(page.getTotalPages());
                        mediaResponsePage.setIsFirstPage(page.isFirst());
                        mediaResponsePage.setIsLastPage(page.isLast());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                        log.info("Record found! status - {}", mediaResponsePage);
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, mediaResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllMedia);
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



    public Map<String, Object> addEventGallery(EventGalleryRequest eventRequest) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            if(eventRequest.getId()!=null &&  eventRequest.getId()!=0) {

            Optional<EventGallery> byId = eventRepositiory.findById(eventRequest.getId());
                if (byId.isPresent()) {
                    EventGallery eventGallery = byId.get();
                    BeanUtils.copyProperties(eventRequest, eventGallery);
                    eventGallery.setUpdatedDate(new Date());
                    EventGallery saveEvent = eventRepositiory.save(eventGallery);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.EVENT_UPDATED);
                    map.put(DataConstant.RESPONSE_BODY, saveEvent);
                    log.info("event updated successfully: {}", DataConstant.EVENT_UPDATED);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.ID_NOT_FOUND);
                    map.put(DataConstant.RESPONSE_BODY, null);
                    log.info("event with id {} not found: {}", eventRequest.getId(), DataConstant.ID_NOT_FOUND);
                    return map;
                }
            }

            if (eventRequest.getEventTitle()!=null &&! eventRequest.getEventTitle().trim().isEmpty()
                    && eventRequest.getEventDate()!=null && ! eventRequest.getEventDate().trim().isEmpty()
                   && eventRequest.getImages() !=null  ) {
                EventGallery gallery = new EventGallery();

                gallery.setEventTitle(eventRequest.getEventTitle());
                gallery.setEventDate(eventRequest.getEventDate());
                gallery.setCreatedDate(new Date());
                gallery.setEventCategory(eventRequest.getEventCategory());
                gallery.setImages(eventRequest.getImages());
                gallery.setCreatedDate(new Date());
                gallery.setStatus(DataConstant.ONE);
                EventGallery save = eventRepositiory.save(gallery);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.EVENT_GALLERY_ADDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.EVENT_GALLERY_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
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



    public Map<String,Object> getAllMediaGallery(GetAllEventGallery getAllMedia) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllMedia.getPageIndex()==null && getAllMedia.getPageSize()==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<EventGalleryResponse> galleryResponses = new ArrayList<>();
            EventGalleryResponsePage galleryResponsePage = new EventGalleryResponsePage();
            List<EventGallery> galleryList = new ArrayList<>();
            Pageable pageable = null;
            Page<EventGallery> page = null;
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            Short status = 2;

            if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0) {
                    if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 &&  getAllMedia.getSearchText().trim().isEmpty() &&  getAllMedia.getColumnName().trim().isEmpty() &&  getAllMedia.getOrderBy().trim().isEmpty()) {
                        galleryList = eventRepositiory.findAllByStatusNot(status);
                    }else if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 && getAllMedia.getSearchText() != null &&  getAllMedia.getColumnName().trim().isEmpty() &&  getAllMedia.getOrderBy().trim().isEmpty()) {
                    galleryList = eventRepositiory.findAllBySearchText(getAllMedia.getSearchText());
                } else if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 &&  getAllMedia.getSearchText().trim().isEmpty() && getAllMedia.getColumnName() != null && getAllMedia.getOrderBy().equals(DataConstant.ASC)) {
                    galleryList = eventRepositiory.searchAndOrderByASC(getAllMedia.getColumnName());
                } else if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 && getAllMedia.getSearchText().trim().isEmpty() && getAllMedia.getColumnName() != null && getAllMedia.getOrderBy().equals(DataConstant.DESC)) {
                    galleryList = eventRepositiory.searchAndOrderByDESC(getAllMedia.getColumnName());
                } else if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 && getAllMedia.getSearchText() != null && getAllMedia.getColumnName() != null && getAllMedia.getOrderBy().equals(DataConstant.ASC)) {
                    galleryList = eventRepositiory.findASC(getAllMedia.getSearchText(), getAllMedia.getColumnName());
                } else if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 && getAllMedia.getSearchText() != null && getAllMedia.getColumnName() != null && getAllMedia.getOrderBy().equals(DataConstant.DESC)) {
                    galleryList = eventRepositiory.findDESC(getAllMedia.getSearchText(), getAllMedia.getColumnName());
                }

                if (galleryList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", galleryList);
                    int index=0;
                    for (EventGallery eventGallery : galleryList) {
                        EventGalleryResponse eventGalleryResponse = new EventGalleryResponse();
                        BeanUtils.copyProperties(eventGallery, eventGalleryResponse);
//                        eventGalleryResponse.setCreatedDate(DateUtil.convertDateToStringDate(eventGallery.getCreatedDate()));
//                        eventGalleryResponse.setUpdatedDate(DateUtil.convertDateToStringDate(eventGallery.getUpdatedDate()));
                        // for frontEnd team pagination
                        if (getAllMedia.getPageIndex() == 0) {
                            eventGalleryResponse.setSerialNo(index + 1);
                            index++;
                        } else {
                            eventGalleryResponse.setSerialNo((getAllMedia.getPageIndex() * getAllMedia.getPageSize()) + (index + 1));
                            index++;
                        }
                        galleryResponses.add(eventGalleryResponse);
                    }
                    galleryResponsePage.setGalleryResponseList(galleryResponses);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                    log.info("Record found! status - {}", galleryResponsePage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            }
            else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null) {
                if (getAllMedia.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllMedia.getPageIndex(), getAllMedia.getPageSize(),sort);
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != 0 &&  getAllMedia.getSearchText().trim().isEmpty() &&  getAllMedia.getColumnName().trim().isEmpty() &&  getAllMedia.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = eventRepositiory.findAllByStatusNot(status,pageable);

                    } else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null && getAllMedia.getSearchText() != null &&  getAllMedia.getColumnName().trim().isEmpty() &&  getAllMedia.getOrderBy().trim().isEmpty()) {
                        page = eventRepositiory.findAllByUserName(getAllMedia.getSearchText(), pageable);
                    } else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null &&  getAllMedia.getSearchText().trim().isEmpty() && getAllMedia.getColumnName() != null && getAllMedia.getOrderBy().equals(DataConstant.ASC)) {
                        page = eventRepositiory.searchAndOrderByASC(getAllMedia.getColumnName(), pageable);
                    } else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null &&  getAllMedia.getSearchText().trim().isEmpty() && getAllMedia.getColumnName() != null && getAllMedia.getOrderBy().equals(DataConstant.DESC)) {
                        page = eventRepositiory.searchAndOrderByDESC(getAllMedia.getColumnName(), pageable);
                    } else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null && getAllMedia.getSearchText() != null  && !getAllMedia.getSearchText().trim().isEmpty()&& getAllMedia.getColumnName() != null && ! getAllMedia.getColumnName().trim().isEmpty() && getAllMedia.getOrderBy().equals(DataConstant.ASC)) {
                        page = eventRepositiory.findASC(getAllMedia.getSearchText(), getAllMedia.getColumnName(), pageable);
                    } else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null && getAllMedia.getSearchText() != null && ! getAllMedia.getSearchText().trim().isEmpty() && getAllMedia.getColumnName() != null  && ! getAllMedia.getColumnName().trim().isEmpty()&& getAllMedia.getOrderBy().equals(DataConstant.DESC)) {
                        page = eventRepositiory.findDESC(getAllMedia.getSearchText(), getAllMedia.getColumnName(), pageable);
                    }

                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", galleryList);
                        galleryList = page.getContent();
                        for (EventGallery mediaCalender : galleryList) {
                            EventGalleryResponse mediaResponse = new EventGalleryResponse();
                            BeanUtils.copyProperties(mediaCalender, mediaResponse);
//                            mediaResponse.setCreatedDate(DateUtil.convertDateToStringDate(mediaCalender.getCreatedDate()));
//                            mediaResponse.setUpdatedDate(DateUtil.convertDateToStringDate(mediaCalender.getUpdatedDate()));
                            galleryResponses.add(mediaResponse);
                        }
                        galleryResponsePage.setGalleryResponseList(galleryResponses);
                        galleryResponsePage.setPageIndex(page.getNumber());
                        galleryResponsePage.setPageSize(page.getSize());
                        galleryResponsePage.setTotalElement(page.getTotalElements());
                        galleryResponsePage.setTotalPages(page.getTotalPages());
                        galleryResponsePage.setIsFirstPage(page.isFirst());
                        galleryResponsePage.setIsLastPage(page.isLast());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                        log.info("Record found! status - {}", galleryResponsePage);
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllMedia);
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

    public Map<String,Object> getAllDeleteMediaGallery(GetAllDeleteGallery getAllMedia) {
        Map<String, Object> map = new HashMap<>();
        try {
            if(getAllMedia.getPageIndex()==null && getAllMedia.getPageSize()==null) {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
            }
            List<EventGalleryResponse> galleryResponses = new ArrayList<>();
            EventGalleryResponsePage galleryResponsePage = new EventGalleryResponsePage();
            List<EventGallery> galleryList = new ArrayList<>();
            Pageable pageable = null;
            Page<EventGallery> page = null;
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            Short status = 2;

            if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0) {
                if (getAllMedia.getPageIndex() == 0 && getAllMedia.getPageSize() == 0 ) {
                    galleryList = eventRepositiory.findAllByStatus(status);
                }

                if (galleryList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", galleryList);
                    int index=0;
                    for (EventGallery eventGallery : galleryList) {
                        EventGalleryResponse eventGalleryResponse = new EventGalleryResponse();
                        BeanUtils.copyProperties(eventGallery, eventGalleryResponse);
//                        eventGalleryResponse.setCreatedDate(DateUtil.convertDateToStringDate(eventGallery.getCreatedDate()));
//                        eventGalleryResponse.setUpdatedDate(DateUtil.convertDateToStringDate(eventGallery.getUpdatedDate()));
                        // for frontEnd team pagination
                        if (getAllMedia.getPageIndex() == 0) {
                            eventGalleryResponse.setSerialNo(index + 1);
                            index++;
                        } else {
                            eventGalleryResponse.setSerialNo((getAllMedia.getPageIndex() * getAllMedia.getPageSize()) + (index + 1));
                            index++;
                        }
                        galleryResponses.add(eventGalleryResponse);
                    }
                    galleryResponsePage.setGalleryResponseList(galleryResponses);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                    log.info("Record found! status - {}", galleryResponsePage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            }
            else if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != null) {
                if (getAllMedia.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllMedia.getPageIndex(), getAllMedia.getPageSize(),sort);
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllMedia.getPageIndex() != null && getAllMedia.getPageSize() != 0 ) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = eventRepositiory.findAllByStatus(status,pageable);

                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", galleryList);
                        galleryList = page.getContent();
                        for (EventGallery mediaCalender : galleryList) {
                            EventGalleryResponse mediaResponse = new EventGalleryResponse();
                            BeanUtils.copyProperties(mediaCalender, mediaResponse);
//                            mediaResponse.setCreatedDate(DateUtil.convertDateToStringDate(mediaCalender.getCreatedDate()));
//                            mediaResponse.setUpdatedDate(DateUtil.convertDateToStringDate(mediaCalender.getUpdatedDate()));
                            galleryResponses.add(mediaResponse);
                        }
                        galleryResponsePage.setGalleryResponseList(galleryResponses);
                        galleryResponsePage.setPageIndex(page.getNumber());
                        galleryResponsePage.setPageSize(page.getSize());
                        galleryResponsePage.setTotalElement(page.getTotalElements());
                        galleryResponsePage.setTotalPages(page.getTotalPages());
                        galleryResponsePage.setIsFirstPage(page.isFirst());
                        galleryResponsePage.setIsLastPage(page.isLast());
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                        log.info("Record found! status - {}", galleryResponsePage);
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, galleryResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllMedia);
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
