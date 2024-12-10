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
@Table(name = "black_list")
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String drugName;
    private String drugCode;
    private String manufacturedBy;
    private String fromDate;
    private String uploadImage;
    private String toDate;
    private String reason;
    private Short status;
    private Date createdDate;
    private  Date updatedDate;
}
