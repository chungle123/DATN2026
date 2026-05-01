package com.project.DATN2026.repository;

import com.project.DATN2026.entity.Product;
import com.project.DATN2026.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    Page<ProductDetail> getProductDetailsByProductId(Long id, Pageable pageable);

    ProductDetail getProductDetailByProduct(Product product);
    List<ProductDetail> getProductDetailByProductId(Long productId);

    ProductDetail findByBarcodeContainingIgnoreCase(String barcode);

    boolean existsByBarcode(String barcode);

    @Query("SELECT pd.id as productDetailId, p.code as maSanPham, p.name as tenSanPham, " +
            "s.name as kichCo, c.name as mauSac, pd.quantity as soLuongTon " +
            "FROM ProductDetail pd " +
            "JOIN pd.product p " +
            "JOIN pd.size s " +
            "JOIN pd.color c " +
            "WHERE pd.quantity <= :threshold AND p.deleteFlag = false AND p.status = 1 " +
            "ORDER BY pd.quantity ASC")
    List<com.project.DATN2026.dto.Product.LowStockProductDto> findLowStockProducts(int threshold);

    @Query("SELECT COUNT(pd) FROM ProductDetail pd JOIN pd.product p WHERE pd.quantity <= :threshold AND p.deleteFlag = false AND p.status = 1")
    Long countLowStockProducts(int threshold);
}