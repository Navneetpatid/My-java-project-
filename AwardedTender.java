package com.janaushadhi.adminservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "awarded_tender")
public class AwardedTender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String yearOfPurchase;
    private String nitFile;
    private String department;
    private Short status;
    private Date createdDate;
    private Date updatedDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "awardedId", referencedColumnName = "id")
    private  List<Purchase> purchaseList;


}
