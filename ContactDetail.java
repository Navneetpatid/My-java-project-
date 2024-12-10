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
@Table(name = "contact_detail")
public class ContactDetail {


    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private String name;
    private String designation;
    private String emailId;
    private String contactNo;
    private String intercomNo;
    private String department;
    private Date createdDate;
    private Date updatedDate;
    private Short status;

}

