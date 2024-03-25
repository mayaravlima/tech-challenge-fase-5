package com.postech.fase5.paymentapi.model;

import com.postech.fase5.paymentapi.entity.Payment;
import com.postech.fase5.paymentapi.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long id;
    private String cartId;
    private Long userId;
    private Integer totalItems;
    private Double totalCost;
    private PaymentStatus status;
    private List<ProductResponse> products;


    public Payment toCreatePayment() {
        return Payment
                .builder()
                .cartId(cartId)
                .userId(userId)
                .totalAmount(totalCost)
                .status(PaymentStatus.PENDING)
                .build();
    }
}