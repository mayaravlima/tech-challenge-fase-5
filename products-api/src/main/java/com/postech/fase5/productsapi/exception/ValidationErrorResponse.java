package com.postech.fase5.productsapi.exception;

import java.util.List;

public record ValidationErrorResponse(
        List<String> errors
) {
}
