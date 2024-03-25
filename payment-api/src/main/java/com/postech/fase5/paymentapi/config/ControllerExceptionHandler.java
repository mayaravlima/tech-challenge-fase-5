package com.postech.fase5.paymentapi.config;

import com.postech.fase5.paymentapi.exception.ErrorResponse;
import com.postech.fase5.paymentapi.exception.PaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(ex.getStatusCode())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleWebClientException(WebClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAs(ErrorResponse.class));
    }
}
