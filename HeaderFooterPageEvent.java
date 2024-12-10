package com.janaushadhi.adminservice.util;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class HeaderFooterPageEvent implements IEventHandler {
    private String headerPath;
    private String footerPath;
    private float topPadding;
    private float bottomPadding;

    public HeaderFooterPageEvent(String headerPath, String footerPath, float topPadding, float bottomPadding) {
        this.headerPath = headerPath;
        this.footerPath = footerPath;
        this.topPadding = topPadding;
        this.bottomPadding = bottomPadding;
    }


    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();

        try {
            // Create a Document object
            Document document = new Document(pdfDoc);

            // Retrieve the page size
            Rectangle pageSize = pdfDoc.getDefaultPageSize();

            // Create Image objects for header and footer
            Image header = new Image(ImageDataFactory.create(headerPath));
            Image footer = new Image(ImageDataFactory.create(footerPath));

            // Scale images to fit the page size
            header.scaleToFit(pageSize.getWidth(), pageSize.getHeight() * 0.1f); // Adjust scale factor as needed
            footer.scaleToFit(pageSize.getWidth(), pageSize.getHeight() * 0.1f); // Adjust scale factor as needed

            // Set fixed positions for header and footer with padding
            header.setFixedPosition(0, pageSize.getHeight() - header.getImageScaledHeight() - topPadding);
            footer.setFixedPosition(0, bottomPadding);

            // Add images to the document
            document.add(header);
            document.add(footer);
        } catch (IOException e) {
            log.error("header footer error",e.getMessage());
        }
    }
}
