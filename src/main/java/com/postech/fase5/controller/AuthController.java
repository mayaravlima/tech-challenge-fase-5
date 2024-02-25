package com.postech.fase5.controller;

import com.postech.fase5.dto.AuthenticationRequest;
import com.postech.fase5.dto.SigupRequest;
import com.postech.fase5.entity.User;
import com.postech.fase5.exception.UnauthorizedException;
import com.postech.fase5.repository.UserRepository;
import com.postech.fase5.service.auth.AuthService;
import com.postech.fase5.utils.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JWTUtil jwtUtil;

    private final AuthService authService;

    private static final String TOKEN_PREFIX = "Bearer ";


    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.username(),
                            authenticationRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(403, "Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        Optional<User> userOptional = userRepository.findFirstByEmail(authenticationRequest.username());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", user.getId());
            responseMap.put("email", user.getEmail());
            responseMap.put("token", TOKEN_PREFIX + jwt);

            user.setPassword(null);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwt);

            return new ResponseEntity<>(responseMap, headers, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SigupRequest sigupRequest) {
        if (authService.hasUserWithEmail(sigupRequest.email())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        return ResponseEntity.ok(authService.createUser(sigupRequest));

    }


}
