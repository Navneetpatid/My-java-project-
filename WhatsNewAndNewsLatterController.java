package com.janaushadhi.adminservice.controller;

import java.util.Map;

import com.janaushadhi.adminservice.requestpayload.WhatsNewAndNewsLatterRequestPayload;
import com.janaushadhi.adminservice.responsepayload.GetAllByNews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.janaushadhi.adminservice.service.WhatsNewAndNewsLatterService;

@RestController
@RequestMapping("/api/v1/admin/whatsNewAndNewsLatter")
public class WhatsNewAndNewsLatterController {
	@Autowired
	private WhatsNewAndNewsLatterService whatsNewAndNewsLatterService;
	
	@PostMapping(value = "/addWhatsNewAndNewsLatter")
    public Map<String, Object> addWhatsNewAndNewsLatter(@RequestBody WhatsNewAndNewsLatterRequestPayload requestPayload) {
        return  whatsNewAndNewsLatterService.addWhatsNewAndNewsLatter(requestPayload) ;
    }
	
	@PostMapping(value = "/getAllByType")
    public Map<String, Object> getAllByType(@RequestBody GetAllByNews getAllByNews) {
        return  whatsNewAndNewsLatterService.getAllByType(getAllByNews) ;
    }
	
	@PostMapping(value = "/getWhatsNewAndNewsLatterById")
    public Map<String, Object> getWhatsNewAndNewsLatterById(Long id) {
        return  whatsNewAndNewsLatterService.getWhatsNewAndNewsLatterById(id) ;
    }
}
