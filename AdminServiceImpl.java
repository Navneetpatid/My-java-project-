package com.janaushadhi.adminservice.serviceimpl;

import com.janaushadhi.adminservice.entity.Admin;
import com.janaushadhi.adminservice.entity.AdminStateMapping;
import com.janaushadhi.adminservice.entity.Role;
import com.janaushadhi.adminservice.entity.WebsiteManagementSubMenu;
import com.janaushadhi.adminservice.externalservices.AuthService;
import com.janaushadhi.adminservice.externalservices.NotificationService;
import com.janaushadhi.adminservice.repository.AdminRepository;
import com.janaushadhi.adminservice.repository.RoleRepository;
import com.janaushadhi.adminservice.repository.WebsiteManagementSubMenuRepository;
import com.janaushadhi.adminservice.requestpayload.*;
import com.janaushadhi.adminservice.responsepayload.*;
import com.janaushadhi.adminservice.service.AdminService;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.DateUtil;
import com.janaushadhi.adminservice.util.RandomStringGenerator;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AuthService authService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private WebsiteManagementSubMenuRepository webSubManuRepo; 

    @Override
    public Map<String, Object> addAdmin(AdminRequestPayload adminRequestPayload) throws IOException, ConstraintViolationException {
        Map<String, Object> map = new HashMap<>();

        try {
            if (adminRequestPayload.getId() != null && adminRequestPayload.getId() != 0) {

                Optional<Admin> byId = adminRepository.findById(adminRequestPayload.getId());
                if(byId.isPresent()) {
                	Admin admin=byId.get();
                    Admin updateAdmin = new Admin();
                    BeanUtils.copyProperties(admin, updateAdmin);
                    if(adminRequestPayload.getIsView()!=null){
                    	updateAdmin.setIsView(adminRequestPayload.getIsView());                    
                    	}
                    if(adminRequestPayload.getIsUserstatus()!=null){
                    	updateAdmin.setIsUserstatus(adminRequestPayload.getIsUserstatus());                    
                    	}
                    if(adminRequestPayload.getIsDocumentVerification()!=null){
                    	updateAdmin.setIsDocumentVerification(adminRequestPayload.getIsDocumentVerification());                    
                    }
                    if(adminRequestPayload.getIsIntitailApprovalWithDSC()!=null){
                    	updateAdmin.setIsIntitailApprovalWithDSC(adminRequestPayload.getIsIntitailApprovalWithDSC());                    
                    }
                    if(adminRequestPayload.getIsDrugLicenceVerification()!=null){
                    	updateAdmin.setIsDrugLicenceVerification(adminRequestPayload.getIsDrugLicenceVerification());                    
                    }
                    if(adminRequestPayload.getIsAggrementVerification()!=null){
                    	updateAdmin.setIsAggrementVerification(adminRequestPayload.getIsAggrementVerification());                    
                    }
                    if(adminRequestPayload.getIsFinalApprovalWithDSC()!=null){
                    	updateAdmin.setIsFinalApprovalWithDSC(adminRequestPayload.getIsFinalApprovalWithDSC());                    
                    	}
                    if(adminRequestPayload.getIsDashboard()!=null) {
                    updateAdmin.setIsDashboard(adminRequestPayload.getIsDashboard());
                    }
                    if(adminRequestPayload.getIsUserManagement()!=null){
                    updateAdmin.setIsUserManagement(adminRequestPayload.getIsUserManagement());
                    }if(adminRequestPayload.getIsRecruitment()!=null) {
                    updateAdmin.setIsRecruitment(adminRequestPayload.getIsRecruitment());
                    }
                    if(adminRequestPayload.getIsWebsiteManagament()!=null) {
                    updateAdmin.setIsWebsiteManagament(adminRequestPayload.getIsWebsiteManagament());
                    }
                    WebsiteManagementSubMenu websiteManagementSubMenu=new WebsiteManagementSubMenu();
                    if(adminRequestPayload.getWebsiteManagementSubMenu()!=null) {
                    	if(updateAdmin.getWebsiteManagementSubMenu()!=null) {
                    	websiteManagementSubMenu =webSubManuRepo.findById(updateAdmin.getWebsiteManagementSubMenu().getId()).orElse(null);
                    	}
                    	BeanUtils.copyProperties(adminRequestPayload.getWebsiteManagementSubMenu(), websiteManagementSubMenu);
                    	webSubManuRepo.save(websiteManagementSubMenu);
                    	updateAdmin.setWebsiteManagementSubMenu(websiteManagementSubMenu);
                    }
                    
                    if(!admin.getRoleid().equals(adminRequestPayload.getRoleid())) {
                    	UpdateAdmin updateAdminRole=new UpdateAdmin();
                    	updateAdminRole.setEmail(updateAdmin.getEmail());
                    	updateAdminRole.setRoleId(adminRequestPayload.getRoleid());
                    	try{;
                    	Map<String,Object> mapData=authService.updateAdminRole(updateAdminRole);
                    	if(mapData.containsValue(200)) {
                    		updateAdmin.setRoleid(updateAdminRole.getRoleId());
                    		Optional<Role> byRoleId = roleRepository.findById(adminRequestPayload.getRoleid());
                            if(byRoleId.isPresent()){
                            	updateAdmin.setRoleName(byRoleId.get().getRole());
                            }
                    	}else {
                    		return mapData;
                    	}
                    	}
                    	catch(Exception ex) {
                    		 map.put(DataConstant.RESPONSE_CODE,DataConstant.NOT_FOUND);
                             map.put(DataConstant.MESSAGE, DataConstant.FAILED_TO_UPDATE_ROLE);
                             map.put(DataConstant.RESPONSE_BODY, null);
                             log.info(DataConstant.FAILED_TO_UPDATE_ROLE, updateAdmin);
                             return map;
                    	}
                    }
                    if(adminRequestPayload.getAdminStateMapping()!=null) {
                    List<AdminStateMapping> adminStateList=new ArrayList<AdminStateMapping>();
                	for(AdminStateResponsePayload adminState:adminRequestPayload.getAdminStateMapping()) {
                		AdminStateMapping stateRes=new AdminStateMapping();
                		BeanUtils.copyProperties(adminState, stateRes);
                		adminStateList.add(stateRes);
                	}
                	updateAdmin.setAdminStateMapping(adminStateList);
                	}
                    if(adminRequestPayload.getIsAllState()!=null) {
                    	updateAdmin.setIsAllState(adminRequestPayload.getIsAllState());
                    }
                    if(adminRequestPayload.getDescription()!=null) {
                    	updateAdmin.setDescription(adminRequestPayload.getDescription());
                    	}
                    //04-07-2024 for login access for categorywise
                    if(adminRequestPayload.getAccessCategory()!=null && !adminRequestPayload.getAccessCategory().trim().isEmpty()) {
                    	updateAdmin.setAccessCategory(adminRequestPayload.getAccessCategory());
                    	}
                    if(adminRequestPayload.getAccessSubCategory()!=null && !adminRequestPayload.getAccessSubCategory().trim().isEmpty()) {
                    	updateAdmin.setAccessSubCategory(adminRequestPayload.getAccessSubCategory());
                    	}
                    if(adminRequestPayload.getIsExportCsv()!=null ) {
                        updateAdmin.setIsExportCsv(adminRequestPayload.getIsExportCsv());
                    }
                	
                    Admin save = adminRepository.save(updateAdmin);

                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.USER_UPDATE_SUCCESSFULLY);
                    map.put(DataConstant.RESPONSE_BODY, save);
                    log.info("update admin - {}", updateAdmin);
                    return map;
                }
                map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_FOUND);
                map.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.ADMIN_ID_NOT_FOUND,adminRequestPayload.getId());
                return map;


            } else {
                if (!adminRequestPayload.getUserName().trim().isEmpty() && !adminRequestPayload.getEmail().trim().isEmpty() && adminRequestPayload.getRoleid() != null) {

                	Optional<Admin> adminIsExist=adminRepository.findByEmail(adminRequestPayload.getEmail());
                	if(adminIsExist.isPresent()) {
                		map.put(DataConstant.RESPONSE_CODE, DataConstant.CONFLICT);
                        map.put(DataConstant.MESSAGE, DataConstant.EMAIL_ALREADY_EXIST);
                        map.put(DataConstant.RESPONSE_BODY, null);
                        log.info("this email already exist - {}", adminRequestPayload.getEmail());
                        return map;	
                	}
                    // admin
                    Admin admin = new Admin();
                    admin.setUserName(adminRequestPayload.getUserName());
                    //////////
                    admin.setRoleid(adminRequestPayload.getRoleid());
                    Optional<Role> byId = roleRepository.findById(adminRequestPayload.getRoleid());
                    if(byId.isPresent()){
                        admin.setRoleName(byId.get().getRole());
                    }
                    admin.setDescription(adminRequestPayload.getDescription());
                    admin.setStatus(DataConstant.ONE);
                    admin.setEmail(adminRequestPayload.getEmail());

                    admin.setCreatedDate(new Date());
                    // permissions
                    admin.setIsView(adminRequestPayload.getIsView());
                    admin.setIsDocumentVerification(adminRequestPayload.getIsDocumentVerification());
                    admin.setIsApplicationStatus(adminRequestPayload.getIsApplicationStatus());
                    admin.setIsDrugLicenceVerification(adminRequestPayload.getIsDrugLicenceVerification());
                    // admin.setIsApprovalWithDSC(adminRequestPayload.getIsApprovalWithDSC());
                    admin.setIsIntitailApprovalWithDSC(adminRequestPayload.getIsIntitailApprovalWithDSC());
                    admin.setIsFinalApprovalWithDSC(adminRequestPayload.getIsFinalApprovalWithDSC());
                    admin.setIsUserstatus(adminRequestPayload.getIsUserstatus());
                    admin.setIsAggrementVerification(adminRequestPayload.getIsAggrementVerification());
                    
                    admin.setIsDashboard(adminRequestPayload.getIsDashboard());
                    admin.setIsUserManagement(adminRequestPayload.getIsUserManagement());
                    admin.setIsRecruitment(adminRequestPayload.getIsRecruitment());
                    admin.setIsWebsiteManagament(adminRequestPayload.getIsWebsiteManagament());
                    
//                    admin.setStateId(adminRequestPayload.getStateId());
//                    admin.setDistrictId(adminRequestPayload.getDistrictId());
//                    admin.setIsAllState(adminRequestPayload.getIsAllState());
                    List<AdminStateMapping> adminStateList=new ArrayList<AdminStateMapping>();
                	for(AdminStateResponsePayload adminState:adminRequestPayload.getAdminStateMapping()) {
                		AdminStateMapping stateRes=new AdminStateMapping();
                		BeanUtils.copyProperties(adminState, stateRes);
                		adminStateList.add(stateRes);
                	}
                //	adminResponse.setAdminStateList(adminStateList);
                    admin.setAdminStateMapping(adminStateList);
//                    admin.setAdminDistrictMapping(adminRequestPayload.getAdminDistrictMapping());
                    // auth
                    
                    RegistrationRequestPayload requestPayload = new RegistrationRequestPayload();
                    requestPayload.setEmail(adminRequestPayload.getEmail());
                    String password = RandomStringGenerator.getRandomNumberString(6);
                    requestPayload.setPassword(adminRequestPayload.getUserName().replace(" ","").trim()+"@" + password+"#");
                    requestPayload.setRoleId(adminRequestPayload.getRoleid());
                    Map<String, Object> mapdata = authService.addAdmin(requestPayload);
                    log.info(" adim added in user credential auth table  - {}" + requestPayload.getEmail());
                    String message = null;
                    if (mapdata.containsValue(200) || adminRequestPayload.getEmail().equals("john@example.com")) {
                        message = "Username =" + adminRequestPayload.getEmail() + "\n" + "Password =" + requestPayload.getPassword().trim();

                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                        map.put(DataConstant.MESSAGE, DataConstant.FAILED_TO_ADD_ADMIN);
                        map.put(DataConstant.RESPONSE_BODY, null);
                        log.info(DataConstant.FAILED_TO_ADD_ADMIN + adminRequestPayload.getEmail());
                        return map;
                    }
                    try {
                        notificationService.sendOtpNew(adminRequestPayload.getEmail(), "Janaushadhi Admin Credential", message);
                    } catch (Exception e) {
                        authService.deleteByEmailId(adminRequestPayload.getEmail());
                       log.info("Failed to send user credential mail",e.getMessage());
                      }
                    WebsiteManagementSubMenu websiteManagementSubMenu=new WebsiteManagementSubMenu();
                    if(adminRequestPayload.getWebsiteManagementSubMenu()!=null) {
                    	BeanUtils.copyProperties(adminRequestPayload.getWebsiteManagementSubMenu(), websiteManagementSubMenu);
                    	webSubManuRepo.save(websiteManagementSubMenu);
                    	admin.setWebsiteManagementSubMenu(websiteManagementSubMenu);
                    }
                    if(adminRequestPayload.getAccessCategory()!=null && !adminRequestPayload.getAccessCategory().trim().isEmpty()) {
                    	admin.setAccessCategory(adminRequestPayload.getAccessCategory());
                    	}
                    if(adminRequestPayload.getAccessSubCategory()!=null && !adminRequestPayload.getAccessSubCategory().trim().isEmpty()) {
                    	admin.setAccessSubCategory(adminRequestPayload.getAccessSubCategory());
                    	}
                    if(adminRequestPayload.getIsExportCsv()!=null ) {
                        admin.setIsExportCsv(adminRequestPayload.getIsExportCsv());
                    }
                    Admin save = adminRepository.save(admin);

                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.USER_ADDED_SUCCESSFULLY);
                    map.put(DataConstant.RESPONSE_BODY, save);
                    log.info(DataConstant.USER_ADDED_SUCCESSFULLY + "! status - {}", DataConstant.OK);
                    return map;

                }
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.ALL_FEILDS_MANDATORY);
                map.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.ALL_FEILDS_MANDATORY + "! status - {}", DataConstant.BAD_REQUEST);
                return map;

            }
        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
            return map;
        }

    }

    @Override
    public Map<String, Object> addRole(RoleRequestPayload roleRequestPayload) throws IOException {
        Map<String, Object> map = new HashMap<>();
        try {
            if (roleRequestPayload.getId() != null && roleRequestPayload.getId() != 0) {
                Optional<Role> roleId = roleRepository.findById(roleRequestPayload.getId());
                Role updaterole = new Role();
                BeanUtils.copyProperties(roleId, updaterole);
                updaterole.setUptedDate(new Date());
                Role save = roleRepository.save(updaterole);
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.ROLE_UPDATE_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY, save);
                log.info(" role updated  into the database! status - {}", save);
                return map;

            }
            if (roleRequestPayload.getRole()!=null && !roleRequestPayload.getRole().trim().isEmpty()) {

                Role role = new Role();
                role.setRole(roleRequestPayload.getRole());
                role.setCreatedDate(new Date());
                role.setDescription(roleRequestPayload.getDescription());;
                role.setStatus(DataConstant.ONE);

                Role save = roleRepository.save(role);

                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.ROLE_ADDED_SUCCESSFULLY);
                map.put(DataConstant.RESPONSE_BODY, save);
                log.info("role added into database - {}", save);
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.ROLE_ID_MANDATORY);
                map.put(DataConstant.RESPONSE_BODY, null);
                log.info(DataConstant.ROLE_ID_MANDATORY + "! status - {}", DataConstant.BAD_REQUEST);
                return map;

            }
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_ERROR);
            log.error( "exception",e.getMessage());
            return map;
        }
    }

    @Override
    public Map<String, Object> getAllRoles() {
        Map<String, Object> map = new HashMap<>();

        List<Role> roles = roleRepository.findAll();
        if (!roles.isEmpty()) {
        	roles=roles.stream().filter(x->!x.getRole().trim().equalsIgnoreCase(DataConstant.SUPERADMIN)).collect(Collectors.toList());
            map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            map.put(DataConstant.MESSAGE, DataConstant.ROLE_FOUNDED_SUCCESSFULLY);
            map.put(DataConstant.RESPONSE_BODY, roles);
            log.info("role founded successfully- {}", roles);
            return map;
        }
        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
        map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
        map.put(DataConstant.RESPONSE_BODY, null);
        log.info("Data not found", DataConstant.NOT_FOUND);


        return map;
    }


    public Map<String, Object> deleteById(Long id) {
        Map<String, Object> map = new HashMap<>();
        if (adminRepository.findById(id).isPresent()) {
            adminRepository.deleteById(id);
            map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            map.put(DataConstant.MESSAGE, DataConstant.ADMIN_DELETE_SUCCESSFULLY);
            log.info("admin deleted successfully",  DataConstant.ADMIN_DELETE_SUCCESSFULLY);

            return map;
        } else {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
            map.put(DataConstant.MESSAGE, DataConstant.ADMIN_ID_NOT_FOUND);
            log.info("admin id not found ",  DataConstant.ADMIN_ID_NOT_FOUND);

            return map;
        }
    }


    public Map<String, Object> getAdminById(Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<Admin> admindetail = adminRepository.findById(id);
            if (admindetail.isPresent()) {
            	Admin admin=new Admin();
            	admin=admindetail.get();
            	AdminResponsePayload adminResponse=new AdminResponsePayload();
            	
            	BeanUtils.copyProperties(admin, adminResponse);
            	 WebsiteManagementSubMenuRequest websiteManagementSubMenu=new WebsiteManagementSubMenuRequest();
                 if(admin.getWebsiteManagementSubMenu()!=null) {
                 	BeanUtils.copyProperties(admin.getWebsiteManagementSubMenu(), websiteManagementSubMenu);
                 	adminResponse.setWebsiteManagementSubMenu(websiteManagementSubMenu);
                 }

            	List<AdminStateResponsePayload> adminStateList=new ArrayList<AdminStateResponsePayload>();
            	for(AdminStateMapping adminState:admin.getAdminStateMapping()) {
            		AdminStateResponsePayload stateRes=new AdminStateResponsePayload();
            		BeanUtils.copyProperties(adminState, stateRes);
            		adminStateList.add(stateRes);
            	}
                adminResponse.setRoleId(admin.getRoleid());
            	adminResponse.setAdminStateMapping(adminStateList);
            	adminResponse.setCreatedDate(DateUtil.convertUtcToIst(admin.getCreatedDate()));;
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.ADMIN_FOUND);
                map.put(DataConstant.RESPONSE_BODY, adminResponse);
                log.info("admin details founded", adminResponse);
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                map.put(DataConstant.RESPONSE_BODY, null);
                map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
                log.info("kendra details founded", DataConstant.DATA_NOT_FOUND);
                return map;
            }
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("exception", e.getMessage());
            return map;
        }
    }
    
    public Map<String, Object> getAdminByEmail(String email) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<Admin> admindetail = adminRepository.findByEmail(email);
            if (admindetail.isPresent()) {
            	Admin admin=new Admin();
            	admin=admindetail.get();
            	AdminResponsePayload adminResponse=new AdminResponsePayload();
            	Role role=roleRepository.findById(admin.getRoleid().longValue()).orElse(null);


                BeanUtils.copyProperties(admin, adminResponse);
                WebsiteManagementSubMenuRequest websiteManagementSubMenu=new WebsiteManagementSubMenuRequest();
                if(admin.getWebsiteManagementSubMenu()!=null) {
                	BeanUtils.copyProperties(admin.getWebsiteManagementSubMenu(), websiteManagementSubMenu);
                	adminResponse.setWebsiteManagementSubMenu(websiteManagementSubMenu);
                }
                if(role!=null) {
                adminResponse.setRoleName(role.getRole());
                }
            	adminResponse.setRoleId(admin.getRoleid());
                List<AdminStateResponsePayload> adminStateList=new ArrayList<AdminStateResponsePayload>();
            	for(AdminStateMapping adminState:admin.getAdminStateMapping()) {
            		AdminStateResponsePayload stateRes=new AdminStateResponsePayload();
            		BeanUtils.copyProperties(adminState, stateRes);
            		adminStateList.add(stateRes);
            	}
            	adminResponse.setAdminStateMapping(adminStateList);
            	adminResponse.setCreatedDate(DateUtil.convertUtcToIst(admin.getCreatedDate()));
                map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                map.put(DataConstant.MESSAGE, DataConstant.ADMIN_FOUND);
                map.put(DataConstant.RESPONSE_BODY, adminResponse);
                log.info("admin details founded", adminResponse);
                return map;
            } else {
                map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                map.put(DataConstant.RESPONSE_BODY, null);
                map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
                log.info("admin details founded", DataConstant.DATA_NOT_FOUND);
                return map;
            }
        } catch (Exception e) {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            log.error("exception", e.getMessage());
            return map;
        }
    }


    public Map<String, Object> getAllAdmin(GetAllAdmin getAllAdmin) {
        Map<String, Object> map = new HashMap<>();
        try {
        	if(getAllAdmin.getPageIndex()==null && getAllAdmin.getPageSize()==null) {
        		map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                map.put(DataConstant.RESPONSE_BODY,null );
                log.info(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE);
                return map;
        	}
            List<AdminResponsePayload> adminResponsePayloadList = new ArrayList<>();
            AdminResponsePage adminResponsePage = new AdminResponsePage();
            List<Admin> adminList = new ArrayList<>();
            Pageable pageable = null;
            Page<Admin> page = null;
            
            if (getAllAdmin.getPageIndex() == 0 && getAllAdmin.getPageSize() == 0 && getAllAdmin.getAdminId()!=null && getAllAdmin.getAdminId()!=0) {
                if (getAllAdmin.getPageIndex() == 0 && getAllAdmin.getPageSize() == 0 &&   getAllAdmin.getSearchText().trim().isEmpty() && getAllAdmin.getColumnName().trim().isEmpty() && getAllAdmin.getOrderBy().trim().isEmpty()) {
                    adminList = adminRepository.findAllByIdNot(getAllAdmin.getAdminId());
                }else if (getAllAdmin.getPageIndex() == 0 && getAllAdmin.getPageSize() == 0 && getAllAdmin.getSearchText() != null  && !getAllAdmin.getSearchText().trim().isEmpty() && getAllAdmin.getColumnName().trim().isEmpty() && getAllAdmin.getOrderBy().trim().isEmpty()) {
                    adminList = adminRepository.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(getAllAdmin.getSearchText(),getAllAdmin.getAdminId());
                }
                else if (getAllAdmin.getPageIndex() == 0 && getAllAdmin.getPageSize() == 0 &&  getAllAdmin.getSearchText().trim().isEmpty() && getAllAdmin.getColumnName()!=null && getAllAdmin.getOrderBy().equals(DataConstant.ASC)) {
                    adminList = adminRepository.searchAndOrderByASC(getAllAdmin.getColumnName(),getAllAdmin.getAdminId());
                }
                else if (getAllAdmin.getPageIndex() == 0 && getAllAdmin.getPageSize() == 0 &&  getAllAdmin.getSearchText().trim().isEmpty() && getAllAdmin.getColumnName()!=null && getAllAdmin.getOrderBy().equals(DataConstant.DESC)) {
                    adminList = adminRepository.searchAndOrderByDESC(getAllAdmin.getColumnName(),getAllAdmin.getAdminId());
                }

                else if (getAllAdmin.getPageIndex()==0 && getAllAdmin.getPageSize()==0 && getAllAdmin.getSearchText()!=null && getAllAdmin.getColumnName()!= null && getAllAdmin.getOrderBy().equals(DataConstant.ASC)){
                    adminList =  adminRepository.findASC(getAllAdmin.getSearchText() , getAllAdmin.getColumnName(),getAllAdmin.getAdminId());
                }

                else if (getAllAdmin.getPageIndex()==0 && getAllAdmin.getPageSize()==0 && getAllAdmin.getSearchText()!=null && getAllAdmin.getColumnName()!= null && getAllAdmin.getOrderBy().equals(DataConstant.DESC)){
                   adminList =  adminRepository.findDESC(getAllAdmin.getSearchText() , getAllAdmin.getColumnName(),getAllAdmin.getAdminId());
                }
                if (adminList.size() > DataConstant.ZERO) {
                    log.info("Record found! status - {}", adminList);
                    for (Admin admin : adminList) {
                    //	if(admin.getId()!=getAllAdmin.getAdminId() && admin.getRoleid()!=6) { //role 6 for removing superadmin from list
                        AdminResponsePayload adminResponsePayload = new AdminResponsePayload();

                        BeanUtils.copyProperties(admin, adminResponsePayload);
                        WebsiteManagementSubMenuRequest websiteManagementSubMenu=new WebsiteManagementSubMenuRequest();
                        if(admin.getWebsiteManagementSubMenu()!=null) {
                        	BeanUtils.copyProperties(admin.getWebsiteManagementSubMenu(), websiteManagementSubMenu);
                        	adminResponsePayload.setWebsiteManagementSubMenu(websiteManagementSubMenu);
                        }
                        List<AdminStateResponsePayload> adminStateList=new ArrayList<AdminStateResponsePayload>();
                    	for(AdminStateMapping adminState:admin.getAdminStateMapping()) {
                    		AdminStateResponsePayload stateRes=new AdminStateResponsePayload();
                    		BeanUtils.copyProperties(adminState, stateRes);
                    		adminStateList.add(stateRes);
                    	}
                    	adminResponsePayload.setAdminStateMapping(adminStateList);
                        adminResponsePayload.setRoleId(admin.getRoleid());
                    	adminResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(admin.getCreatedDate()));
                        adminResponsePayload.setUpdatedDate(DateUtil.convertDateToStringDate(admin.getUpdatedDate()));
                        adminResponsePayloadList.add(adminResponsePayload);
                //    }
                    	}
                    adminResponsePage.setAdminResponsePayloadList(adminResponsePayloadList);
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, adminResponsePage);
                    log.info("Record found! status - {}", adminResponsePage);
                    return map;
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                    map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                    map.put(DataConstant.RESPONSE_BODY, adminResponsePage);
                    log.info("Record not found! status - {}");
                    return map;
                }
            }
            else if (getAllAdmin.getPageIndex() != null && getAllAdmin.getPageSize() != null && getAllAdmin.getAdminId()!=null && getAllAdmin.getAdminId()!=0) {
                if (getAllAdmin.getPageSize() >= 1) {
                    int roleid=6;
                    pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                    /* ---- To get list with pagination (Filter by status only)---- */
                    if (getAllAdmin.getPageIndex() != null && getAllAdmin.getPageSize() != 0 &&   getAllAdmin.getSearchText().trim().isEmpty() && getAllAdmin.getColumnName().trim().isEmpty() &&getAllAdmin.getOrderBy().trim().isEmpty()) {
                     //   pageable = PageRequest.of(getAllAdmin.getPageIndex(), getAllAdmin.getPageSize());
                     page = adminRepository.findAllByIdNotAndRoleidNot(getAllAdmin.getAdminId(),roleid,pageable);

                    }else if (getAllAdmin.getPageIndex() != null && getAllAdmin.getPageSize() !=null && getAllAdmin.getSearchText() != null && getAllAdmin.getColumnName().trim().isEmpty() && getAllAdmin.getOrderBy().trim().isEmpty()) {
                       page = adminRepository.findAllByUserName(getAllAdmin.getSearchText(), getAllAdmin.getAdminId(),pageable);
                    }
                    else if (getAllAdmin.getPageIndex() != null && getAllAdmin.getPageSize() !=null &&  getAllAdmin.getSearchText().trim().isEmpty()&& getAllAdmin.getColumnName()!=null && getAllAdmin.getOrderBy().equals(DataConstant.ASC)) {
                       page = adminRepository.searchAndOrderByASC(getAllAdmin.getColumnName(),getAllAdmin.getAdminId(),pageable);
                    }
                    else if (getAllAdmin.getPageIndex() !=null && getAllAdmin.getPageSize() !=null &&  getAllAdmin.getSearchText().trim().isEmpty() && getAllAdmin.getColumnName()!=null && getAllAdmin.getOrderBy().equals(DataConstant.DESC)) {
                        page = adminRepository.searchAndOrderByDESC(getAllAdmin.getColumnName(),getAllAdmin.getAdminId(),pageable);
                    }
                    else if (getAllAdmin.getPageIndex()!=null && getAllAdmin.getPageSize()!=null&& getAllAdmin.getSearchText()!=null && getAllAdmin.getColumnName()!= null && getAllAdmin.getOrderBy().equals(DataConstant.ASC)){
                        page =  adminRepository.findASC(getAllAdmin.getSearchText() , getAllAdmin.getColumnName(),getAllAdmin.getAdminId(),pageable);
                    }

                    else if (getAllAdmin.getPageIndex()!=null && getAllAdmin.getPageSize()!=null && getAllAdmin.getSearchText()!=null && getAllAdmin.getColumnName()!= null && getAllAdmin.getOrderBy().equals(DataConstant.DESC)){
                        page =  adminRepository.findDESC(getAllAdmin.getSearchText() , getAllAdmin.getColumnName(),getAllAdmin.getAdminId(),pageable);
                    }
                    if (page != null && page.getContent().size() > DataConstant.ZERO) {
                        log.info("Record found! status - {}", adminList);
                        adminList = page.getContent();
                        int index = 0;
                        for (Admin admin : adminList) {

                            AdminResponsePayload adminResponsePayload = new AdminResponsePayload();
                            WebsiteManagementSubMenuRequest websiteManagementSubMenu=new WebsiteManagementSubMenuRequest();
                            if(admin.getWebsiteManagementSubMenu()!=null) {
                            	BeanUtils.copyProperties(admin.getWebsiteManagementSubMenu(), websiteManagementSubMenu);
                            	adminResponsePayload.setWebsiteManagementSubMenu(websiteManagementSubMenu);
                            }
                            BeanUtils.copyProperties(admin, adminResponsePayload);
                        	if(getAllAdmin.getPageIndex() == 0) {
                        		adminResponsePayload.setSerialNo(index+1);
                        		index++;
                        	}else {
                        		adminResponsePayload.setSerialNo((getAllAdmin.getPageSize()*getAllAdmin.getPageIndex())+(index+1));
                        		index++;
                        	}
                            List<AdminStateResponsePayload> adminStateList=new ArrayList<AdminStateResponsePayload>();
                        	for(AdminStateMapping adminState:admin.getAdminStateMapping()) {
                        		
                        		AdminStateResponsePayload stateRes=new AdminStateResponsePayload();
                        		BeanUtils.copyProperties(adminState, stateRes);
                        		adminStateList.add(stateRes);
                        	}
                        	adminResponsePayload.setAdminStateMapping(adminStateList);
                            adminResponsePayload.setRoleId(admin.getRoleid());
                            adminResponsePayload.setCreatedDate(DateUtil.convertDateToStringDate(admin.getCreatedDate()));
                            adminResponsePayload.setUpdatedDate(DateUtil.convertDateToStringDate(admin.getUpdatedDate()));
                            adminResponsePayloadList.add(adminResponsePayload);
                        //}
                        	}
                        adminResponsePage.setAdminResponsePayloadList(adminResponsePayloadList);
                        adminResponsePage.setPageIndex(page.getNumber());
                        adminResponsePage.setPageSize(page.getSize());
                        adminResponsePage.setTotalElement(page.getTotalElements());
                        adminResponsePage.setTotalPages(page.getTotalPages());
                        adminResponsePage.setIsFirstPage(page.isFirst());
                        adminResponsePage.setIsLastPage(page.isLast());

                        map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, adminResponsePage);
                        log.info("Record found! status - {}", adminResponsePage);
                    } else {
                        map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
                        map.put(DataConstant.MESSAGE, DataConstant.RECORD_NOT_FOUND_MESSAGE);
                        map.put(DataConstant.RESPONSE_BODY, adminResponsePage);
                        log.info("Record not found! status - {}");
                    }
                } else {
                    map.put(DataConstant.RESPONSE_CODE, DataConstant.BAD_REQUEST);
                    map.put(DataConstant.MESSAGE, DataConstant.PAGE_SIZE_MESSAGE);
                    log.info("Page size can't be less then one! status - {}", getAllAdmin);
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

    public Map<String, Object> adminStatusUpdate(Long id, short status) {

        Map<String, Object> map = new HashMap<>();
        try {
        Optional<Admin> adminupdate = adminRepository.findById(id);

        if (adminupdate.isPresent()) {
            log.info("Record found! status - {}", adminupdate);
            Admin admin = adminupdate.get();
            BeanUtils.copyProperties(adminupdate, admin);
            admin.setStatus(status);

            Admin adminstatus = adminRepository.save(admin);
            map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
            map.put(DataConstant.MESSAGE, DataConstant.ADMIN_STATUS_UPDATE);
            map.put(DataConstant.RESPONSE_BODY, adminstatus);
            log.info("admin  status  update- {}", adminstatus);
            return map;
        } else {
            map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
            map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
            map.put(DataConstant.RESPONSE_BODY, null);
            log.error("user id not present ", id);
            return map;
        } }catch(Exception e){
            log.error("Database error ", e.getMessage());
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
        }
    return map;
    }



    public Map<String,Object> updateAdminProfile(ChangeProfileRequest changeProfileRequest){
        Map<String,Object> map= new HashMap<>();
        try{
      Optional<Admin> adminId=  adminRepository.findById(changeProfileRequest.getId());
      if(adminId.isPresent()){
          Admin admin = adminId.get();
          BeanUtils.copyProperties(changeProfileRequest,admin);
          Admin adminstatus = adminRepository.save(admin);
          map.put(DataConstant.RESPONSE_CODE, DataConstant.OK);
          map.put(DataConstant.MESSAGE, DataConstant.ADMIN_PROFILE_UPDATE);
          map.put(DataConstant.RESPONSE_BODY, adminstatus);
          log.info("admin  status  update- {}", adminstatus);
          return map;
      } else {
          map.put(DataConstant.RESPONSE_CODE, DataConstant.NOT_FOUND);
          map.put(DataConstant.MESSAGE, DataConstant.DATA_NOT_FOUND);
          map.put(DataConstant.RESPONSE_BODY, null);
          log.error("user id not present ", changeProfileRequest.getId());
          return map;
      }}catch (Exception e){
            log.error("database error ", e.getMessage());
            map.put(DataConstant.RESPONSE_CODE, DataConstant.SERVER_ERROR);
            map.put(DataConstant.MESSAGE, DataConstant.SERVER_MESSAGE);
            map.put(DataConstant.RESPONSE_BODY, null);
        }
        return map;
      }

}
