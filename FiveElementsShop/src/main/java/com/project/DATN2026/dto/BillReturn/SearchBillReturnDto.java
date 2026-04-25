package com.project.DATN2026.dto.BillReturn;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchBillReturnDto {
    private String fromDate;
    private String toDate;
    private String returnStatus;
}
