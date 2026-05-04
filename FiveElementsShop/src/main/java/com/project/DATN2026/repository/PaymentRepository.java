package com.project.DATN2026.repository;

import com.project.DATN2026.entity.Payment;
import com.project.DATN2026.entity.enumClass.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderId(String orderId);
    Payment findByOrderId(String orderId);
    Payment findByBill_Id(Long billId);
    List<Payment> findByPaymentStatusOrderByPaymentDateDesc(PaymentStatus status);
    List<Payment> findByPaymentStatusInOrderByPaymentDateDesc(List<PaymentStatus> statuses);
}

