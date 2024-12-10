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
@Table(name = "management_team")
public class ManagementTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private String eduQualification;
    private String contactNo;
    private String designation;
    private String mail;
    private String fax;
    private String photo;
    private Date createdDate;
    private Date updatedDate;
    private Short status;
    private boolean imageIsActive;

}
