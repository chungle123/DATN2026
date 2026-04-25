package com.project.DATN2026.controller.api;

import com.project.DATN2026.dto.Statistic.*;
import com.project.DATN2026.service.AccountService;
import com.project.DATN2026.service.StatisticService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
public class StatisticRestController {

    private final StatisticService statisticService;

    private final AccountService accountService;

    private final com.project.DATN2026.service.ExcelExportService excelExportService;

    public StatisticRestController(StatisticService statisticService, AccountService accountService, com.project.DATN2026.service.ExcelExportService excelExportService) {
        this.statisticService = statisticService;
        this.accountService = accountService;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/api/get-statistic-revenue-day-in-month")
    private List<DayInMonthStatistic> getDayInMonthStatistic(@RequestParam String month, @RequestParam String year) {
        return statisticService.getDayInMonthStatistic(month, year);
    }

    @GetMapping("/api/statistics/export-excel")
    public ResponseEntity<InputStreamResource> exportExcel(@RequestParam String month, @RequestParam String year) {
        List<DayInMonthStatistic> data = statisticService.getDayInMonthStatistic(month, year);
        ByteArrayInputStream in = excelExportService.exportRevenueToExcel(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=doanh_thu_" + month + "_" + year + ".xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/api/get-statistic-revenue-day-from-time")
    private List<DayInMonthStatistic2> getDayInMonthStatistic2(@RequestParam String fromDate, @RequestParam String toDate) {
        return statisticService.getDailyRevenue2(fromDate, toDate);
    }

    @GetMapping("/api/get-statistic-revenue-month-from-time")
    private List<MonthInYearStatistic2> getMonthlyStatistic(@RequestParam String fromMonth, @RequestParam String toMonth) {
        return statisticService.getMonthlyRevenue(fromMonth, toMonth);
    }


    @GetMapping("/api/get-bestseller-product")
    private List<BestSellerProduct> getBestSellerProductInTime(@RequestParam String fromDate, @RequestParam String toDate) {
        return statisticService.getBestSellerProduct(fromDate, toDate);
    }

    @GetMapping("/api/get-bestseller-product-all")
    private List<BestSellerProduct> getBestSellerProductAll() {
        return statisticService.getBestSellerProductAll();
    }

    @GetMapping("/api/get-statistic-revenue-month-in-year")
    private List<MonthInYearStatistic> getMonthInYearStatistic(@RequestParam String year) {
        return statisticService.getMonthInYearStatistic(year);
    }

    @GetMapping("/api/get-bestseller-product-time")
    private List<BestSellerProduct> getBestSellerProductTime(@RequestParam String fromDate, @RequestParam String toDate) {
        return statisticService.getBestSellerProduct(fromDate, toDate);
    }

    @GetMapping("/get-statistic-user-by-month")
    public List<UserStatistic> getStatisticUserByMonth() {
        List<UserStatistic> userStatistics = accountService.getUserStatistics("2023-01-01", "2023-12-31");
        return  userStatistics;
    }

    @GetMapping("/api/get-statistic-product-time")
    public List<ProductStatistic> getStatisticProductInTime(@RequestParam String fromDate, @RequestParam String toDate) {
        List<ProductStatistic> productStatistics = statisticService.getStatisticProductInTime(fromDate, toDate);
        return  productStatistics;
    }

    @GetMapping("/api/get-statistic-order")
    public List<OrderStatistic> getStatisticOrder() {
        List<OrderStatistic> orderStatisticList = statisticService.getStatisticOrder();
        return orderStatisticList;
    }
}
