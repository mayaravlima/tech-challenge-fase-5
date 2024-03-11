package com.postech.fase5.productsapi.exception;

import lombok.*;

@Getter
@Setter
public class ProductServiceCustomException extends RuntimeException {
    private String message;

    private int statusCode;

    public ProductServiceCustomException(String message) {
        super(message);
    }

    public ProductServiceCustomException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
