package com.janaushadhi.adminservice.requestpayload;
import java.util.List;

import com.janaushadhi.adminservice.entity.WebsiteManagementSubMenu;
import com.janaushadhi.adminservice.responsepayload.AdminStateResponsePayload;

import lombok.Data;

@Data
public class AdminRequestPayload {

    private  Long id;
    private String userName;
    private String email;
    private String adminProfile;
    private  Integer  roleid;
    private String description;
    private Short isView;
    private  Short isDocumentVerification;
    private Short isUserstatus;
    private Short isApplicationStatus;
    private Short isDrugLicenceVerification;
    private Short isAggrementVerification;
    private Short isIntitailApprovalWithDSC;
    private Short isFinalApprovalWithDSC;
    private Short status;
    private Short isAllState;
    //25-06-2025 //role management
    private Short isDashboard;
    private Short isUserManagement;
    private Short isRecruitment;
    private Short isWebsiteManagament;
    private WebsiteManagementSubMenuRequest websiteManagementSubMenu;
  //25-06-2025 //role management end
//    private List<Short> stateId;
//    private List<Short> districtId;
    private List<AdminStateResponsePayload> adminStateMapping;
//    private List<AdminDistrictMapping> adminDistrictMapping;
//    private List<AdminStateDistrictPayload> adminStateDistrictPayload;
    
    //04-07-2024 for login access for categorywise
    private String accessCategory; 
    private String accessSubCategory;
    private Short isExportCsv;
}
