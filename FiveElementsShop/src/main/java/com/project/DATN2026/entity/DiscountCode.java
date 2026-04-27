package com.project.DATN2026.entity;

import com.project.DATN2026.entity.enumClass.DiscountCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "discount_code")
public class DiscountCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    private String code;

    @Nationalized
    private String detail;
    private int type;

    @Column(nullable = true)
    private Integer maximumAmount;

    @Column(nullable = true)
    private Integer percentage;
    private Date startDate;
    private Date endDate;
    @Column(nullable = true)
    private Double discountAmount;
    private Double minimumAmountInCart;
    private Integer maximumUsage;
    private int status;
    private boolean deleteFlag;
}
