package com.janaushadhi.adminservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "upload_image")
public class UploadImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "name")
    private String name;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "designation")
    private String designation;

    @Lob
    @Column(name = "description",columnDefinition = "Text")
    private String description;

    @Column(name = "status")
    private Short status;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;
    @OneToOne
    private UploadImageHindi uploadImageHindi;
}
