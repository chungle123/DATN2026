package com.project.DATN2026.dto.BillReturn;

import com.project.DATN2026.dto.Product.ProductDetailDto;
import com.project.DATN2026.dto.Product.ProductDto;
import lombok.Data;

@Data
public class RefundProductDto {
    private String productName;
    private Long productDetailId;
    private String size;
    private String color;

    //Giá trả lại
    private Double momentPriceRefund;

    //Số lượng khách hàng trả lại
    private Integer quantityRefund;
}
