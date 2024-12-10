package com.janaushadhi.adminservice.controller;

import com.janaushadhi.adminservice.requestpayload.GetDataRequest;
import com.janaushadhi.adminservice.responsepayload.GetAllPharmasict;
import com.janaushadhi.adminservice.serviceimpl.PdfGenerationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/pdf")
public class PdfController {

    @Autowired
    private PdfGenerationService pdfGenerationService;


    @PostMapping("/pharmacistPdfDownload")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestBody GetAllPharmasict getAllPharmasict) {
        Map<String, Object> response = pdfGenerationService.generatePdfAndList(getAllPharmasict);

        ByteArrayInputStream pdfStream = (ByteArrayInputStream) response.get("pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pharmacist.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.MULTIPART_FORM_DATA)
               // .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }


    @PostMapping("/productPdfDownload")
    public ResponseEntity<InputStreamResource> downloadPdfProduct(@RequestBody GetDataRequest getAllProduct) {
        Map<String, Object> response = pdfGenerationService.generatePdfAndListProduct(getAllProduct);

        ByteArrayInputStream pdfStream = (ByteArrayInputStream) response.get("pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Product & MRP List.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                // .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

}
