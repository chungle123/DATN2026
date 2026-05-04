package com.project.DATN2026.repository;

import com.project.DATN2026.entity.MockBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockBankAccountRepository extends JpaRepository<MockBankAccount, Long> {
    MockBankAccount findByAccountNumberAndBankName(String accountNumber, String bankName);
    MockBankAccount findByIsShopAccountTrue();
    List<MockBankAccount> findByIsShopAccountFalse();
}
