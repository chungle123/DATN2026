package com.project.DATN2026.dto;

import com.project.DATN2026.dto.Product.ProductDetailDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckOrderDto {
    private Long productDetailId;
    private String quantity;
}
