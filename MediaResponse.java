package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponse {

    private  Long id;
    private String month ;
    private int year;
    private String eventsTitle;
    private String eventDate;
    private String createdDate;
    private String updatedDate;
    private Short status;
    private Integer serialNo;
}
