package com.project.DATN2026.dto.Bill;

import com.project.DATN2026.entity.enumClass.BillStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchBillDto {
    private String keyword;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private BillStatus billStatus;
}
