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
@Table(name = "amendment")
public class Amendment {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)

    private  Long id;
    private String tenderSubTitle;
    private String publishDate;
    private String closingDate;
    private String tenderfile;
    private String nitfile;
    private String boqfile;
    private Date createdDate;
    private Date updatedDate;
    private  Short status;

}
