package com.janaushadhi.adminservice.serviceimpl;

import com.janaushadhi.adminservice.entity.Admin;
import com.janaushadhi.adminservice.entity.AdminStateMapping;
import com.janaushadhi.adminservice.entity.Role;
import com.janaushadhi.adminservice.externalservices.AuthService;
import com.janaushadhi.adminservice.externalservices.KendraService;
import com.janaushadhi.adminservice.externalservices.NotificationService;
import com.janaushadhi.adminservice.repository.AddKendraRepository;
import com.janaushadhi.adminservice.repository.AdminRepository;
import com.janaushadhi.adminservice.repository.RoleRepository;
import com.janaushadhi.adminservice.repository.WebsiteManagementSubMenuRepository;
import com.janaushadhi.adminservice.requestpayload.*;
import com.janaushadhi.adminservice.responsepayload.AdminResponsePage;
import com.janaushadhi.adminservice.responsepayload.AdminResponsePayload;
import com.janaushadhi.adminservice.responsepayload.AdminStateResponsePayload;
import com.janaushadhi.adminservice.util.DataConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {


    private AuthService authService;

    private RoleRepository roleRepository;


    private AdminRepository adminRepository;


    private NotificationService notificationService;
    
    private WebsiteManagementSubMenuRepository webSubManuRepository; 
    @InjectMocks
    AdminServiceImpl adminService;
    @BeforeEach
    void serviceSetup() {
        authService = mock(AuthService.class);
        roleRepository = mock(RoleRepository.class);
        adminRepository = mock(AdminRepository.class);
        notificationService = mock(NotificationService.class);
        webSubManuRepository =mock(WebsiteManagementSubMenuRepository.class);
        adminService = new AdminServiceImpl(authService,roleRepository,adminRepository,notificationService,webSubManuRepository);
        //addKendraServiceImpl = new AddKendraServiceImpl(addKendraRepository,kendraService);

    }

    @Test
    void testUpdateExistingAdmin() throws IOException {
        AdminRequestPayload request = new AdminRequestPayload();
        request.setId(1L);
        request.setIsView((short) 1);
        request.setIsUserstatus((short)1);
        request.setIsDocumentVerification((short)1);
        request.setRoleid(2);

        Admin existingAdmin = new Admin();
        existingAdmin.setId(1L);
        existingAdmin.setRoleid(2);
        existingAdmin.setEmail("test@example.com");

        when(adminRepository.findById(request.getId())).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.save(existingAdmin)).thenReturn(existingAdmin);

        Map<String, Object> response = adminService.addAdmin(request);

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.USER_UPDATE_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testUpdateNonExistingAdmin() throws IOException {
        AdminRequestPayload request = new AdminRequestPayload();
        request.setId(1L);

        when(adminRepository.findById(request.getId())).thenReturn(Optional.empty());

        Map<String, Object> response = adminService.addAdmin(request);

        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ADMIN_ID_NOT_FOUND, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testAddNewAdmin() throws IOException {
        AdminRequestPayload request = new AdminRequestPayload();
        List<AdminStateResponsePayload> adminStateMappingList=new ArrayList<>();
        AdminStateResponsePayload adminStateMapping=new AdminStateResponsePayload();
        adminStateMapping.setStateId(1L);
        adminStateMappingList.add(adminStateMapping);
        List<Long> districtId=new ArrayList<>();
        districtId.add(1L);
        adminStateMapping.setDistrictId(districtId);
        request.setUserName("JohnDoe");
        request.setEmail("john@example.com");
        request.setRoleid(2);
        request.setAdminStateMapping(adminStateMappingList);
        Admin newAdmin = new Admin();
        newAdmin.setId(1L);

        when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(adminRepository.save(newAdmin)).thenReturn(newAdmin);

        RegistrationRequestPayload registrationRequest = new RegistrationRequestPayload();
        registrationRequest.setEmail(request.getEmail());
        registrationRequest.setPassword("JohnDoe@123456#");
        registrationRequest.setRoleId(request.getRoleid());
        registrationRequest.setMobileNumber("987654321");
        registrationRequest.setName("JohnDoe");

        when(authService.addAdmin(registrationRequest)).thenReturn(Map.of("responseCode", 200));
     //   doNothing().when(notificationService).sendOtpNew(eq(request.getEmail()), " test", "test ");

        Map<String, Object> response = adminService.addAdmin(request);

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.USER_ADDED_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testAddNewAdminWithDuplicateEmail() throws IOException {
        AdminRequestPayload request = new AdminRequestPayload();
        request.setUserName("JohnDoe");
        request.setEmail("john@example.com");
        request.setRoleid(2);

        Admin existingAdmin = new Admin();
        existingAdmin.setId(1L);

        when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingAdmin));

        Map<String, Object> response = adminService.addAdmin(request);

        assertEquals(DataConstant.CONFLICT, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.EMAIL_ALREADY_EXIST, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testAddNewAdminWithMissingFields() throws IOException {
        AdminRequestPayload request = new AdminRequestPayload();
        request.setUserName("");
        request.setEmail("");
        request.setRoleid(null);

        Map<String, Object> response = adminService.addAdmin(request);

        assertEquals(DataConstant.BAD_REQUEST, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ALL_FEILDS_MANDATORY, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testHandleException() throws IOException {
        AdminRequestPayload request = new AdminRequestPayload();
        request.setId(1L);

        when(adminRepository.findById(request.getId())).thenThrow(new RuntimeException("Server error"));

        Map<String, Object> response = adminService.addAdmin(request);

        assertNull(response.get(DataConstant.RESPONSE_CODE));
        assertNull(response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));
    }

    @Test
    void testUpdateExistingRole() throws IOException {
        RoleRequestPayload request = new RoleRequestPayload();
        request.setId(1L);
        request.setRole("Updated Role");

        Role existingRole = new Role();
        existingRole.setId(1L);

        when(roleRepository.findById(request.getId())).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        Map<String, Object> response = adminService.addRole(request);

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ROLE_UPDATE_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
    }

//    @Test
//    void testUpdateNonExistingRole() throws IOException {
//        RoleRequestPayload request = new RoleRequestPayload();
//        request.setId(1L);
//
//        when(roleRepository.findById(request.getId())).thenReturn(Optional.empty());
//
//        Map<String, Object> response = adminService.addRole(request);
//
//        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_NOT_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//    }

    @Test
    void testAddNewRole() throws IOException {
        RoleRequestPayload request = new RoleRequestPayload();
        request.setRole("New Role");

        Role newRole = new Role();
        newRole.setId(1L);

        when(roleRepository.save(any(Role.class))).thenReturn(newRole);

        Map<String, Object> response = adminService.addRole(request);

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ROLE_ADDED_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testAddNewRoleWithMissingFields() throws IOException {
        RoleRequestPayload request = new RoleRequestPayload();
        request.setRole("");

        Map<String, Object> response = adminService.addRole(request);

        assertEquals(DataConstant.BAD_REQUEST, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ROLE_ID_MANDATORY, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testAddRoleHandleException() throws IOException {
        RoleRequestPayload request = new RoleRequestPayload();
        request.setId(1L);

        when(roleRepository.findById(request.getId())).thenThrow(new RuntimeException("Server error"));

        Map<String, Object> response = adminService.addRole(request);

        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.MESSAGE));
    }

    @Test
    void testGetAllRolesSuccessfully() {
        List<Role> roles = Arrays.asList(
                new Role(1L, "Role1","test",new Date(),null,(short) 1),
                new Role(2L, "Role2","test",new Date(),null,(short) 1),
                new Role(3L, "Role3","test",new Date(),null,(short) 1)
        );

        when(roleRepository.findAll()).thenReturn(roles);

        Map<String, Object> response = adminService.getAllRoles();

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ROLE_FOUNDED_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
        assertEquals(3, ((List<Role>) response.get(DataConstant.RESPONSE_BODY)).size());
    }

    @Test
    void testGetAllRolesExcludingId6() {
        List<Role> roles = Arrays.asList(
                new Role(1L, "Role1","test",new Date(),null,(short) 1),
                new Role(2L, "Role2","test",new Date(),null,(short) 1),
                new Role(3L, "superadmin","test",new Date(),null,(short) 1)
        );

        when(roleRepository.findAll()).thenReturn(roles);

        Map<String, Object> response = adminService.getAllRoles();

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ROLE_FOUNDED_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
        List<Role> filteredRoles = ((List<Role>) response.get(DataConstant.RESPONSE_BODY)).stream().filter(x->!x.getRole().trim().equalsIgnoreCase(DataConstant.SUPERADMIN)).collect(Collectors.toList());
        assertEquals(2, filteredRoles.size());
        assertFalse(filteredRoles.stream().anyMatch(role -> role.getRole().equalsIgnoreCase(DataConstant.SUPERADMIN)));
    }

    @Test
    void testGetAllRolesNoRolesFound() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> response = adminService.getAllRoles();

        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.DATA_NOT_FOUND, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));
    }

    @Test
    void testDeleteByIdSuccessfully() {
        Long adminId = 1L;
        Admin admin = new Admin();
        admin.setId(adminId);

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

        Map<String, Object> response = adminService.deleteById(adminId);

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ADMIN_DELETE_SUCCESSFULLY, response.get(DataConstant.MESSAGE));
        verify(adminRepository, times(1)).deleteById(adminId);
    }
    @Test
    void testDeleteByIdNotFound() {
        Long adminId = 1L;

        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        Map<String, Object> response = adminService.deleteById(adminId);

        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ADMIN_ID_NOT_FOUND, response.get(DataConstant.MESSAGE));
        verify(adminRepository, times(0)).deleteById(adminId);
    }

 //   @Test
