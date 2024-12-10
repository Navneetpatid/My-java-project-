package com.janaushadhi.adminservice.responsepayload;
import lombok.Data;

@Data
public class UploadImageResponse {

    private Long id;

    private String imageUrl;

    private String name;
    
    private String category;
    
    private String designation;
    
    private String title;
    
    private String description;

    private Short status;

    private String createdDate;

    private String updatedDate;
    
    private Integer serialNo;
    
    private UploadImageHindiResponse uploadImageHindiResponse;
}
