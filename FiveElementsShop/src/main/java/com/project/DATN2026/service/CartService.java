package com.project.DATN2026.service;

import com.project.DATN2026.dto.Cart.CartDto;
import com.project.DATN2026.dto.Order.OrderDto;
import com.project.DATN2026.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
//    Page<Cart> carts(Pageable pageable);
    List<CartDto> getAllCart();
    List<CartDto> getAllCartByAccountId();
    void addToCart(CartDto cartDto) throws NotFoundException;

    void updateCart(CartDto cartDto) throws NotFoundException;

    void orderUser(OrderDto orderDto);
    OrderDto orderAdmin(OrderDto orderDto);

    void deleteCart(Long id);
}