//    void testGetAdminByIdSuccessfully() {
//        Long adminId = 1L;
//        Admin admin = new Admin();
//        admin.setId(adminId);
//        admin.setRoleid(2);
//        AdminStateMapping stateMapping = new AdminStateMapping();
//        List<AdminStateMapping> stateMappings = Collections.singletonList(stateMapping);
//        admin.setAdminStateMapping(stateMappings);
//
//        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
//
//        Map<String, Object> response = adminService.getAdminById(adminId);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.ADMIN_FOUND, response.get(DataConstant.MESSAGE));
//        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
//        assertTrue(response.get(DataConstant.RESPONSE_BODY) instanceof AdminResponsePayload);
//
//        AdminResponsePayload adminResponse = (AdminResponsePayload) response.get(DataConstant.RESPONSE_BODY);
//        assertEquals(admin.getRoleid(), adminResponse.getRoleId());
//        assertEquals(admin.getAdminStateMapping().size(), adminResponse.getAdminStateMapping().size());
//
//        verify(adminRepository, times(1)).findById(adminId);
//    }

    @Test
    void testGetAdminByIdNotFound() {
        Long adminId = 1L;

        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        Map<String, Object> response = adminService.getAdminById(adminId);

        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.DATA_NOT_FOUND, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));

        verify(adminRepository, times(1)).findById(adminId);
    }
    @Test
    void testGetAdminByIdExceptionHandling() {
        Long adminId = 1L;

        when(adminRepository.findById(adminId)).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> response = adminService.getAdminById(adminId);

        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.SERVER_MESSAGE, response.get(DataConstant.MESSAGE));

        verify(adminRepository, times(1)).findById(adminId);
    }

