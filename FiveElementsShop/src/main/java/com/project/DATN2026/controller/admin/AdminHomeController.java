package com.project.DATN2026.controller.admin;

import com.project.DATN2026.dto.Bill.BillDtoInterface;
import com.project.DATN2026.dto.Product.LowStockProductDto;
import com.project.DATN2026.dto.Product.ProductDto;
import com.project.DATN2026.repository.BillRepository;
import com.project.DATN2026.repository.ProductDetailRepository;
import com.project.DATN2026.service.AccountService;
import com.project.DATN2026.service.BillService;
import com.project.DATN2026.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class  AdminHomeController {
    private final BillService billService;
    private final ProductService productService;

    private final BillRepository billRepository;

    private final AccountService accountService;

    private final ProductDetailRepository productDetailRepository;

    private static final int LOW_STOCK_THRESHOLD = 5;

    public AdminHomeController(BillService billService, ProductService productService, BillRepository billRepository, AccountService accountService, ProductDetailRepository productDetailRepository) {
        this.billService = billService;
        this.productService = productService;
        this.billRepository = billRepository;
        this.accountService = accountService;
        this.productDetailRepository = productDetailRepository;
    }

    @GetMapping("/admin")
    public String viewAdminHome(Model model) {
        Page<BillDtoInterface> billDtos = billService.findAll(Pageable.ofSize(10));
        Page<ProductDto> productDtos = productService.getAllProductApi(Pageable.ofSize(10));

        model.addAttribute("billList", billRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createDate"))));
        model.addAttribute("totalBillQuantity", billDtos.getTotalElements());
        model.addAttribute("totalProduct", productDtos.getTotalElements());
        model.addAttribute("revenue", billRepository.calculateTotalRevenue());
        model.addAttribute("totalBillWaiting", billRepository.getTotalBillStatusWaiting());

        // Low Stock Alert
        Long lowStockCount = productDetailRepository.countLowStockProducts(LOW_STOCK_THRESHOLD);
        List<LowStockProductDto> lowStockProducts = productDetailRepository.findLowStockProducts(LOW_STOCK_THRESHOLD);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("lowStockThreshold", LOW_STOCK_THRESHOLD);

        return "/admin/index";
    }
}
