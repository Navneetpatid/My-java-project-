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
@Table(name = "media")
public class MediaCalender {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private String month ;
    private Integer year;
    private String eventsTitle;
    private String eventDate;
    private Date createdDate;
    private Date updatedDate;
    private Short status;
}
