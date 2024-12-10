package com.janaushadhi.adminservice.service;

import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.WhatsNewAndNewsLatterRequestPayload;
import com.janaushadhi.adminservice.responsepayload.GetAllByNews;

public interface WhatsNewAndNewsLatterService {

	Map<String, Object> addWhatsNewAndNewsLatter(WhatsNewAndNewsLatterRequestPayload requestPayload);

	Map<String, Object> getAllByType(GetAllByNews getAllByNews);

	Map<String, Object> getWhatsNewAndNewsLatterById(Long id);

}
