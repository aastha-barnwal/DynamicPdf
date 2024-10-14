package com.example.dynamic_pdf.controller;

import com.example.dynamic_pdf.service.PdfService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dynamic_pdf.model.InvoiceRequest;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/generate")
    public ResponseEntity<?> generatePdf(@RequestBody InvoiceRequest request, HttpServletResponse response ) {
        try {
        	 
       
//        	String hash = pdfService.getUniqueFileName(request);
//        	
//            if (reqCookies != null) {
//                for (Cookie cookie : reqCookies) {
//                    if (hash.equals(cookie.getName())) {
//                        return new ResponseEntity<>(cookie.getValue().getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
//                    }
//                }
//            }
        	
            String pdfPath = pdfService.generatePdf(request);
            
            File pdfFile = new File(pdfPath);
            byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfPath);
            
            Cookie cookie = new Cookie(pdfService.getUniqueFileName(request),new String(pdfBytes, StandardCharsets.UTF_8));
            
            
            response.addCookie(cookie);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
//            return new ResponseEntity<>("PDF Generated: " + pdfPath, HttpStatus.OK);
        } catch (IOException | DocumentException e) {
            return new ResponseEntity<>("Error generating PDF: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String filename) throws IOException {
        File pdfFile = new File("generated_pdfs/" + filename);

        if (!pdfFile.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
