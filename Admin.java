package com.janaushadhi.adminservice.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private String userName;
    private String email;
    private String adminProfile;
    private  Integer  roleid;
    private String roleName;
    private String description;
    private Date createdDate;
    private  Date updatedDate;
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
    
    private Short isDashboard;
    private Short isUserManagement;
    private Short isRecruitment;
    private Short isWebsiteManagament;
    @OneToOne
    private WebsiteManagementSubMenu websiteManagementSubMenu;
    @OneToMany(cascade = CascadeType.ALL)
    private List<AdminStateMapping> adminStateMapping;
    //04-07-2024 for login access for categorywise
    private String accessCategory; 
    private String accessSubCategory;
    private Short isExportCsv;


}
