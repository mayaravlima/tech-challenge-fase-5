package com.postech.fase5.dto;

public record SigupRequest(
        String name,
        String password,
        String email
) {
}
