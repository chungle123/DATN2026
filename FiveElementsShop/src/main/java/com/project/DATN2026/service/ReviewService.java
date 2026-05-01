package com.project.DATN2026.service;

import com.project.DATN2026.entity.Review;

import java.util.List;

public interface ReviewService {
    boolean canReview(Long customerId, Long productId);
    Review saveReview(Review review);
    List<Review> getReviewsByProductId(Long productId);
}
