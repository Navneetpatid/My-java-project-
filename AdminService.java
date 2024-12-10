package com.janaushadhi.adminservice.service;

import java.io.IOException;
import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.AdminRequestPayload;
import com.janaushadhi.adminservice.requestpayload.ChangeProfileRequest;
import com.janaushadhi.adminservice.requestpayload.GetAllAdmin;
import com.janaushadhi.adminservice.requestpayload.RoleRequestPayload;

public interface AdminService {



    public Map<String,Object> addAdmin(AdminRequestPayload adminRequestPayload) throws IOException;
    public Map<String,Object> addRole(RoleRequestPayload roleRequestPayload) throws IOException;
    public Map<String,Object> getAllRoles();
    public Map<String, Object> getAdminById(Long id);
    public  Map<String,Object> deleteById(Long id);
    public Map<String,Object> adminStatusUpdate(Long id,short status);

   public Map<String, Object> getAllAdmin(GetAllAdmin getAllAdmin);

	public Map<String, Object> getAdminByEmail(String email);

    public Map<String,Object> updateAdminProfile(ChangeProfileRequest changeProfileRequest);
}
