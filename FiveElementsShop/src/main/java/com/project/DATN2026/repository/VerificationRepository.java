package com.project.DATN2026.repository;

import com.project.DATN2026.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByCode(String code);
}
