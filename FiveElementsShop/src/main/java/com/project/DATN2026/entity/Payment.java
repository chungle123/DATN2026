package com.project.DATN2026.entity;

import com.project.DATN2026.entity.enumClass.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String amount;
    private String orderStatus;
    private LocalDateTime paymentDate;
    private Integer statusExchange;

    // Thông tin ngân hàng cho thanh toán online
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private String transactionContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @OneToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;
}
