package com.postech.fase5.authapi.config;

import com.postech.fase5.authapi.exception.ApiErrorResponse;
import com.postech.fase5.authapi.exception.UnauthorizedException;
import com.postech.fase5.authapi.exception.UserException;
import com.postech.fase5.authapi.exception.ValidationErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedException(final UnauthorizedException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatus());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiErrorResponse> handleUserException(final UserException e) {
        final var apiResponse = new ApiErrorResponse(e.getMessage(), e.getStatus());

        return ResponseEntity.status(HttpStatus.valueOf(apiResponse.status())).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonErrors(HttpMessageNotReadableException error) {
        Map<String, String> errorResponse = Map.of("error", error.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
