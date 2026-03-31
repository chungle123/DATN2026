package com.project.DATN2026.service;

import com.project.DATN2026.dto.ProductDiscount.ProductDiscountCreateDto;
import com.project.DATN2026.dto.ProductDiscount.ProductDiscountDto;
import com.project.DATN2026.entity.ProductDiscount;

import java.util.List;

public interface ProductDiscountService {
    List<ProductDiscount> getAllProductDiscount();

    ProductDiscountDto updateCloseProductDiscount(Long discountId, boolean closed);

    List<ProductDiscountDto> createProductDiscountMultiple(ProductDiscountCreateDto productDiscountCreateDto);
}
