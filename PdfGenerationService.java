package com.janaushadhi.adminservice.serviceimpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.janaushadhi.adminservice.entity.Newproduct;
import com.janaushadhi.adminservice.entity.Pharmacist;
import com.janaushadhi.adminservice.externalservices.AuthService;
import com.janaushadhi.adminservice.externalservices.KendraService;
import com.janaushadhi.adminservice.requestpayload.GetDataRequest;
import com.janaushadhi.adminservice.responsepayload.*;
import com.janaushadhi.adminservice.util.DataConstant;
import com.janaushadhi.adminservice.util.HeaderFooterPageEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class PdfGenerationService {

    @Autowired
    private AuthService authService;



    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private KendraService kendraService;



//        private static final  String headerPath="http://151.106.39.5:8080/Janaushadhi_FILES/1718181377841.png"; //NOSONAR
//    private static final  String footerPath="http://151.106.39.5:8080/Janaushadhi_FILES/1718181410790.png";  //NOSONAR

//    private static final  String headerPath="C:/image/initial_letter_header.png";
//    private static final  String footerPath="C:/image/initial_letter_fotter.png";

        public Map<String, Object> generatePdfAndList(GetAllPharmasict getAllPharmasict) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        List<Pharmacist> dataList = null;
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> allPharmacistWithFilters = authService.getAllPharmacistWithFilters(getAllPharmasict);
            PharmacistResponsePage pharmacistResponsePage = objectMapper.convertValue(allPharmacistWithFilters.get("responseBody"), PharmacistResponsePage.class);
            Integer index= 0;
            //header footer in pdf
           // pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderFooterPageEvent(headerPath, footerPath, 0, 0));
            //pdf heading
            Paragraph heading = new Paragraph("Pharmacist List")
                    .setFontSize(12)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(5)
                    .setMarginBottom(5);
            document.add(heading);


            // Create a table with 10 columns
            Table table = new Table(UnitValue.createPercentArray(new float[]{1/2, 2, 2, 2, 2, 1, 1}));

            // Add table headers
            table.addHeaderCell("Sr.No").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
            table.addHeaderCell("Contact Number").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
            table.addHeaderCell("Name").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
            table.addHeaderCell("Email").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
            table.addHeaderCell("Address").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
//            table.addHeaderCell("state_Name");
            table.addHeaderCell("District").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
            table.addHeaderCell("Pin Code").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
            
            for (PharmacistResponsepayload data : pharmacistResponsePage.getPharmacistResponsepayloadList()) {

                if(getAllPharmasict.getPageIndex() == 0) {
                    data.setSerialNo(index+1);
                    //  table.addCell(data.getSerialNo(index+1));
                    index++;
                }else {
                    data.setSerialNo((getAllPharmasict.getPageSize()*getAllPharmasict.getPageIndex())+(index+1));
                    index++;
                }
                Map<String, Object> districtOfIndiaByDistrictId = kendraService.getDistrictOfIndiaByDistrictId(data.getDistrictId());
                if (data.getDistrictId() != null && data.getDistrictId() != 0) {
                    DistrictOfIndiaResponse districtOfIndiaResponse = objectMapper.convertValue(districtOfIndiaByDistrictId.get("responseBody"), DistrictOfIndiaResponse.class);
                    data.setDistrictName(districtOfIndiaResponse.getDistrictNameInEnglish());
                    data.setStateName(districtOfIndiaResponse.getStateNameInEnglish());
                    
                   // table.addCell(data.getId().toString());
                    table.addCell(new Cell()
                            .add(new Paragraph(String.valueOf(data.getSerialNo())))
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginBottom(8)
                            .setFontSize(8));

                    table.addCell(new Cell()
                            .add(new Paragraph(data.getContactNumber()))
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginBottom(8)
                            .setFontSize(8));

                    table.addCell(new Cell()
                            .add(new Paragraph(data.getName()))
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginBottom(8)
                            .setFontSize(8));

                    table.addCell(new Cell()
                            .add(new Paragraph(data.getEmail()))
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginBottom(8)
                            .setFontSize(8));

                    table.addCell(new Cell()
                            .add(new Paragraph(data.getAddress()))
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginBottom(8)
                            .setFontSize(8));

                    table.addCell(new Cell()
                            .add(new Paragraph(data.getDistrictName()))
                            .setTextAlignment(TextAlignment.LEFT)
                            .setMarginBottom(8)
                            .setFontSize(8));

                    table.addCell(new Cell()
                            .add(new Paragraph(String.valueOf(data.getPinCode())))
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginBottom(8)
                            .setFontSize(8));
//                    table.addCell(String.valueOf(data.getSerialNo())).setTextAlignment(TextAlignment.LEFT).setMarginBottom(8).setFontSize(8);
//                    table.addCell(data.getContactNumber()).setTextAlignment(TextAlignment.CENTER).setMarginBottom(8).setFontSize(8);
//                    table.addCell(data.getName()).setTextAlignment(TextAlignment.LEFT).setMarginBottom(8).setFontSize(8);
//                    table.addCell(data.getEmail()).setTextAlignment(TextAlignment.LEFT).setMarginBottom(8).setFontSize(8);
//                    table.addCell(data.getAddress()).setTextAlignment(TextAlignment.LEFT).setMarginBottom(8).setFontSize(8);
//                   // table.addCell(data.getStateName());
//                    table.addCell(data.getDistrictName()).setTextAlignment(TextAlignment.LEFT).setMarginBottom(8).setFontSize(8);
//                    table.addCell(data.getPinCode()).setTextAlignment(TextAlignment.CENTER).setMarginBottom(8).setFontSize(8);
                }
            }
            document.add(table);

        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
			
        }


        Map<String, Object> response = new HashMap<>();
        response.put("pdf", new ByteArrayInputStream(outputStream.toByteArray()));
        response.put("dataList", dataList);
        return response;
    }



    public Map<String, Object> generatePdfAndListProduct(GetDataRequest getAllProduct) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        List<Newproduct> dataList = null;
        try (
                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> allProduct = productService.getAllProduct(getAllProduct);
            NewProductRPageResponse productresponsePage = objectMapper.convertValue(allProduct.get("responseBody"), NewProductRPageResponse.class);
            //add header and footer

           // pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new HeaderFooterPageEvent(headerPath, footerPath, 0, 0));

            // pdf heading
            Paragraph heading = new Paragraph("Product Portfolio List")
                    .setFontSize(15)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(5)
                     .setMarginBottom(5);
                    document.add(heading);

            // Create a table with 10 columns
            Table table = new Table(UnitValue.createPercentArray(new float[]{1/2, 1, 4, 1, 1}));
//
            // Add table headers
            table.addHeaderCell("Sr.No").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);
           // table.addHeaderCell("product_id");
            table.addHeaderCell("Drug Code").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);;
            table.addHeaderCell("Generic Name").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);;
            table.addHeaderCell("Unit Size").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);;
            table.addHeaderCell("MRP(in Rs.)").getHeader().setBold().setTextAlignment(TextAlignment.LEFT).setMarginTop(8).setFontSize(8);;
           // table.addHeaderCell("Group Name").getHeader().setBold().setTextAlignment(TextAlignment.CENTER).setMarginTop(25);;


            Integer index= 0;


            for (NewProductResponse data : productresponsePage.getNewProductResponsesList()) {

              //  document.add(new Paragraph(data.toString())); // Assuming Pharmacist has a meaningful toString() method
                   // table.addCell(data.getProductId().toString());
                if(getAllProduct.getPageIndex() == 0) {
                    data.setSerialNo(index+1);
                  //  table.addCell(data.getSerialNo(index+1));
                    index++;
                }else {
                    data.setSerialNo((getAllProduct.getPageSize()*getAllProduct.getPageIndex())+(index+1));
                    index++;
                }
                table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getSerialNo())))
                        .setTextAlignment(TextAlignment.LEFT).setMarginBottom(6).setFontSize(8)); // Align left

                table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getDrugCode())))
                        .setTextAlignment(TextAlignment.CENTER).setMarginBottom(6).setFontSize(8)); // Align center

                table.addCell(new Cell().add(new Paragraph(data.getGenericName()))
                        .setTextAlignment(TextAlignment.LEFT).setMarginBottom(6).setFontSize(8)); // Align left

                table.addCell(new Cell().add(new Paragraph(data.getUnitSize()))
                        .setTextAlignment(TextAlignment.LEFT).setMarginBottom(6).setFontSize(8)); // Align left

                table.addCell(new Cell().add(new Paragraph(String.valueOf(data.getMrp())))
                        .setTextAlignment(TextAlignment.CENTER).setMarginBottom(6).setFontSize(8)); // Align center
//                    table.addCell(String.valueOf(data.getSerialNo())).setMarginBottom(6).setFontSize(8);
//                    table.addCell(String.valueOf(data.getDrugCode())).setMarginBottom(6).setFontSize(8);
//                    table.addCell(data.getGenericName()).setMarginBottom(6).setFontSize(8);
//                    table.addCell(data.getUnitSize()).setMarginBottom(6).setFontSize(8);
//                    table.addCell(String.valueOf(data.getMrp())).setMarginBottom(6).setFontSize(8);
                   // table.addCell(data.getGroupName()).setTextAlignment(TextAlignment.CENTER).setMarginBottom(25);

            }
            document.add(table);

        } catch (Exception e) {
        	log.info(DataConstant.SERVER_MESSAGE, e.getMessage());
        }


        Map<String, Object> response = new HashMap<>();
        response.put("pdf", new ByteArrayInputStream(outputStream.toByteArray()));
        response.put("dataList", dataList);
        return response;
    }
}

















