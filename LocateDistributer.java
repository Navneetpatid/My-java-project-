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
public class LocateDistributer {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private String nameOfFirm;
    private String email;
    private String contactNumber;
    private String code;
    private String distributorAddress;
    private Long stateId;
    private Long districtId;
    private Long cityId;
    private Long pinCode;
    private Date createdDate;
    private Date updateddate;
    private  Short status;

}
