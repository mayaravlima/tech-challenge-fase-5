package com.postech.fase5.paymentapi.model;

import com.postech.fase5.paymentapi.entity.Payment;
import com.postech.fase5.paymentapi.enums.PaymentStatus;

import java.util.List;
import java.util.stream.Collectors;

public record PaymentResponse(
        Long id,
        String cartId,
        Long userId,
        Double totalCost,
        PaymentStatus status

) {
    public static PaymentResponse fomEntity(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getCartId(),
                payment.getUserId(),
                payment.getTotalAmount(),
                payment.getStatus()
        );
    }

    public static List<PaymentResponse> fromEntities(List<Payment> payments) {
        return payments.stream()
                .map(PaymentResponse::fomEntity)
                .collect(Collectors.toList());
    }
}
