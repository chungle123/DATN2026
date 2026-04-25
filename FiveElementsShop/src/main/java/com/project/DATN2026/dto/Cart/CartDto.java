package com.project.DATN2026.dto.Cart;

import com.project.DATN2026.dto.Product.ProductDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Long accountId;
    private ProductCart product;
    private ProductDetailDto detail;

    @NotNull
    private int quantity;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

}