//    @Test
//    void testGetAdminByEmailSuccessfully() {
//        String email = "test@example.com";
//        Admin admin = new Admin();
//        admin.setEmail(email);
//        admin.setRoleid(1);
//        admin.setRoleName("Admin");
//        AdminStateMapping stateMapping = new AdminStateMapping();
//        List<AdminStateMapping> stateMappings = Collections.singletonList(stateMapping);
//        admin.setAdminStateMapping(stateMappings);
//
//        Role role = new Role();
//        role.setId(1L);
//        role.setRole("Admin");
//
//        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(admin));
//        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
//
//        Map<String, Object> response = adminService.getAdminByEmail(email);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.ADMIN_FOUND, response.get(DataConstant.MESSAGE));
//        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
//        assertTrue(response.get(DataConstant.RESPONSE_BODY) instanceof AdminResponsePayload);
//
//        AdminResponsePayload adminResponse = (AdminResponsePayload) response.get(DataConstant.RESPONSE_BODY);
//        assertEquals(admin.getRoleid(), adminResponse.getRoleId());
//        assertEquals(role.getRole(), adminResponse.getRoleName());
//        assertEquals(admin.getAdminStateMapping().size(), adminResponse.getAdminStateMapping().size());
//
//        verify(adminRepository, times(1)).findByEmail(email);
//        verify(roleRepository, times(1)).findById(1L);
//    }

    @Test
    void testGetAdminByEmailNotFound() {
        String email = "test@example.com";

        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());

        Map<String, Object> response = adminService.getAdminByEmail(email);

        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.DATA_NOT_FOUND, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));

        verify(adminRepository, times(1)).findByEmail(email);
    }
    @Test
    void testGetAdminByEmailExceptionHandling() {
        String email = "test@example.com";

        when(adminRepository.findByEmail(email)).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> response = adminService.getAdminByEmail(email);

        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.SERVER_MESSAGE, response.get(DataConstant.MESSAGE));

        verify(adminRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetAllAdminInvalidPageIndexAndSize() {
        GetAllAdmin getAllAdmin = new GetAllAdmin();
        getAllAdmin.setPageIndex(null);
        getAllAdmin.setPageSize(null);

        Map<String, Object> response = adminService.getAllAdmin(getAllAdmin);

        assertEquals(DataConstant.BAD_REQUEST, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));
    }

