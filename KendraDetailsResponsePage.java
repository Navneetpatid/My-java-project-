package com.janaushadhi.adminservice.responsepayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KendraDetailsResponsePage {

	private Integer pageIndex;

	private Integer pageSize;

	private Long totalElement;

	private Integer totalPages;

	private Boolean isLastPage;

	private Boolean isFirstPage;

	// private List<KendraDetailsResponse> KendraResponseList;

	private List<KendraDetailsResponsePayload> kendraDetailsResponsePayloadList;
	
}
