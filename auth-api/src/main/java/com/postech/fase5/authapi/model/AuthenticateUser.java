package com.postech.fase5.authapi.model;

public record AuthenticateUser(
        Long id,

        String email,

        String token
) {
}