//    @Test
//    void testGetAllAdminSuccessfullyWithoutPagination() {
//        GetAllAdmin getAllAdmin = new GetAllAdmin();
//        getAllAdmin.setPageIndex(0);
//        getAllAdmin.setPageSize(0);
//        getAllAdmin.setAdminId(1L);
//
//        List<Admin> adminList = new ArrayList<>();
//        Admin admin = new Admin();
//        admin.setId(1L);
//        admin.setRoleid(2);
//        AdminStateMapping stateMapping = new AdminStateMapping();
//        List<AdminStateMapping> stateMappings = Collections.singletonList(stateMapping);
//        admin.setAdminStateMapping(stateMappings);
//        adminList.add(admin);
//
//        when(adminRepository.findAllByIdNot(getAllAdmin.getAdminId())).thenReturn(adminList);
//
//        Map<String, Object> response = adminService.getAllAdmin(getAllAdmin);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
//        assertTrue(response.get(DataConstant.RESPONSE_BODY) instanceof AdminResponsePage);
//
//        AdminResponsePage adminResponsePage = (AdminResponsePage) response.get(DataConstant.RESPONSE_BODY);
//        assertFalse(adminResponsePage.getAdminResponsePayloadList().isEmpty());
//        assertEquals(1, adminResponsePage.getAdminResponsePayloadList().size());
//    }

//    @Test
//    void testGetAllAdminSuccessfullyWithPagination() {
//        GetAllAdmin getAllAdmin = new GetAllAdmin();
//        getAllAdmin.setPageIndex(0);
//        getAllAdmin.setPageSize(1);
//        getAllAdmin.setAdminId(1L);
//
//        Admin admin = new Admin();
//        admin.setId(1L);
//        admin.setRoleid(2);
//        AdminStateMapping stateMapping = new AdminStateMapping();
//        List<AdminStateMapping> stateMappings = Collections.singletonList(stateMapping);
//        admin.setAdminStateMapping(stateMappings);
//        List<Admin> adminList = Collections.singletonList(admin);
//        Page<Admin> page = new PageImpl<>(adminList);
//
//        when(adminRepository.findAllByIdNotAndRoleidNot(eq(getAllAdmin.getAdminId()), eq(6), any(Pageable.class)))
//                .thenReturn(page);
//
//        Map<String, Object> response = adminService.getAllAdmin(getAllAdmin);
//
//        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
//        assertTrue(response.get(DataConstant.RESPONSE_BODY) instanceof AdminResponsePage);
//
//        AdminResponsePage adminResponsePage = (AdminResponsePage) response.get(DataConstant.RESPONSE_BODY);
//        assertFalse(adminResponsePage.getAdminResponsePayloadList().isEmpty());
//        assertEquals(1, adminResponsePage.getAdminResponsePayloadList().size());
//        assertEquals(0, adminResponsePage.getPageIndex());
//        assertEquals(1, adminResponsePage.getPageSize());
//        assertEquals(1, adminResponsePage.getTotalPages());
//        assertEquals(1, adminResponsePage.getTotalElement());
//    }

