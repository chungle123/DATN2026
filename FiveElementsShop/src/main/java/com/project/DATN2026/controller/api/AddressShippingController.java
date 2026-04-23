package com.project.DATN2026.controller.api;

import com.project.DATN2026.dto.AddressShipping.AddressShippingDto;
import com.project.DATN2026.entity.AddressShipping;
import com.project.DATN2026.service.AddressShippingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AddressShippingController {

    private final AddressShippingService addressShippingService;

    public AddressShippingController(AddressShippingService addressShippingService) {
        this.addressShippingService = addressShippingService;
    }

    @ResponseBody
    @PostMapping("api/public/addressShipping")
    public ResponseEntity<AddressShippingDto> createAddressShipping(@RequestBody AddressShippingDto addressShippingDto){
        return ResponseEntity.ok(addressShippingService.saveAddressShippingUser(addressShippingDto));
    }

    @ResponseBody
    @DeleteMapping("/api/deleteAddress/{id}")
    public void deleteAddressShipping(@PathVariable Long id) {
        addressShippingService.deleteAddressShipping(id);
    }
}
