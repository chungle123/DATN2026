package com.project.DATN2026.dto.Statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthInYearStatistic {
    private int month;
    private BigDecimal revenue;
}
