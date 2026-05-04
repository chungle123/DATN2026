package com.project.DATN2026.entity.enumClass;

public enum PaymentStatus {
    PENDING,        // Chờ thanh toán
    PROCESSING,     // Người dùng đã thanh toán, chờ admin duyệt
    APPROVED,       // Admin đã duyệt
    REJECTED        // Admin từ chối
}
