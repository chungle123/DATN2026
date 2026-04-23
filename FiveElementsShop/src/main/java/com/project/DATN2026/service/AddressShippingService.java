package com.project.DATN2026.service;

import com.project.DATN2026.dto.AddressShipping.AddressShippingDto;
import com.project.DATN2026.dto.AddressShipping.AddressShippingDtoAdmin;
import com.project.DATN2026.entity.AddressShipping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressShippingService {
    List<AddressShippingDto> getAddressShippingByAccountId();
    AddressShippingDto saveAddressShippingUser(AddressShippingDto addressShippingDto);

    AddressShippingDto saveAddressShippingAdmin(AddressShippingDtoAdmin addressShippingDto);

    void deleteAddressShipping(Long id);
}
