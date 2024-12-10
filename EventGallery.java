package com.janaushadhi.adminservice.entity;

import com.janaushadhi.adminservice.requestpayload.EventImages;
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
public class EventGallery {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private  Long id;
    private  String eventTitle;
    private String eventDate;
    private String eventCategory;
    @ElementCollection
    private List<EventImages> images;
    private Date createdDate;
    private Date updatedDate;
    private Short status;
}
