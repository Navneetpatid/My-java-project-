package com.janaushadhi.adminservice.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenderRequestPayload {

    private String tenderTitle;
    private String publishDate;
    private String closingDate;
    private String tenderfile;
    private String nitfile;
    private String boqfile;


}
