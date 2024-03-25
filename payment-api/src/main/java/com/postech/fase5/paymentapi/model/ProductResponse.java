package com.postech.fase5.paymentapi.model;

public record ProductResponse(
        String name,
        Integer quantity,
        Double amount
) {
}
