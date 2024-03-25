package com.postech.fase5.cartapi.exception;

import java.util.List;

public record ValidationErrorResponse(
        List<String> errors
) {
}
