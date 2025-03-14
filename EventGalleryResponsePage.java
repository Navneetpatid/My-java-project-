package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

import java.util.List;
@Data
public class EventGalleryResponsePage {

    private Integer pageIndex;

    private Integer pageSize;

    private Long totalElement;

    private Integer totalPages;

    private Boolean isLastPage;

    private Boolean isFirstPage;

    private List<EventGalleryResponse> galleryResponseList;
}
