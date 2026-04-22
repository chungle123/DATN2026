package com.project.DATN2026.controller.admin;


import com.project.DATN2026.entity.Color;
import com.project.DATN2026.entity.Image;
import com.project.DATN2026.entity.Product;
import com.project.DATN2026.entity.Size;
import com.project.DATN2026.service.ColorService;
import com.project.DATN2026.service.ImageService;
import com.project.DATN2026.service.ProductDetailService;
import com.project.DATN2026.service.ProductService;
import com.project.DATN2026.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductDetailController {

    private Product productInLine;
    private final List<Image> imageList = new ArrayList<>();
    private long idImage;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SizeService sizeService;
    @Autowired
    private ColorService colorService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/admin/chi-tiet-san-pham/{code}")
    public String getProductDetailPage(@PathVariable String code, Model model) {
        Product product = productService.getProductByCode(code);
        if(product != null) {
            model.addAttribute("product", product);
            model.addAttribute("productDetails", product.getProductDetails());
            return "admin/product-detail";
        }

        return "error/404";
    }

    @ModelAttribute("listSize")
    public List<Size> getSize() {
        return sizeService.getAll();
    }

    @ModelAttribute("listColor")
    public List<Color> getColor() {
        return colorService.findAll();
    }
}
