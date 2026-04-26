package com.project.DATN2026.controller.admin;

import com.project.DATN2026.dto.BillReturn.*;
import com.project.DATN2026.entity.Bill;
import com.project.DATN2026.repository.BillDetailRepository;
import com.project.DATN2026.repository.BillRepository;
import com.project.DATN2026.service.BillReturnService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayInputStream;
import com.project.DATN2026.service.ExcelExportService;

@Controller
public class BillReturnController {
    private final BillReturnService billReturnService;
    private final BillDetailRepository billDetailRepository;
    private final ExcelExportService excelExportService;

    public BillReturnController(BillReturnService billReturnService, BillRepository billRepository, BillDetailRepository billDetailRepository, ExcelExportService excelExportService) {
        this.billReturnService = billReturnService;
        this.billDetailRepository = billDetailRepository;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/admin-only/bill-return")
    public String viewBillReturnPage(SearchBillReturnDto searchBillReturnDto, Model model) {
        List<BillReturnDto> billReturnList = billReturnService.getAllBillReturns(searchBillReturnDto);
        model.addAttribute("returnList", billReturnList);
        return "admin/bill-return";
    }

    @GetMapping("/admin-only/bill-return-create")
    public String viewBillReturnCreatePage(Model model) {

        return "admin/bill-return-create";
    }

    @GetMapping("/admin-only/bill-return-detail/{id}")
    public String viewBillReturnDetailPage(Model model, @PathVariable Long id) {
       BillReturnDetailDto billReturnDetailDto = billReturnService.getBillReturnDetailById(id);

       Double total = Double.valueOf(0);

        for (RefundProductDto refundProductDto:
                billReturnDetailDto.getRefundProductDtos()) {
            total += refundProductDto.getMomentPriceRefund() * refundProductDto.getQuantityRefund();
        }

        Double totalReturn = Double.valueOf(0);

        for (ReturnProductDto returnProductDto:
                billReturnDetailDto.getReturnProductDtos()) {
            totalReturn += returnProductDto.getMomentPriceExchange() * returnProductDto.getQuantityReturn();
        }

       model.addAttribute("total", total);
        model.addAttribute("totalReturn", totalReturn);
       model.addAttribute("billReturnDetail", billReturnDetailDto);

        return "admin/bill-return-detail";
    }

    @GetMapping("/admin-only/bill-return-detail-code/{code}")
    public String viewBillReturnDetailPageByCode(Model model, @PathVariable String code) {
        BillReturnDetailDto billReturnDetailDto = billReturnService.getBillReturnDetailByCode(code);

        Double total = Double.valueOf(0);

        for (RefundProductDto refundProductDto:
                billReturnDetailDto.getRefundProductDtos()) {
            total += refundProductDto.getMomentPriceRefund() * refundProductDto.getQuantityRefund();
        }

        Double totalReturn = Double.valueOf(0);

        for (ReturnProductDto returnProductDto:
                billReturnDetailDto.getReturnProductDtos()) {
            totalReturn += returnProductDto.getMomentPriceExchange() * returnProductDto.getQuantityReturn();
        }

        model.addAttribute("total", total);
        model.addAttribute("totalReturn", totalReturn);
        model.addAttribute("billReturnDetail", billReturnDetailDto);

        return "admin/bill-return-detail";
    }


    @GetMapping("/admin/bill-return-detail-generate/{id}")
    public String generateHtmlPrint(Model model, @PathVariable Long id) {
        BillReturnDetailDto billReturnDetailDto = billReturnService.getBillReturnDetailById(id);

        Double total = Double.valueOf(0);

        for (RefundProductDto refundProductDto:
                billReturnDetailDto.getRefundProductDtos()) {
            total += refundProductDto.getMomentPriceRefund() * refundProductDto.getQuantityRefund();
        }

        Double totalReturn = Double.valueOf(0);

        for (ReturnProductDto returnProductDto:
                billReturnDetailDto.getReturnProductDtos()) {
            totalReturn += returnProductDto.getMomentPriceExchange() * returnProductDto.getQuantityReturn();
        }

        model.addAttribute("total", total);
        model.addAttribute("totalReturn", totalReturn);
        model.addAttribute("billReturnDetail", billReturnDetailDto);

        return "admin/invoice-return-print";
    }

    @PostMapping("/admin/update-bill-return-status")
    public String updateBillReturnStatus(@ModelAttribute("billReturnDto") BillReturnDto billReturnDto, Model model, RedirectAttributes redirectAttributes) {

        try {
            BillReturnDto updatedBillReturn = billReturnService.updateStatus(billReturnDto.getId(), billReturnDto.getReturnStatus());
            redirectAttributes.addFlashAttribute("message", "Đơn đổi trả " + updatedBillReturn.getCode() + " cập nhật trạng thái thành công!");
        } catch (Exception e) {
            model.addAttribute("message", "Error updating status");
        }
        return "redirect:/admin-only/bill-return";
    }

    @ResponseBody
    @PostMapping("/api/bill-return")
    public BillReturnDto createBillReturn(@RequestBody BillReturnCreateDto billReturnCreateDto) {
        return billReturnService.createBillReturn(billReturnCreateDto);
    }

    @GetMapping("/admin-only/export-bill-returns")
    public ResponseEntity<InputStreamResource> exportBillReturns(SearchBillReturnDto searchBillReturnDto) {
        List<BillReturnDto> billReturnList = billReturnService.getAllBillReturns(searchBillReturnDto);
        ByteArrayInputStream in = excelExportService.exportReturnsToExcel(billReturnList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=quanlyhoadon.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
}
