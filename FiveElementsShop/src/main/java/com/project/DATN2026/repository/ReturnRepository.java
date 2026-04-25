package com.project.DATN2026.repository;

import com.project.DATN2026.entity.BillReturn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnRepository extends JpaRepository<BillReturn, Long> {
}
