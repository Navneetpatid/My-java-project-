package com.janaushadhi.adminservice.responsepayload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductRPageResponse {
    List<NewProductResponse> newProductResponsesList;
    private Integer pageIndex;
    private Integer pageSize;
    private Long totalElement;
    private Boolean isLastPage;

    private Boolean isFirstPage;
    private Integer totalPages;
}
