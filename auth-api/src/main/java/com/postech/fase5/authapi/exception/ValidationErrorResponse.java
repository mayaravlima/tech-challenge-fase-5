package com.postech.fase5.authapi.exception;

import java.util.List;

public record ValidationErrorResponse(
        List<String> errors
) {
}
