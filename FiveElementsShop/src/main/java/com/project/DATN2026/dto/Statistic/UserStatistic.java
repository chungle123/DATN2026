package com.project.DATN2026.dto.Statistic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatistic {
    private String month;
    private int userCount;

}
