package com.postech.fase5.cartapi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartException extends RuntimeException {

    private String message;

    private int statusCode;

    public CartException(String message) {
        super(message);
    }

    public CartException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
