package com.janaushadhi.adminservice.responsepayload;

import lombok.Data;

@Data
public class GetAllByNews {

   private Integer pageIndex;
    private Integer pageSize;
    private   String type;
    private String searchByTitle;
}
