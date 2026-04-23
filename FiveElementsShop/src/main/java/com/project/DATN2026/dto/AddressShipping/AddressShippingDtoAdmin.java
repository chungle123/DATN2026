package com.project.DATN2026.dto.AddressShipping;

import com.project.DATN2026.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressShippingDtoAdmin {
    private Long id;
    private String address;
    private Long customerId;
}
