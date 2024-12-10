package com.janaushadhi.adminservice.responsepayload;
import lombok.Data;

import java.util.List;

import com.janaushadhi.adminservice.requestpayload.WebsiteManagementSubMenuRequest;

@Data
public class AdminResponsePayload {

    private  Long id;
    private String userName;
    private String email;
//    private AdminAddressResponsePayload adminAddress;
    private String adminProfile;
    private  Integer  roleId;
    private String roleName;
    private String description;
    private String createdDate;
    private  String updatedDate;

    //permissions
    private Short isView;
    private  Short isDocumentVerification;
    private Short isUserstatus;
    private Short isApplicationStatus;
    private Short isDrugLicenceVerification;
    private Short isAggrementVerification;
    private Short isIntitailApprovalWithDSC;
    private Short isFinalApprovalWithDSC;
    private  Short status;
    private Short isAllState;
//    private List<Short> stateId;
//    private List<Short> districtId;
    
    private Short isDashboard;
    private Short isUserManagement;
    private Short isRecruitment;
    private Short isWebsiteManagament;
    private WebsiteManagementSubMenuRequest websiteManagementSubMenu;
    
    private List<AdminStateResponsePayload> adminStateMapping;
//    private List<AdminDistrictPayload> adminDistrictList;
    private Integer serialNo;
    
    //04-07-2024 for login access categorywise
    private String accessCategory; 
    private String accessSubCategory;
    private Short isExportCsv;
}
