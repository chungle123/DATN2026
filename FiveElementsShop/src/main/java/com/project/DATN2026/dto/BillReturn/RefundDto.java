package com.project.DATN2026.dto.BillReturn;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefundDto {
    private Long billDetailId;

    //Giá trả lại
    private Double momentPriceRefund;

    //Số lượng khách hàng trả lại
    private Integer quantityRefund;
}
