package com.example.dynamic_pdf.service;

	import com.example.dynamic_pdf.model.InvoiceRequest;
	import com.itextpdf.text.DocumentException;
	import org.junit.jupiter.api.Test;
	import org.springframework.boot.test.context.SpringBootTest;

	import java.io.File;
	import java.io.IOException;
	import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

	@SpringBootTest

public class PdfServiceTest {
	    private final PdfService pdfService = new PdfService();

	    @Test
	    public void testGeneratePdf() throws IOException, DocumentException {
	        InvoiceRequest request = new InvoiceRequest();
	        request.setSeller("Seller A");
	        request.setSellerGstin("GSTIN123");
	        request.setBuyer("Buyer B");
	        request.setBuyerGstin("GSTIN456");

	        InvoiceRequest.Item item = new InvoiceRequest.Item("Item 1", 10, 50.0);
	        request.setItems(List.of(item));

	        String pdfPath = pdfService.generatePdf(request);

	        // Verify that the file is created
	        File pdfFile = new File(pdfPath);
	        assertTrue(pdfFile.exists());

	        // Verify PDF content (simple size check or checksum)
	        byte[] pdfBytes = Files.readAllBytes(pdfFile.toPath());
	        assertTrue(pdfBytes.length > 0);
	    }
	}

}
