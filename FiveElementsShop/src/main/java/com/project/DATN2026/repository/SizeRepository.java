package com.project.DATN2026.repository;

import com.project.DATN2026.entity.Color;
import com.project.DATN2026.entity.Product;
import com.project.DATN2026.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {
    @Query(value = "select distinct s from Size s join ProductDetail pd on s.id = pd.size.id where pd.product = :product")
    List<Size> findSizesByProduct(Product product);

    @Query(value = "select distinct s from Size s join ProductDetail pd on s.id = pd.size.id where pd.product = :product and pd.color = :color")
    List<Size> findSizesByProductAndColor(Product product, Color color);

    boolean existsByCode(String code);

    List<Size> findAllByDeleteFlagFalse();
    Page<Size> findAllByDeleteFlagFalse(Pageable pageable);

    boolean existsByCodeAndDeleteFlagFalse(String code);

    Size findByCodeAndDeleteFlagTrue(String code);
}