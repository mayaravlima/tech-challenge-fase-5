package com.postech.fase5.authapi.model;

import com.postech.fase5.authapi.entity.User;
import com.postech.fase5.authapi.enums.UserRole;

public record UserDTO(
        Long id,
        String name,
        String email,
        UserRole role
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
