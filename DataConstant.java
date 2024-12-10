package com.janaushadhi.adminservice.util;

public class DataConstant {

	public static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public static final String RESPONSE_CODE = "responseCode";
	public static final String MESSAGE = "message";
	public static final String RESPONSE_BODY = "responseBody";
	public static final Short ZERO = 0;
	public static final Short ONE = 1;
	public static final int OK = 200;
	public static final int BAD_REQUEST = 400;
	public static final int NOT_AUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final String WRONGEMAILPASSWORD = "402";//NOSONAR
	public static final int NOT_FOUND = 404;
	public static final int CONFLICT = 409;

	public static final int SERVER_ERROR = 500;
	public static final String PASSWORD_UPDATED_SUCCESSFULLY = "Password Updated Successfully.";
	public static final String OTP_VERIFIED_SECCUSSFULLY = "OTP Verified Successfully";
	public static final String PLEASE_ENTER_EMAIL_OR_MOBILE_NUMBER = "Please Enter Email Or Mobile Number";
	public static final String INVALID_OPT = "Invalid OTP";
	public static final String USER_NOT_FOUND = "User Not Found";
	public static final String PLEASE_ENTER_NEW_PASSWORD_AND_CONFIRM_PASSWORD = "Please Enter New Password And Confirm Password.";
	public static final String PASSWORD_NOT_MATCHED = "Password Not Matched";
	public static final String USER_REGISTERED_SUCCESSFULLY = "User Registered Successfully";
	public static final String USER_ALREADY_EXIST = "This UserId or Email or Mobile no. Already Exist";
	public static final String ALL_FIELDS_ARE_MANDATORY = "All Fields Are Mandatory";
	public static final String INVALID_CAPTACHA = "Invalid Captcha";
	public static final String CAPTCHA_GENERATED_SUCCESSFULLY = "Captcha_Generated_Successfully";
	public static final String USER_LOGIN_SUCCESSFULLY = "User Login Successfully";
	public static final String BAD_CREDENTIALS = "Invalid Id Password";
	public static final String PLEASE_INPUT_CAPTCHA = "Please Input Captcha";
	public static final String PLEASE_INPUT_VALID_CREDENTIAL = "Please Input Valid Credentials";
	public static final String OTP_SEND_SUCCESSFULLY = "OTP Send Successfully";
	public static final String OTP_SEND_SUCCESSFULLY_ON_MOBILE = "OTP Send Successfully On Your Mobile No.";
	public static final String INVALID_EMAIL_OR_NUMBER = "Invalid Email Or Number";

	public static final String VERIFY_OTP = "Verify OTP";

//	public static final String RESPONSEBODY = "responsebody";
	// public static final String OK = "200";
	public static final String USER_ADDED_SUCCESSFULLY = "Admin User Added Successfully";
	public static final String DATA_NOT_FOUND = "Data Not Found";
	public static final String USER_UPDATE_SUCCESSFULLY = "Admin User Updated Successfully";
	public static final String ROLE_UPDATE_SUCCESSFULLY = "role Updated successfully";
	public static final String ROLE_ADDED_SUCCESSFULLY = " role added successfully";
	public static final String ADD_ADDRESS = "please add address for LoFeildOfficer";
	public static final String FAILED_TO_ADD_ADMIN = "failed to add admin";
	public static final String ROLE_ID_MANDATORY = "role id mandatory";
	public static final String ALL_FEILDS_MANDATORY = " All  feilds are mandatory";

	public static final String ADDRESS_IS_MANDATORY = "please add state, district, block..!!";

	public static final String ADMIN_DELETE_SUCCESSFULLY = "admin deleted successfully";
	public static final String ADMIN_ID_NOT_FOUND = "admin id not found in database";
	public static final String ADMIN_FOUND = "adminDetils founded  successfully ";
	public static final String SERVER_MESSAGE = "server error";
	public static final String RECORD_NOT_FOUND_MESSAGE = "record not found";
	public static final String RECORD_FOUND_MESSAGE = "record found successfully";
	public static final String STATUS_INVALID_MESSAGE = "ststus invaild";
	public static final String PAGE_SIZE_MESSAGE = "invaild page size";
	public static final String PAGE_SIZE_AND_INDEX_CANT_NULL_MESSAGE = "pageSize and page index cant be null";
	public static final String INVALID_CREDENTIAL = "invaild credeantial";
	public static final String DB_CONNECTION_ERROR = "db connection error";
	public static final String ADMIN_STATUS_UPDATE = "admin status updated";
	public static final String ROLE_FOUNDED_SUCCESSFULLY = "role founded successfully";
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	public static final String EMAIL_ALREADY_EXIST = "This email already exist..!! ";


//=============================================================Add product constant================================================================================////////
	public static final String OBJECT_RESPONSE = "object";
	//public static final String DATA = "data";
	public static final String PRODUCT_ADD_SUCCESSFULLY = " Product add successfully";
	public static final String PRODUCT_NOT_ADD_SUCCESSFULLY = "Product Not added ";
	public static final String PRODUCT_UPDATE_SUCCESSFULLY = "Product uddate successfully";
	public static final String SERVER_ISSUE = "Something went wrong!";
	public static final String PRODUCT_FAILED_TO_UPDATE = "Failed to update product";

