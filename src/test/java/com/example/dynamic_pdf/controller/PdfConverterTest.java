package com.example.dynamic_pdf.controller;

import com.example.dynamic_pdf.model.InvoiceRequest;
import com.example.dynamic_pdf.service.PdfService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

@SpringBootTest
@AutoConfigureMockMvc
public class PdfConverterTest {



	    @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private PdfService pdfService;

	    @Test
	    public void testGeneratePdf() throws Exception {
	        InvoiceRequest request = new InvoiceRequest();
	        request.setSeller("Seller A");
	        request.setSellerGstin("GSTIN123");
	        request.setBuyer("Buyer B");
	        request.setBuyerGstin("GSTIN456");

	        // Mock the PDF generation
	        Mockito.when(pdfService.generatePdf(Mockito.any())).thenReturn("generated_pdfs/invoice123.pdf");

	        mockMvc.perform(post("/api/pdf/generate")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{\"seller\":\"Seller A\",\"sellerGstin\":\"GSTIN123\",\"buyer\":\"Buyer B\",\"buyerGstin\":\"GSTIN456\",\"items\":[]}")
	        )
	                .andExpect(status().isOk())
	                .andExpect(header().exists("Content-Disposition"))
	                .andExpect(cookie().exists("invoice123.pdf"));
	    }
	}
