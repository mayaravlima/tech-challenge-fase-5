package com.postech.fase5.cartapi.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private int statusCode;
}
