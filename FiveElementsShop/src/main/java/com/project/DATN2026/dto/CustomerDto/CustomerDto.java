package com.project.DATN2026.dto.CustomerDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String code;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private Long points;
}
