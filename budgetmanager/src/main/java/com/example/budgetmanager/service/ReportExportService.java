package com.example.budgetmanager.service;

import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.model.IncomeRecord;
import com.lowagie.text.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

// Sadece gerekli com.lowagie.text sınıflarını tek tek import ettik
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReportExportService {

    public ByteArrayInputStream exportExpensesToPDF(List<ExpenseRecord> expenses) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            com.lowagie.text.Font headerFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD);
            Paragraph title = new Paragraph("Expense Report", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 3, 3, 2});

            table.addCell("Date");
            table.addCell("Category");
            table.addCell("Subcategory");
            table.addCell("Amount");

            for (ExpenseRecord e : expenses) {
                table.addCell(e.getDate().toString());
                table.addCell(e.getCategory().getName());
                table.addCell(e.getSubCategory() != null ? e.getSubCategory().getName() : "-");
                table.addCell(String.valueOf(e.getAmount()));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportExpensesToExcel(List<ExpenseRecord> expenses) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Expenses");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Category");
            header.createCell(2).setCellValue("Subcategory");
            header.createCell(3).setCellValue("Amount");

            int rowNum = 1;
            for (ExpenseRecord e : expenses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(e.getDate().toString());
                row.createCell(1).setCellValue(e.getCategory().getName());
                row.createCell(2).setCellValue(e.getSubCategory() != null ? e.getSubCategory().getName() : "-");
                row.createCell(3).setCellValue(e.getAmount());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportIncomesToPDF(List<IncomeRecord> incomes) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Paragraph title = new Paragraph("Income Report", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 3, 2});

            table.addCell("Date");
            table.addCell("Category");
            table.addCell("Amount");

            for (IncomeRecord i : incomes) {
                table.addCell(i.getDate().toString());
                table.addCell(i.getCategory().getName());
                table.addCell(String.valueOf(i.getAmount()));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportIncomesToExcel(List<IncomeRecord> incomes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Incomes");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Category");
            header.createCell(2).setCellValue("Amount");

            int rowNum = 1;
            for (IncomeRecord i : incomes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(i.getDate().toString());
                row.createCell(1).setCellValue(i.getCategory().getName());
                row.createCell(2).setCellValue(i.getAmount());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}
