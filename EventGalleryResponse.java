package com.janaushadhi.adminservice.responsepayload;

import com.janaushadhi.adminservice.requestpayload.EventImages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventGalleryResponse {
    private Long id;
    private  String eventTitle;
    private String eventDate;
    private String eventCategory;
    private List<EventImages> images;
    private Short status;
    private int serialNo;
}
