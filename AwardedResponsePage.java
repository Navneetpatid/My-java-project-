package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;
import java.util.List;

@Data
public class AwardedResponsePage {
    private Integer pageIndex;

    private Integer pageSize;

    private Long totalElement;

    private Integer totalPages;

    private Boolean isLastPage;

    private Boolean isFirstPage;

    private List<AwardedTenderResponse> awardedTenderResponseList;
}
