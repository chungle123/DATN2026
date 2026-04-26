package com.project.DATN2026.service;

import com.project.DATN2026.dto.Statistic.DayInMonthStatistic;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.time.format.DateTimeFormatter;
import com.project.DATN2026.dto.BillReturn.BillReturnDto;

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

    public ByteArrayInputStream exportReturnsToExcel(List<BillReturnDto> dataList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Danh Sach Doi Tra");

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Mã đổi trả", "Thời gian", "Khách hàng", "Tiền trả khách (VND)", "Lý do", "Trạng thái" };
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                
                // Add simple style for header
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                cell.setCellStyle(headerStyle);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            // Data Rows
            int rowIdx = 1;
            for (BillReturnDto stat : dataList) {
                Row row = sheet.createRow(rowIdx++);
                
                // Mã đổi trả
                row.createCell(0).setCellValue(stat.getCode() != null ? stat.getCode() : "");
                
                // Thời gian
                String dateStr = stat.getReturnDate() != null ? stat.getReturnDate().format(formatter) : "";
                row.createCell(1).setCellValue(dateStr);
                
                // Khách hàng
                String customerName = stat.getCustomer() != null ? stat.getCustomer().getName() : "Khách lẻ";
                row.createCell(2).setCellValue(customerName);
                
                // Tiền trả khách
                double returnMoney = stat.getReturnMoney() != null ? stat.getReturnMoney() : 0.0;
                row.createCell(3).setCellValue(returnMoney);
                
                // Lý do
                String reason = (stat.getReturnReason() != null && !stat.getReturnReason().isEmpty()) ? stat.getReturnReason() : "Không có";
                row.createCell(4).setCellValue(reason);
                
                // Trạng thái
                String statusStr = "";
                switch (stat.getReturnStatus()) {
                    case 0: statusStr = "Chờ xác nhận"; break;
                    case 1: statusStr = "Chờ lấy hàng"; break;
                    case 2: statusStr = "Chờ giao hàng"; break;
                    case 3: statusStr = "Hoàn thành"; break;
                    case 4: statusStr = "Đã hủy"; break;
                    default: statusStr = "Không xác định"; break;
                }
                row.createCell(5).setCellValue(statusStr);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to import data to Excel file: " + e.getMessage());
        }
    }
}
