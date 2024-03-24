package com.postech.fase5.gateway.config;

import com.postech.fase5.gateway.exception.ApiErrorResponse;
import com.postech.fase5.gateway.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedException(final UnauthorizedException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatus());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }
}
