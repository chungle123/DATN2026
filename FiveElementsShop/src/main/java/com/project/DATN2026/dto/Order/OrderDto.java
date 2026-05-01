package com.project.DATN2026.dto.Order;

import com.project.DATN2026.dto.CustomerDto.CustomerDto;
import com.project.DATN2026.entity.enumClass.BillStatus;
import com.project.DATN2026.entity.enumClass.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String billId;
    private CustomerDto customer;
    private InvoiceType invoiceType;
    private BillStatus billStatus;
    private Long paymentMethodId;
    private String billingAddress;
    private double promotionPrice;
    private Long voucherId;
    private String orderId;
    private Long pointsUsed = 0L;
    private List<OrderDetailDto> orderDetailDtos;
}
