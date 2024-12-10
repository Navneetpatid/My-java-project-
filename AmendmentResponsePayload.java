package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmendmentResponsePayload {

    private String tenderSubTitle;
    private String publishDate;
    private String closingDate;
    private String tenderfile;
    private String nitfile;
    private String boqfile;
    private Short status;
    private String createdDate;
    private String updatedDate;

}
