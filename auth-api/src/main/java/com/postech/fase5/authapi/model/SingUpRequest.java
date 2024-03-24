package com.postech.fase5.authapi.model;

import com.postech.fase5.authapi.entity.User;
import com.postech.fase5.authapi.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public record SingUpRequest (

        @NotNull(message = "Name is required")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,

        @NotNull(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @NotNull(message = "Email is required")
        @Email(message = "Email is invalid")
        String email,
        UserRole role
) {
    public User toUser() {
        return User.builder()
                .name(name)
                .password(password)
                .email(email)
                .role(Objects.nonNull(role) ? role : UserRole.CUSTOMER)
                .build();
    }
}
