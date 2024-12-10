package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmendmentRequestPayload  {

    private String tenderSubTitle;
//    private Date publishDate;
//    private Date closingDate;
    private String publishDate;
    private String closingDate;
    private String tenderfile;
    private String nitfile;
    private String boqfile;
    private Short status;
}
