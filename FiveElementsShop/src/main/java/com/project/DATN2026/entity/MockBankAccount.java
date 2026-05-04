package com.project.DATN2026.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mock_bank_account")
@Getter
@Setter
@NoArgsConstructor
public class MockBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private Double balance;

    // true = tài khoản shop, false = tài khoản khách hàng
    private Boolean isShopAccount;

    public MockBankAccount(String bankName, String accountNumber, String accountHolder, Double balance, Boolean isShopAccount) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.isShopAccount = isShopAccount;
    }
}
