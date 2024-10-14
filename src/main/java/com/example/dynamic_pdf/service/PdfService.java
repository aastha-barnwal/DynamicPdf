package com.example.dynamic_pdf.service;

import com.example.dynamic_pdf.model.InvoiceRequest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class PdfService {

    private static final String PDF_STORAGE = "generated_pdfs/";

    public String generatePdf(InvoiceRequest request) throws IOException, DocumentException {
        // Ensure directory exists
        Files.createDirectories(Paths.get(PDF_STORAGE));

        String fileName = getUniqueFileName(request);
        File pdfFile = new File(PDF_STORAGE + fileName);

        // Check if file already exists
        if (pdfFile.exists()) {
            return pdfFile.getAbsolutePath();
        }

        // Create new PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        // Add content to the document
        document.add(new Paragraph("Invoice for: " + request.getSeller()));
        document.add(new Paragraph("Seller GSTIN: " + request.getSellerGstin()));
        document.add(new Paragraph("Buyer: " + request.getBuyer()));
        document.add(new Paragraph("Buyer GSTIN: " + request.getBuyerGstin()));
        document.add(new Paragraph("Items:"));

        // Add items to the document
        for (InvoiceRequest.Item item : request.getItems()) {
            document.add(new Paragraph(item.getName() + " - " + item.getQuantity() + " @ " + item.getRate()));
        }

        document.close();
        return pdfFile.getAbsolutePath();
    }

    public String getUniqueFileName(InvoiceRequest request) {
        // Create a unique file name based on hash of request data
        return Objects.hash(request.getSeller(), request.getBuyer(), request.getItems()) + ".pdf";
    }
}
