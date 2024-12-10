package com.janaushadhi.adminservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tender")
public class Tender {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private String tenderTitle;
    private Date publishDate;
    private Date closingDate;
    private String tenderfile;
    private String nitfile;
    private String boqfile;
    private Date createdDate;
    private Date updateddate;
    private Short status;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tenderId", referencedColumnName = "id")
    private List<Amendment> amendmentList;

}
