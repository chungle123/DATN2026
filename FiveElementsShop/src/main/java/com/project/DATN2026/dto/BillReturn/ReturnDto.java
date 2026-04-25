package com.project.DATN2026.dto.BillReturn;

import lombok.Data;

@Data
public class ReturnDto {
    private Long productDetailId;

    //Số lượng khách hàng đổi
    private Integer quantityReturn;
}
