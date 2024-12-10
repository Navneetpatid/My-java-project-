package com.janaushadhi.adminservice.serviceimpl;
import com.janaushadhi.adminservice.entity.ContactDetail;
import com.janaushadhi.adminservice.repository.ContactRepository;
import com.janaushadhi.adminservice.requestpayload.ContactRequest;
import com.janaushadhi.adminservice.requestpayload.ContactsDeletePayload;
import com.janaushadhi.adminservice.responsepayload.*;
import com.janaushadhi.adminservice.util.DataConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ContactServiceImpl {

    @Autowired
    private ContactRepository contactRepository;


    public Map<String, Object> addContact(ContactRequest contactRequest) throws IOException {
        Map<String, Object> addContactMap = new HashMap<>();
        try {

            if(contactRequest.getId()!=null &&  contactRequest.getId()!=0) {

                Optional<ContactDetail> optionalDistributor = contactRepository.findById(contactRequest.getId());
                if (optionalDistributor.isPresent()) {
                    ContactDetail existingDistributor = optionalDistributor.get();

                    BeanUtils.copyProperties(contactRequest, existingDistributor);
                    existingDistributor.setUpdatedDate(new Date());
                    ContactDetail savedDistributor = contactRepository.save(existingDistributor);
                    addContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.CONTACT_UPDATED);
                    addContactMap.put(DataConstant.RESPONSE_BODY, savedDistributor);
                    log.info("contact updated successfully: {}", DataConstant.CONTACT_UPDATED);
                    return addContactMap;
                } else {
                    addContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    addContactMap.put(DataConstant.MESSAGE, DataConstant.ID_NOT_FOUND);
                    addContactMap.put(DataConstant.RESPONSE_BODY, null);
                    log.info("contact with id {} not found: {}", contactRequest.getId(), DataConstant.ID_NOT_FOUND);
                    return addContactMap;
                }
            }


            if (contactRequest.getName()!=null ||! contactRequest.getName().trim().isEmpty()
                    && contactRequest.getEmailId()!=null||! contactRequest.getEmailId().trim().isEmpty()
                    &&contactRequest.getDesignation()!=null||!contactRequest.getDesignation().trim().isEmpty()
                    && contactRequest.getDepartment()!=null&& !contactRequest.getDepartment().trim().isEmpty() 
                  && !contactRequest.getContactNo().trim().equals("")  && contactRequest.getContactNo()!=null 
                    &&contactRequest.getIntercomNo()!=null && !contactRequest.getIntercomNo().trim().isEmpty()) {
                ContactDetail contactDetail = new ContactDetail();
                contactDetail.setName(contactRequest.getName());
                contactDetail.setDesignation(contactRequest.getDesignation());
                contactDetail.setDepartment(contactRequest.getDepartment());
                contactDetail.setEmailId(contactRequest.getEmailId());
                contactDetail.setContactNo(contactRequest.getContactNo());
                contactDetail.setIntercomNo(contactRequest.getIntercomNo());
                contactDetail.setCreatedDate(new Date());
                contactDetail.setStatus(DataConstant.ONE);

                ContactDetail save = contactRepository.save(contactDetail);
                addContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                addContactMap.put(DataConstant.MESSAGE, DataConstant.CONTACT_ADDED_SUCCESSFULLY);
                addContactMap.put(DataConstant.RESPONSE_BODY, save);
                log.info(DataConstant.CONTACT_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                return addContactMap;
            } else {
                addContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                addContactMap.put(DataConstant.MESSAGE, DataConstant.ALL_FIELDS_ARE_MANDATORY);
                addContactMap.put(DataConstant.RESPONSE_BODY, null);
                log.info("All feilds are mandatory",DataConstant.ALL_FIELDS_ARE_MANDATORY);
                return addContactMap;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
		 }
        return addContactMap;
    }


    public Map<String, Object> contactStatusUpdate(Long id, short status) {
        Map<String, Object> contactStatusMap = new HashMap<>();
        Optional<ContactDetail> contactDetail = contactRepository.findById(id);
        if (contactDetail.isPresent()) {
            log.info("  contact Record found! status - {}",contactDetail );
            ContactDetail detail = contactDetail.get();
            BeanUtils.copyProperties(contactDetail, detail);
            detail.setStatus(status);

            ContactDetail saved = contactRepository.save(detail);
            contactStatusMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            contactStatusMap.put(DataConstant.MESSAGE, DataConstant.CONTACT_STATUS_UPDATE);
            contactStatusMap.put(DataConstant.RESPONSE_BODY, saved);
            log.info("contact status update- {}", saved);
            return contactStatusMap;
        } else {
            contactStatusMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
            contactStatusMap.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
            contactStatusMap.put(DataConstant.RESPONSE_BODY, null);
            log.error("contact id not present ", id);
            return contactStatusMap;
        }
    }


    public Map<String, Object> getByContactId(Long id) {
        Map<String, Object> getByContactMap = new HashMap<>();
        try {
            Short status= 2;
            Optional<ContactDetail> contactDetail = contactRepository.findByIdAndStatusNot(id,status);
            if (!contactDetail.isEmpty()) {
                getByContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                getByContactMap.put(DataConstant.MESSAGE, DataConstant.CONTACT_FOUNDED_SUCCESSFULLY);
                getByContactMap.put(DataConstant.RESPONSE_BODY,contactDetail );
                log.info("contact found successfully- {}",  DataConstant.CONTACT_FOUNDED_SUCCESSFULLY);
                return getByContactMap;
            } else {
                getByContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getByContactMap.put(DataConstant.RESPONSE_BODY, null);
                getByContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                log.info("contact  not found ",  DataConstant.RECORD_NOT_FOUND_MESSAGE);

                return getByContactMap;
            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
            getByContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getByContactMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            return getByContactMap;
        }
    }


    public Map<String, Object> getAllContact(GetAllContact getAllContact) {
        Map<String, Object> getAllContactMap = new HashMap<>();
        try {

            if(getAllContact.getPageIndex() ==null &&getAllContact. getPageSize() ==null) {
                getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getAllContactMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                getAllContactMap.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return getAllContactMap;
            }
            List<ContactResponse> contactResponseList = new ArrayList<>();
            ContactResponsePage contactResponsePage = new ContactResponsePage();
            List<ContactDetail> contactDetails = new ArrayList<>();
            Pageable pageable = null;
            Page<ContactDetail> page = null;
            Short status = 2; // for soft delete status  (contact)


            if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0) {
                if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 && getAllContact.getSearchText() == null || getAllContact.getSearchText().trim().isEmpty() && getAllContact.getColumnName() == null || getAllContact.getColumnName().trim().isEmpty() && getAllContact.getOrderBy() == null || getAllContact.getOrderBy().trim().isEmpty()) {
                    contactDetails = contactRepository.findAllByStatusNot(status);
                } else if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 && getAllContact.getSearchText() != null && getAllContact.getColumnName() == null || getAllContact.getColumnName().trim().isEmpty() && getAllContact.getOrderBy() == null || getAllContact.getOrderBy().trim().isEmpty()) {
                    contactDetails = contactRepository.findAllBySearchText(getAllContact.getSearchText());
                } else if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 && getAllContact.getSearchText() == null || getAllContact.getSearchText().trim().isEmpty() && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.ASC)) {
                    contactDetails = contactRepository.searchAndOrderByASC(getAllContact.getColumnName());
                } else if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 && getAllContact.getSearchText() == null || getAllContact.getSearchText().trim().isEmpty() && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.DESC)) {
                    contactDetails = contactRepository.searchAndOrderByDESC(getAllContact.getColumnName());
                } else if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 && getAllContact.getSearchText() != null && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.ASC)) {
                    contactDetails = contactRepository.findASC(getAllContact.getSearchText(), getAllContact.getColumnName());
                } else if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 && getAllContact.getSearchText() != null && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.DESC)) {
                    contactDetails = contactRepository.findDESC(getAllContact.getSearchText(), getAllContact.getColumnName());
                }
                if (contactDetails.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", contactDetails);
                    for (ContactDetail detail : contactDetails) {
                        ContactResponse contactResponse = new ContactResponse();
                        BeanUtils.copyProperties(detail, contactResponse);
                        contactResponseList.add(contactResponse);
                    }
                    contactResponsePage.setContactResponseList(contactResponseList);
                    getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getAllContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getAllContactMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                    log.info("Record found! status - {}", contactResponsePage);
                    return getAllContactMap;
                } else {
                    getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getAllContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getAllContactMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                    log.info("Record not found! status - {}");
                    return getAllContactMap;
                }
            } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null) {
                if (getAllContact.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllContact.getPageIndex(), getAllContact.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != 0 &&  getAllContact.getSearchText().trim().isEmpty() &&  getAllContact.getColumnName().trim().isEmpty() &&  getAllContact.getOrderBy().trim().isEmpty()) {
                        //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                        page = contactRepository.findAllByStatusNot(status,pageable);

                    } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null && getAllContact.getSearchText() != null &&  getAllContact.getColumnName().trim().isEmpty() &&  getAllContact.getOrderBy().trim().isEmpty()) {
                        page = contactRepository.findAllByUserName(getAllContact.getSearchText(), pageable);
                    } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null &&  getAllContact.getSearchText().trim().isEmpty() && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.ASC)) {
                        page = contactRepository.searchAndOrderByASC(getAllContact.getColumnName(), pageable);
                    } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null &&  getAllContact.getSearchText().trim().isEmpty() && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.DESC)) {
                        page = contactRepository.searchAndOrderByDESC(getAllContact.getColumnName(), pageable);
                    } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null && getAllContact.getSearchText() != null && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.ASC)) {
                        page = contactRepository.findASC(getAllContact.getSearchText(), getAllContact.getColumnName(), pageable);
                    } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null && getAllContact.getSearchText() != null && getAllContact.getColumnName() != null && getAllContact.getOrderBy().equals(DataConstant.DESC)) {
                        page = contactRepository.findDESC(getAllContact.getSearchText(), getAllContact.getColumnName(), pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", contactDetails);
                        contactDetails = page.getContent();
                        int index=0;
                        for (ContactDetail contactDetail : contactDetails) {
                            ContactResponse pharmacistResponsepayload = new ContactResponse();
                            BeanUtils.copyProperties(contactDetail, pharmacistResponsepayload);
                            //for frontEnd team pagination
                            if(getAllContact.getPageIndex() == 0) {
                            	pharmacistResponsepayload.setSerialNo(index+1);
                        		index++;
                        	//	System.out.println("index==="+index);
                        	}else {
                        		pharmacistResponsepayload.setSerialNo((getAllContact.getPageSize()*getAllContact.getPageIndex())+(index+1));
                        		index++;
                        	//	System.out.println("index==="+bannerResponsePayLoad.getSerialNo());
                        	}
                            contactResponseList.add(pharmacistResponsepayload);
                        }
                        contactResponsePage.setContactResponseList(contactResponseList);
                        contactResponsePage.setPageIndex(page.getNumber());
                        contactResponsePage.setPageSize(page.getSize());
                        contactResponsePage.setTotalElement(page.getTotalElements());
                        contactResponsePage.setTotalPages(page.getTotalPages());
                        contactResponsePage.setIsFirstPage(page.isFirst());
                        contactResponsePage.setIsLastPage(page.isLast());

                        getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getAllContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getAllContactMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                        log.info("Record found! status - {}", contactResponsePage);
                    } else {
                        getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getAllContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getAllContactMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return getAllContactMap;
                    }
                } else {
                    getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getAllContactMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllContact);
                    return getAllContactMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getAllContactMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return getAllContactMap;
        } catch (Exception e) {
            getAllContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getAllContactMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return getAllContactMap;
        }
        return getAllContactMap;
    }





    public Map<String, Object> getAllContactByDepartment(GetAllContact getAllContact) {
        Map<String, Object> departmentMap = new HashMap<>();
        try {

            if(getAllContact.getPageIndex() ==null &&getAllContact. getPageSize() ==null) {
                departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                departmentMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                departmentMap.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return departmentMap;
            }
            List<ContactResponse> contactResponseList = new ArrayList<>();
            ContactResponsePage contactResponsePage = new ContactResponsePage();
            List<ContactDetail> contactDetails = new ArrayList<>();
            Pageable pageable = null;
            Page<ContactDetail> page = null;
            Short status = 2; // for soft delete status  (contact)
            if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0) {
                if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 &&  getAllContact.getSearchText().trim().isEmpty() &&  getAllContact.getColumnName().trim().isEmpty() &&  getAllContact.getOrderBy().trim().isEmpty()) {
                    contactDetails = contactRepository.findAllByStatusNot(status);
                } else if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 && getAllContact.getSearchText() != null&&  !getAllContact.getSearchText().trim().isEmpty()  &&  getAllContact.getColumnName().trim().isEmpty() &&  getAllContact.getOrderBy().trim().isEmpty()) {

                    contactDetails = contactRepository.findAllByDepartmentAndStatusNot(getAllContact.getSearchText(),  status);
                }

                if (contactDetails.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", contactDetails);
                    for (ContactDetail detail : contactDetails) {
                        ContactResponse contactResponse = new ContactResponse();
                        BeanUtils.copyProperties(detail, contactResponse);
                        contactResponseList.add(contactResponse);
                    }
                    contactResponsePage.setContactResponseList(contactResponseList);
                    departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    departmentMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    departmentMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                    log.info("Record found! status - {}", contactResponsePage);
                    return departmentMap;
                } else {
                    departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    departmentMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    departmentMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                    log.info("Record not found! status - {}");
                    return departmentMap;
                }
            } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null) {
                if (getAllContact.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllContact.getPageIndex(), getAllContact.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != 0 && getAllContact.getSearchText().trim().isEmpty() &&  getAllContact.getColumnName().trim().isEmpty() &&  getAllContact.getOrderBy().trim().isEmpty()) {
                      page = contactRepository.findAllByStatusNot(status,pageable);

                    } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null && getAllContact.getSearchText() != null &&  !getAllContact.getSearchText().trim().isEmpty()&&  getAllContact.getColumnName().trim().isEmpty() &&  getAllContact.getOrderBy().trim().isEmpty()) {
                        page = contactRepository.findAllByDepartmentAndStatusNot(getAllContact.getSearchText(), pageable,status);
                    }

                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", contactDetails);
                        contactDetails = page.getContent();
                        int index=0;
                        for (ContactDetail contactDetail : contactDetails) {
                            ContactResponse pharmacistResponsepayload = new ContactResponse();
                            BeanUtils.copyProperties(contactDetail, pharmacistResponsepayload);
                            //for frontEnd team pagination
                            if(getAllContact.getPageIndex() == 0) {
                                pharmacistResponsepayload.setSerialNo(index+1);
                                index++;
                                }else {
                                pharmacistResponsepayload.setSerialNo((getAllContact.getPageSize()*getAllContact.getPageIndex())+(index+1));
                                index++;
                                }
                            contactResponseList.add(pharmacistResponsepayload);
                        }
                        contactResponsePage.setContactResponseList(contactResponseList);
                        contactResponsePage.setPageIndex(page.getNumber());
                        contactResponsePage.setPageSize(page.getSize());
                        contactResponsePage.setTotalElement(page.getTotalElements());
                        contactResponsePage.setTotalPages(page.getTotalPages());
                        contactResponsePage.setIsFirstPage(page.isFirst());
                        contactResponsePage.setIsLastPage(page.isLast());

                        departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        departmentMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        departmentMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                        log.info("Record found! status - {}", contactResponsePage);
                    } else {
                        departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        departmentMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        departmentMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return departmentMap;
                    }
                } else {
                    departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    departmentMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllContact);
                    return departmentMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            departmentMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return departmentMap;
        } catch (Exception e) {
            departmentMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            departmentMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return departmentMap;
        }
        return departmentMap;
    }


	public Map<Object, Object> deleteContactBulk(ContactsDeletePayload contactIds) {
		Map<Object, Object> ContactBulkMap = new HashMap<>();
        List<ContactDetail> contactDetailList = null;
        List<ContactDetail> list = new ArrayList<>();
        try {
        	if(contactIds.getContactIds().isEmpty()) {
                ContactBulkMap.put(DataConstant.OBJECT_RESPONSE, null);
                ContactBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                ContactBulkMap.put(DataConstant.MESSAGE, DataConstant.PLEASE_ADD_ID_TO_DELETE);
                log.info(DataConstant.PLEASE_ADD_ID_TO_DELETE);
                return ContactBulkMap;
        	}
        	contactDetailList = contactRepository.findAllById(contactIds.getContactIds());
            if(contactDetailList!=null && !contactDetailList.isEmpty()) {
            for(ContactDetail contact:contactDetailList) {
            	contact.setStatus((short)2);
            	list.add(contact);
            }
            list=contactRepository.saveAll(list);
            ContactBulkMap.put(DataConstant.RESPONSE_BODY, list);
            ContactBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            ContactBulkMap.put(DataConstant.MESSAGE, DataConstant.RECORD_DELETED_SUCCESSFULLY);
            log.info(DataConstant.RECORD_DELETED_SUCCESSFULLY);
            }
            else{
            ContactBulkMap.put(DataConstant.RESPONSE_BODY, null);
            ContactBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
            ContactBulkMap.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
            log.info("No contacts found.");
        }
        } catch (Exception e) {
            ContactBulkMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            ContactBulkMap.put(DataConstant.MESSAGE, DataConstant.SERVER_ISSUE);
            log.error("An error occurred while fetching contacts: " + e.getMessage());
        }
        return ContactBulkMap;
	}


    public Map<String, Object> getAllDeleteContact(GetAllDeleteContact getAllContact) {
        Map<String, Object> getDeleteContactMap = new HashMap<>();
        try {

            if(getAllContact.getPageIndex() ==null &&getAllContact. getPageSize() ==null) {
                getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                getDeleteContactMap.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return getDeleteContactMap;
            }
            List<ContactResponse> contactResponseList = new ArrayList<>();
            ContactResponsePage contactResponsePage = new ContactResponsePage();
            List<ContactDetail> contactDetails = new ArrayList<>();
            Pageable pageable = null;
            Page<ContactDetail> page = null;
            Short status = 2; // for soft delete status  (contact)
            if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0) {
                if (getAllContact.getPageIndex() == 0 && getAllContact.getPageSize() == 0 ) {
                    contactDetails = contactRepository.findAllByStatus(status);
                }
                if (contactDetails.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", contactDetails);
                    for (ContactDetail detail : contactDetails) {
                        ContactResponse contactResponse = new ContactResponse();
                        BeanUtils.copyProperties(detail, contactResponse);
                        contactResponseList.add(contactResponse);
                    }
                    contactResponsePage.setContactResponseList(contactResponseList);
                    getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    getDeleteContactMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                    log.info("Record found! status - {}", contactResponsePage);
                    return getDeleteContactMap;
                } else {
                    getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    getDeleteContactMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                    log.info("Record not found! status - {}");
                    return getDeleteContactMap;
                }
            } else if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != null) {
                if (getAllContact.getPageSize() >= 1) {
                    pageable = PageRequest.of(getAllContact.getPageIndex(), getAllContact.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllContact.getPageIndex() != null && getAllContact.getPageSize() != 0 ) {
                        page = contactRepository.findAllByStatus(status,pageable);

                    }

                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", contactDetails);
                        contactDetails = page.getContent();
                        int index=0;
                        for (ContactDetail contactDetail : contactDetails) {
                            ContactResponse pharmacistResponsepayload = new ContactResponse();
                            BeanUtils.copyProperties(contactDetail, pharmacistResponsepayload);
                            //for frontEnd team pagination
                            if(getAllContact.getPageIndex() == 0) {
                                pharmacistResponsepayload.setSerialNo(index+1);
                                index++;
                            }else {
                                pharmacistResponsepayload.setSerialNo((getAllContact.getPageSize()*getAllContact.getPageIndex())+(index+1));
                                index++;
                            }
                            contactResponseList.add(pharmacistResponsepayload);
                        }
                        contactResponsePage.setContactResponseList(contactResponseList);
                        contactResponsePage.setPageIndex(page.getNumber());
                        contactResponsePage.setPageSize(page.getSize());
                        contactResponsePage.setTotalElement(page.getTotalElements());
                        contactResponsePage.setTotalPages(page.getTotalPages());
                        contactResponsePage.setIsFirstPage(page.isFirst());
                        contactResponsePage.setIsLastPage(page.isLast());

                        getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        getDeleteContactMap.put(DataConstant.RESPONSE_BODY, contactResponsePage);
                        log.info("Record found! status - {}", contactResponsePage);
                    } else {
                        getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        getDeleteContactMap.put(DataConstant.RESPONSE_BODY, null);
                        log.info("Record not found! status - {}");
                        return getDeleteContactMap;
                    }
                } else {
                    getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllContact);
                    return getDeleteContactMap;
                }
            }
        } catch (DataAccessResourceFailureException e) {
            getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.DB_CONNECTION_ERROR);
            getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.INVALID_CREDENTIAL);
            log.error("Exception : " + e.getMessage());
            return getDeleteContactMap;
        } catch (Exception e) {
            getDeleteContactMap.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            getDeleteContactMap.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("Exception : " + e.getMessage());
            return getDeleteContactMap;
        }
        return getDeleteContactMap;
    }

}
