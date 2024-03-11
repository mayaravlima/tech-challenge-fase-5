package com.postech.fase5.config;

import com.postech.fase5.exception.ApiErrorResponse;
import com.postech.fase5.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleReceiptException(final UnauthorizedException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatus());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }
}
