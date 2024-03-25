package com.postech.fase5.paymentapi.entity;

import com.postech.fase5.paymentapi.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cartId;
    private Long userId;
    private double totalAmount;
    private PaymentStatus status;
}
