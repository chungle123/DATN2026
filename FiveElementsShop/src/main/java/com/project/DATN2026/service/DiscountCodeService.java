package com.project.DATN2026.service;


import com.project.DATN2026.dto.DiscountCode.DiscountCodeDto;
import com.project.DATN2026.dto.DiscountCode.SearchDiscountCodeDto;
import com.project.DATN2026.dto.Product.SearchProductDto;
import com.project.DATN2026.entity.DiscountCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscountCodeService {
    Page<DiscountCodeDto> getAllDiscountCode(SearchDiscountCodeDto searchDiscountCodeDto, Pageable pageable);
    DiscountCodeDto saveDiscountCode(DiscountCodeDto discountCodeDto);
    DiscountCodeDto updateDiscountCode(DiscountCodeDto discountCodeDto);

    DiscountCodeDto getDiscountCodeById(Long id);
    DiscountCodeDto getDiscountCodeByCode(Long code);
    DiscountCodeDto updateStatus(Long discountCodeId, int status);
    Page<DiscountCodeDto> getAllAvailableDiscountCode(Pageable pageable);
}
