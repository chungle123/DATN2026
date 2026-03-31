package com.project.DATN2026.service;

import com.project.DATN2026.dto.Product.ProductDetailDto;
import com.project.DATN2026.entity.Product;
import com.project.DATN2026.entity.ProductDetail;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductDetailService {
    ProductDetail save(ProductDetail productDetail);

    ProductDetail getProductDetailByProductCode(String code) throws NotFoundException;

    List<ProductDetailDto> getByProductId(Long id) throws com.project.DATN2026.exception.NotFoundException;
}
