package com.postech.fase5.paymentapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentException extends RuntimeException {

    private String message;

    private int statusCode;

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}