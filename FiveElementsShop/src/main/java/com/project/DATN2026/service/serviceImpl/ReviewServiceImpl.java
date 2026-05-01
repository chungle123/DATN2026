package com.project.DATN2026.service.serviceImpl;

import com.project.DATN2026.entity.Product;
import com.project.DATN2026.entity.Review;
import com.project.DATN2026.repository.ProductRepository;
import com.project.DATN2026.repository.ReviewRepository;
import com.project.DATN2026.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @Override
    public boolean canReview(Long customerId, Long productId) {
        Long completedPurchases = reviewRepository.countCompletedPurchases(customerId, productId);
        Long existingReviews = reviewRepository.countReviewsByCustomerAndProduct(customerId, productId);
        
        // Mua bao nhiêu lần thì đánh giá bấy nhiêu lần
        return completedPurchases > existingReviews;
    }

    @Override
    public Review saveReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        updateProductAverageRating(review.getProduct().getId());
        return savedReview;
    }

    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    private void updateProductAverageRating(Long productId) {
        Double avgRating = reviewRepository.calculateAverageRating(productId);
        if (avgRating == null) {
            avgRating = 0.0;
        }
        
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            // Round to 1 decimal place (e.g. 4.5)
            avgRating = Math.round(avgRating * 10.0) / 10.0;
            product.setAverageRating(avgRating);
            productRepository.save(product);
        }
    }
}
