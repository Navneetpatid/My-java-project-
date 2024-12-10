package com.janaushadhi.adminservice.requestpayload;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class EventGalleryRequest {

    private Long id;
    private  String eventTitle;
    private String eventDate;
    private String eventCategory;
    private List<EventImages> images;
    private Short status;
}
