package com.postech.fase5.authapi.service;

import com.postech.fase5.authapi.entity.User;
import com.postech.fase5.authapi.exception.UnauthorizedException;
import com.postech.fase5.authapi.exception.UserException;
import com.postech.fase5.authapi.model.AuthenticateUser;
import com.postech.fase5.authapi.model.AuthenticationRequest;
import com.postech.fase5.authapi.model.SingUpRequest;
import com.postech.fase5.authapi.model.UserDTO;
import com.postech.fase5.authapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private static final String TOKEN_PREFIX = "Bearer ";


    public UserDTO createUser(SingUpRequest singUpRequest) {
        userRepository.findFirstByEmail(singUpRequest.email())
                .ifPresent(user -> {
                    throw new UserException(
                            HttpStatus.BAD_REQUEST.value(),
                            "User with email " + singUpRequest.email() + " already exists"
                    );
                });

        var user = singUpRequest.toUser();
        user.setPassword(bCryptPasswordEncoder.encode(singUpRequest.password()));

        final var createdUser = userRepository.save(user);
        return UserDTO.fromUser(createdUser);
    }

    public AuthenticateUser authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.email(),
                            authenticationRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(403, "Invalid username or password");
        }

        UserDetails userDetails = loadUserByUsername(authenticationRequest.email());
        Optional<User> userOptional = userRepository.findFirstByEmail(authenticationRequest.email());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String jwt = generateToken(userDetails.getUsername());

            return new AuthenticateUser(user.getId(), user.getEmail(), jwt);

        }

        throw new UnauthorizedException(401, "Unauthorized");
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }

    private UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
