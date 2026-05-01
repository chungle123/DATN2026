package com.project.DATN2026.repository;

import com.project.DATN2026.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double calculateAverageRating(Long productId);

    @Query("SELECT COUNT(bd) FROM BillDetail bd JOIN bd.bill b JOIN bd.productDetail pd WHERE b.customer.id = :customerId AND pd.product.id = :productId AND b.status = com.project.DATN2026.entity.enumClass.BillStatus.HOAN_THANH")
    Long countCompletedPurchases(Long customerId, Long productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.customer.id = :customerId AND r.product.id = :productId")
    Long countReviewsByCustomerAndProduct(Long customerId, Long productId);
}
