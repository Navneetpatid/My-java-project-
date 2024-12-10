package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

  //  private  Long id;
    private String month ;
    private Integer year;
    private String eventTitle;
    private String eventDate;
    private Short status;
}
