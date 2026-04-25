package com.project.DATN2026.dto.Order;

import com.project.DATN2026.dto.Product.ProductDetailDto;
import lombok.Data;

@Data
public class OrderDetailDto {
    private Integer quantity;
    private Long productDetailId;
}
