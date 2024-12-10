package com.janaushadhi.adminservice.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictOfIndiaResponse {

    private Long id;
    //   `S.No.`	INT,
    private Integer stateCode;
    private Long stateId;
    private Integer districtCode;//	VARCHAR(512),
    private String stateNameInEnglish;//	VARCHAR(512),
    private String districtNameInEnglish;//	VARCHAR(512),
    private Integer census2001Code;//	INT,
    private Integer census2011Code;//	INT,   private String stateOrUt;//	VARCHAR(512)
}
