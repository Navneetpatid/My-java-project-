package com.janaushadhi.adminservice.responsepayload;

import java.util.List;

import lombok.Data;

@Data
public class AdminKendraAppResponsePage {

	private Integer pageIndex;

    private Integer pageSize;

    private Long totalElement;

    private Integer totalPages;

    private Boolean isLastPage;

    private Boolean isFirstPage;

    private List<AdminKendraApplicationResponse> adminKendraApplicationsList;
}