//    @Test
//    void testGetAllAdminNotFound() {
//        GetAllAdmin getAllAdmin = new GetAllAdmin();
//        getAllAdmin.setPageIndex(0);
//        getAllAdmin.setPageSize(0);
//        getAllAdmin.setAdminId(1L);
//
//        when(adminRepository.findAllByIdNot(getAllAdmin.getAdminId())).thenReturn(Collections.emptyList());
//
//        Map<String, Object> response = adminService.getAllAdmin(getAllAdmin);
//
//        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
//        assertEquals(DataConstant.RECORD_NOT_FOUND_MESSAGE, response.get(DataConstant.MESSAGE));
//        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
//        assertTrue(response.get(DataConstant.RESPONSE_BODY) instanceof AdminResponsePage);
//
//        AdminResponsePage adminResponsePage = (AdminResponsePage) response.get(DataConstant.RESPONSE_BODY);
//        assertTrue(adminResponsePage.getAdminResponsePayloadList()==null);
//    }
    @Test
    void testGetAllAdminExceptionHandling() {
        GetAllAdmin getAllAdmin = new GetAllAdmin();
        getAllAdmin.setPageIndex(0);
        getAllAdmin.setPageSize(0);
        getAllAdmin.setAdminId(1L);

        when(adminRepository.findAllByIdNot(getAllAdmin.getAdminId())).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> response = adminService.getAllAdmin(getAllAdmin);

        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.SERVER_MESSAGE, response.get(DataConstant.MESSAGE));
    }
    @Test
    void testAdminStatusUpdateSuccess() {
        Long adminId = 1L;
        short status = 1;

        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setStatus((short) 0);

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Map<String, Object> response = adminService.adminStatusUpdate(adminId, status);

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ADMIN_STATUS_UPDATE, response.get(DataConstant.MESSAGE));
        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
        assertTrue(response.get(DataConstant.RESPONSE_BODY) instanceof Admin);

        Admin updatedAdmin = (Admin) response.get(DataConstant.RESPONSE_BODY);
        assertEquals(status, updatedAdmin.getStatus());

        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void testAdminStatusUpdateNotFound() {
        Long adminId = 1L;
        short status = 1;

        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        Map<String, Object> response = adminService.adminStatusUpdate(adminId, status);

        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.DATA_NOT_FOUND, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));

        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, times(0)).save(any(Admin.class));
    }

    @Test
    void testAdminStatusUpdateExceptionHandling() {
        Long adminId = 1L;
        short status = 1;

        when(adminRepository.findById(adminId)).thenThrow( RuntimeException.class);

        Map<String, Object> response = adminService.adminStatusUpdate(adminId, status);

        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.SERVER_MESSAGE, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));

    }

    @Test
    void testUpdateAdminProfileSuccess() {
        ChangeProfileRequest changeProfileRequest = new ChangeProfileRequest();
        changeProfileRequest.setId(1L);
        changeProfileRequest.setUserName("New Name");

        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUserName("Old Name");

        when(adminRepository.findById(changeProfileRequest.getId())).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Map<String, Object> response = adminService.updateAdminProfile(changeProfileRequest);

        assertEquals(DataConstant.OK, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.ADMIN_PROFILE_UPDATE, response.get(DataConstant.MESSAGE));
        assertNotNull(response.get(DataConstant.RESPONSE_BODY));
        assertTrue(response.get(DataConstant.RESPONSE_BODY) instanceof Admin);

        Admin updatedAdmin = (Admin) response.get(DataConstant.RESPONSE_BODY);
        assertEquals(changeProfileRequest.getUserName(), updatedAdmin.getUserName());

        verify(adminRepository, times(1)).findById(changeProfileRequest.getId());
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void testUpdateAdminProfileNotFound() {
        ChangeProfileRequest changeProfileRequest = new ChangeProfileRequest();
        changeProfileRequest.setId(1L);

        when(adminRepository.findById(changeProfileRequest.getId())).thenReturn(Optional.empty());

        Map<String, Object> response = adminService.updateAdminProfile(changeProfileRequest);

        assertEquals(DataConstant.NOT_FOUND, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.DATA_NOT_FOUND, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));

        verify(adminRepository, times(1)).findById(changeProfileRequest.getId());
        verify(adminRepository, times(0)).save(any(Admin.class));
    }

    @Test
    void testUpdateAdminProfileExceptionHandling() {
        ChangeProfileRequest changeProfileRequest = new ChangeProfileRequest();
        changeProfileRequest.setId(1L);

        when(adminRepository.findById(changeProfileRequest.getId())).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> response = adminService.updateAdminProfile(changeProfileRequest);

        assertEquals(DataConstant.SERVER_ERROR, response.get(DataConstant.RESPONSE_CODE));
        assertEquals(DataConstant.SERVER_MESSAGE, response.get(DataConstant.MESSAGE));
        assertNull(response.get(DataConstant.RESPONSE_BODY));

        verify(adminRepository, times(1)).findById(changeProfileRequest.getId());
        verify(adminRepository, times(0)).save(any(Admin.class));
    }

}