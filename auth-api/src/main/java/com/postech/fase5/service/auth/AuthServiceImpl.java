package com.postech.fase5.service.auth;

import com.postech.fase5.dto.SigupRequest;
import com.postech.fase5.dto.UserDTO;
import com.postech.fase5.entity.User;
import com.postech.fase5.enums.UserRole;
import com.postech.fase5.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO createUser(SigupRequest sigupRequest) {
        User user = new User();
        user.setEmail(sigupRequest.email());
        user.setName(sigupRequest.name());
        user.setPassword(bCryptPasswordEncoder.encode(sigupRequest.password()));
        user.setRole(UserRole.CUSTOMER);
        final var createdUser = userRepository.save(user);
        return createdUser.toDTO();
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);

        if (adminAccount == null) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setName("admin");
            admin.setRole(UserRole.ADMIN);
            admin.setPassword(bCryptPasswordEncoder.encode("admin"));

            userRepository.save(admin);
        }
    }

}
