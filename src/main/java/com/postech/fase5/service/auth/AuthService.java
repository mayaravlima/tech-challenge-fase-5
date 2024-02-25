package com.postech.fase5.service.auth;

import com.postech.fase5.dto.SigupRequest;
import com.postech.fase5.dto.UserDTO;

public interface AuthService {
    Boolean hasUserWithEmail(String email);

    UserDTO createUser(SigupRequest sigupRequest);
}
