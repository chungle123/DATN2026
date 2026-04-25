package com.project.DATN2026.repository;

import com.project.DATN2026.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderId(String orderId);
    Payment findByOrderId(String orderId);

}