	public static final Object PRODUCT_FOUND_SUCCESS = "Product found successfully";

	public static final Object PRODUCT_NOT_FOUND = "Product not found in database";

	public static final String DATA = "data";

	public static final Object MANDATORY_FIELDS_MISSING = "Please fill mandatory field ";


    public static final String RECORD_ADDED_SUCCESSFULLY = "file added successfully";
	public static final String BAD_REQUEST_MESSAGE = "bad request";
	public static final String NO_SERVER_CONNECTION = "Internal server error";
    public static final String TENDER_ADDED_SUCCESSFULLY ="tender added successfully" ;
    public static final String TENDER_UPDATE_SUCCESSFULLY = "tender update //amendment add successfully";
    public static final String TENDER_STATUS_UPDATE ="tender status updated successfully" ;

	public static final String UPDATED_SUCCESSFULLY = "Record Updated Successfully";

	public static final String PLEASE_INPUT_APPLICATION_ID = "Please Input ApplicationId";

	public static final String FAILED_TO_UPDATE_KENDRA = "Failed to update kendra.";
	public static final String FAILED_TO_UPDATE_ROLE = "Failed to update admin role";
    public static final String DISTRIBUTOR_ADDED_SUCCESSFULLY = "distributor added successfully";
	public static final String DISTRIBUTOR_FOUNDED_SUCCESSFULLY ="distributor found successfully" ;
	public static final String TENDER_FOUNDED_SUCCESSFULLY = "tender found successfully";


	public static final String CODE_OR_EMAIL_ALREADY_EXIST = "distributor code or email already exist";
    public static final String DISTRIBUTOR_UPDATED = "distributor updated successfully";

	public static final String FAILED_TO_ADD_KENDRA = "Failed to add kendra.";

	public static final String INVALID_REQUEST = "PageNo PageSize can't be null.";

	public static final String ADMIN_NOT_FOUND = "admin id not found";

	public static final String ADMIN_ID_NOT_NULL = "admin id not null or zero (0)";


	//-----------------Product-Messages---------------------------------------
	public static final Object DEACTIVATED_SUCCESSFULLY = "Deactivated Successfully........!";
	public static final Object ACTIVATED_SUCCESSFULLY = "Activated Successfully........!";
	//public static final Object INVALID_REQUEST = "Invalid Request........!";
	public static final String PRODUCT_DELETE_SUCCESSFULLY = "Product Delete Successfully........!";

	//-------------------CSV-Class----------------------------------------------------
	public static final String FAIL_CSV_FILE = "fail to parse CSV file: ";
	public static final String DRUG_CODE = "Drug Code";
	public static final String MRP = "MRP";
	public static final String STATUS = "Status";
	public static final String UNIT_SIZE = "Unit Size";
	public static final String GROUP_NAME = "Group Name";
	public static final String GENERIC_NAME = "Generic Name";
	public static final String UTF = "UTF-8";
	//----------------------------------------------------------------------------------
	public static final String DATA_STORE_SUCCESSFULLY = "Data Store Successfully";
	public static final String FILE_NOT_FOUND = "File not Found";

	//----------------------------------Banner-----------------------------------------
	public static final String BANNER_ADD_SUCCESSFULLY = "Banner Add Successfully";
	public static final String BANNER_UPDATE_SUCCESSFULLY = "Banner Updated Successfully";
	public static final String BANNER_NOT_UPDATE_SUCCESSFULLY = "Banner Not Updated !";
	public static final String BANNER_NOT_FOUND = "Banner Not Found......";
	public static final String BANNER_FOUND = "Banner Found Successfully......";
	public static final String ID = "id";
	public static final String CODE = "Code";
	public static final String CONTANCT ="ContactNumber" ;
	public static final String NAMEOFFIRM ="NameOfFirm" ;



	public static final String ADDRESS = "DistributorAddress";
	public static final String DISTRICT_ID = "DistrictId";
	public static final String STATE_ID = "StateId";
	public static final String CREATED_DATE = "created_date";
	public static  final String Email="Email";

	public static final String SUCCESS = "Success";

	public static final String RECORD_REMOVED_SUUCESSFULLY = "Record removed successfully";

