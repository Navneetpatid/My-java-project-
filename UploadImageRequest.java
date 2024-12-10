package com.janaushadhi.adminservice.requestpayload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageRequest {



    private Long id;

    private String imageUrl;
    
    private String category;

    private String name;

    private String designation;
    
    private String title;

    private String description;
    
    private UploadImageHindiRequest uploadImageHindirequest;
}
