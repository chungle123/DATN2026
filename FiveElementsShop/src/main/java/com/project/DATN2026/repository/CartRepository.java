package com.project.DATN2026.repository;

import com.project.DATN2026.entity.Cart;
import com.project.DATN2026.entity.Product;
import com.project.DATN2026.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByAccount_Id(Long accountId);
    boolean existsByProductDetail_IdAndAccount_Id(Long productDetailId, Long accountId);
    Cart findByProductDetail_IdAndAccount_Id(Long productDetailId, Long accountId);
    Cart findByProductDetail(ProductDetail productDetail);
    void deleteAllByAccount_Id(Long accountId);

}
