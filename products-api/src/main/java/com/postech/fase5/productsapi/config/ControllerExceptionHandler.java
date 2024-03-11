package com.postech.fase5.productsapi.config;

import com.postech.fase5.productsapi.exception.ErrorResponse;
import com.postech.fase5.productsapi.exception.ProductServiceCustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceCustomException(ProductServiceCustomException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(ex.getStatusCode())
                .build(), HttpStatus.NOT_FOUND);
    }
}
