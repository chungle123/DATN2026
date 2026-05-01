package com.project.DATN2026.controller.user;


import com.project.DATN2026.dto.AddressShipping.AddressShippingDto;
import com.project.DATN2026.dto.Cart.CartDto;
import com.project.DATN2026.dto.DiscountCode.DiscountCodeDto;
import com.project.DATN2026.entity.Account;
import com.project.DATN2026.entity.Customer;
import com.project.DATN2026.exception.NotFoundException;
import com.project.DATN2026.repository.CustomerRepository;
import com.project.DATN2026.service.AddressShippingService;
import com.project.DATN2026.service.BillService;
import com.project.DATN2026.service.CartService;
import com.project.DATN2026.service.DiscountCodeService;
import com.project.DATN2026.utils.UserLoginUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ShoppingCartController {
    private final CartService cartService;

    private final BillService billService;

    private final DiscountCodeService discountCodeService;

    private final AddressShippingService addressShippingService;

    private final CustomerRepository customerRepository;

    public ShoppingCartController(CartService cartService, BillService billService, DiscountCodeService discountCodeService, AddressShippingService addressShippingService, CustomerRepository customerRepository) {
        this.cartService = cartService;
        this.billService = billService;
        this.discountCodeService = discountCodeService;
        this.addressShippingService = addressShippingService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/shoping-cart")
    public String viewShoppingCart(Model model) {
        List<CartDto> cartDtoList = cartService.getAllCartByAccountId();
        Page<DiscountCodeDto> discountCodeList = discountCodeService.getAllAvailableDiscountCode(PageRequest.of(0, 15));
        List<AddressShippingDto> addressShippingDtos = addressShippingService.getAddressShippingByAccountId();
        model.addAttribute("discountCodes", discountCodeList.getContent());
        model.addAttribute("addressShippings", addressShippingDtos);
        model.addAttribute("carts", cartDtoList);
        Long points = 0L;
        Account currentAccount = UserLoginUtil.getCurrentLogin();
        if (currentAccount != null) {
            // Query trực tiếp từ DB để tránh lỗi LazyInitializationException
            Customer customerOpt = customerRepository.findByAccount_Id(currentAccount.getId());
            if (customerOpt != null && customerOpt.getPoints() != null) {
                points = customerOpt.getPoints();
            }
        }
        model.addAttribute("customerPoints", points);
        return "user/shoping-cart";
    }

    @ResponseBody
    @PostMapping("/api/addToCart")
    public void addToCart(@RequestBody CartDto cartDto) throws NotFoundException {
        cartService.addToCart(cartDto);
    }

    @ResponseBody
    @PostMapping("/api/deleteCart/{id}")
    public void deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
    }

    @ResponseBody
    @PostMapping("/api/updateCart")
    public void updateCart(@RequestBody CartDto cartDto) throws NotFoundException {
        cartService.updateCart(cartDto);
    }

}

