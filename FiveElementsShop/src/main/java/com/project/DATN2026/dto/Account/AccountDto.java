package com.project.DATN2026.dto.Account;

import com.project.DATN2026.dto.AddressShipping.AddressShippingDto;
import lombok.Data;

import java.util.List;

@Data
public class AccountDto {
    private String phoneNumber;
    private String name;
    private String email;
    private String password;
    private Long points;
    private List<AddressShippingDto> addressShippingList;
}