    public static final String GOVERNING_ADDED_SUCCESSFULLY ="governing council added successfully" ;
    public static final String EXECUTIVE_ADDED_SUCCESSFULLY ="executive council added successfully" ;
    public static final String CONTACT_ADDED_SUCCESSFULLY = "contact added successfully";
	public static final String CONTACT_STATUS_UPDATE = "contact status updated";
	public static final String CONTACT_FOUNDED_SUCCESSFULLY ="contact found successfully" ;
    public static final String CONTACT_UPDATED ="contact Updated" ;
    public static final String ID_NOT_FOUND = "id not found";
	public static final String CODE_ALREADY_EXIST ="store code already exist" ;
	public static final String KENDRA_ADDED_SUCCESSFULLY = "admin add kendra successfully";
	public static final String KENDRA_STATUS_UPDATE = "kendra status update";
	public static final String KENDRA_FOUNDED_SUCCESSFULLY ="kendra found successfully" ;
	public static final String KENDRA_UPDATED = "kendra updated";
	public static final String MANAGEMENT_UPDATED = "management updated successfully";
	public static final String MANAGEMENT_ADDED_SUCCESSFULLY = "management addess successfully";
	public static final String MANAGEMENT_FOUNDED_SUCCESSFULLY ="management found successfully" ;


	//------------------Upload-image---------------------------------------------
	public static final String IMAGE_ADD_SUCCESSFULLY = "Image Added Successfully";
	public static final String Update_ADD_SUCCESSFULLY = "Update Added Successfully";
	public static final String IMAGE_NOT_UPDATE_SUCCESSFULLY = "Image Not Updated !";

	public static final Object IMAGE_DELETE_SUCCESSFULLY = "Image Delete Successfully........!";
	public static final String IMAGE_NOT_FOUND = "Image Not Found Successfully......";
    public static final String ADMIN_PROFILE_UPDATE ="admin profile updated" ;
    public static final String MEDIA_EVENT_ADDED_SUCCESSFULLY =  "media event added successfully";
    public static final String DEBARRED_UPDATED = "debarred updated successfully";
	public static final String DEBARRED_ADDED_SUCCESSFULLY ="debarred added successfully" ;
    public static final String BLACK_LIST_UPDATED = "black list updated successfully";
    public static final String REVOCATION_ORDER_UPDATED = "revocation order updated successfully";
	public static final String REVOCATION_ORDER_ADDED_SUCCESSFULLY ="revocation addedd successfully" ;
    public static final String AWARDED_TENDER_ADDED_SUCCESSFULLY = "awarded tender added successfully";
    public static final String AWARDED_TENDER_UPDATE_SUCCESSFULLY ="awarded tender update successfully" ;
   
    public static final String PAGE_SIZE_AND_INDEX_AND_REPORT_TYPE_CANT_NULL = "pageSize and page index cant be null and reportType should be 'Annual' & 'Financial'";
    public static final String ANNUAL = "Annual";
	public static final String FINANCIAL = "Financial";
	public static final String INVALID_REPORT_TYPE = "Invalid report type. It should be 'Annual' & 'Financial'";

	public static final String WHATS_NEW = "WhatsNew";
	public static final String NEWS_LATTER = "NewsLatter";
	public static final String INVALID_TYPE = "Invalid report type. It should be 'WhatsNew' & 'NewsLatter'";
	public static final String PAGE_SIZE_AND_INDEX_AND_TYPE_CANT_NULL = "pageSize and page index cant be null and type should be 'WhatsNew' & 'NewsLatter'";
	public static final String ID_NOT_NULL_AND_ZERO = "id should not null & zero (0)";
    public static final String SUPERADMIN = "superadmin";


	public static final String RECORD_DELETED_SUCCESSFULLY = "Record deleted seccussfully";

	public static final String PLEASE_ADD_ID_TO_DELETE = "Please input id to delete";

    public static final String TENDER_DELETE_SUCCESSFULLY = "tender deleted successfully";

	public static final String TENDER_ID_NOT_FOUND = "Tender id not found";
	public static final String DEBARRED_LIST_DELETE_SUCCESSFULLY = "debarred list deleted successfully";
	public static final String DEBARRED_ID_NOT_FOUND = "debarred id not found";
	public static final String AWARDED_TENDER_DELETED_SUCCESSFULLY = "awarded tender deleted successfully";
	public static final String AWARDED_TENDER_ID_NOT_FOUND = "awarded tender id not found";
	public static final String REVOCATION_ORDER_DELETE_SUCCESSFULLY = "revocation order deleted successfully";
	public static final String REVOCATION_ORDER_ID_NOT_FOUND = "revocation order id not found";
	public static final String BLACK_LIST_DELETE_SUCCESSFULLY = "black list deleted successfully";
	public static final String BLACK_LIST_ID_NOT_FOUND = "blackList id not found";

    public static final String KENDRA_DELETE_SUCCESSFULLY = "kendra deleted successfully in bulk";
	public static final String KENDRA_NOT_FOUND ="kendra not found" ;
	public static final Short TWO = 2;
	public static final String FILE_UPLOAD_FAILD ="file upload failed" ;
    public static final String EVENT_GALLERY_ADDED_SUCCESSFULLY ="event Gallery images addes successfully" ;
    public static final String EVENT_UPDATED ="event update successfully" ;

	public static final String APPLICATION_REJECTED = "Application Rejected";
    public static  final  String  Date="yyyy-MM-dd";
    public static final String KENDRA_NOT_FOUND_IN_REDIUS ="no kendra found in the redius of 3 km " ;
}
