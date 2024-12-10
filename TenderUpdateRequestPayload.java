package com.janaushadhi.adminservice.requestpayload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenderUpdateRequestPayload {

    private Long tenderId;
    private List<AmendmentRequestPayload> amendmentRequestPayloads;

}
