package com.drivncook.util;

import com.drivncook.model.LoyaltyCard;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class PdfUtil {
    public static void exportLoyaltyCardToPdf(LoyaltyCard card, String filePath) {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            doc.add(new Paragraph("Carte de Fidélité"));
            doc.add(new Paragraph("ID: " + card.getId()));
            doc.add(new Paragraph("Points: " + card.getPoints()));
            doc.add(new Paragraph("Avantages: " + card.getAdvantages()));
            doc.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
