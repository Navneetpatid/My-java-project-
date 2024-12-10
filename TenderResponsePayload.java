package com.janaushadhi.adminservice.responsepayload;

import com.janaushadhi.adminservice.entity.Amendment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenderResponsePayload {


    private  Long id;
    private String tenderTitle;
    private String publishDate;
    private String closingDate;
    private String tenderfile;
    private String nitfile;
    private String boqfile;
    private String createdDate;
    private String updateddate;
    private Short status;
    private List<Amendment> amendments;
    private Integer serialNo;
}
