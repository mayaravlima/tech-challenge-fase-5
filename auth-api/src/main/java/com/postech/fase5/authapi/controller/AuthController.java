package com.postech.fase5.authapi.controller;

import com.postech.fase5.authapi.model.AuthenticateUser;
import com.postech.fase5.authapi.model.AuthenticationRequest;
import com.postech.fase5.authapi.model.SingUpRequest;
import com.postech.fase5.authapi.model.UserDTO;
import com.postech.fase5.authapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createNewUser(@RequestBody @Valid SingUpRequest singUpRequest) {
        return ResponseEntity.status(201).body(authService.createUser(singUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticateUser> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        System.out.println("login");
        return ResponseEntity.ok(authService.authenticate(authenticationRequest));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        authService.validateToken(authorizationHeader);
        return ResponseEntity.ok("Validated");
    }
}
