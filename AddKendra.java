package com.janaushadhi.adminservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin_add_kendra")
public class AddKendra {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private String contactPerson;
    private String storeCode;
    private Long pinCode;
    private Long stateId;
    private Long districtId;
    private String kendraAddress;
    private Date createdDate;
    private Date updatedDate;
    private Short status;
    private String latitude;
    private String longitude;
}
