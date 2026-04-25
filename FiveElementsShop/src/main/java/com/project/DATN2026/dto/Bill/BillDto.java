package com.project.DATN2026.dto.Bill;

import com.project.DATN2026.dto.CustomerDto.CustomerDto;
import com.project.DATN2026.entity.enumClass.BillStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class BillDto {
    private Long id;
    private String code;
    private double promotionPrice;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private BillStatus status;
    private Boolean returnStatus;
    private CustomerDto customer;
    private Double totalAmount;
}
