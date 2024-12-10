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
@Table(name = "revocation_order")
public class RevocationOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String drugName;
    private String drugCode;
    private String manufacturedBy;
    private String blackListDate;
    private String uploadImage;
    private String reason;
    private Short status;
    private Date createdDate;
    private  Date updatedDate;
}
