package com.postech.fase5.dto;

import com.postech.fase5.entity.User;
import com.postech.fase5.enums.UserRole;

public record UserDTO(
        Long id,
        String name,
        String email,
        UserRole role
) {
}
