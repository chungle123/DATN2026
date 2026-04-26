package com.project.DATN2026.service;

import com.project.DATN2026.dto.Statistic.DayInMonthStatistic;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {
    public ByteArrayInputStream exportRevenueToExcel(List<DayInMonthStatistic> dataList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Doanh Thu");

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Ngày", "Doanh Thu (VND)" };
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
            }

            // Data Rows
            int rowIdx = 1;
            for (DayInMonthStatistic stat : dataList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stat.getDate());
                if (stat.getRevenue() != null) {
                    row.createCell(1).setCellValue(stat.getRevenue().doubleValue());
                } else {
                    row.createCell(1).setCellValue(0);
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to import data to Excel file: " + e.getMessage());
        }
    }
}
