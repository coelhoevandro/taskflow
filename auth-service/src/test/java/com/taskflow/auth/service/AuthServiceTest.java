package com.taskflow.auth.service;

import com.taskflow.auth.client.UserServiceClient;
import com.taskflow.auth.dto.LoginRequest;
import com.taskflow.auth.dto.RegisterRequest;
import com.taskflow.auth.model.User;
import com.taskflow.auth.repository.RefreshTokenRepository;
import com.taskflow.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock RefreshTokenRepository refreshTokenRepository;
    @Mock JwtService jwtService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UserServiceClient userServiceClient;

    @InjectMocks AuthService authService;

    @Test
    void register_shouldFail_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() ->
            authService.register(new RegisterRequest("test@example.com", "pass123", "Test User"))
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("Email already in use");
    }

    @Test
    void login_shouldFail_whenUserNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            authService.login(new LoginRequest("unknown@example.com", "pass"))
        ).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_shouldFail_whenPasswordDoesNotMatch() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("hashed")
                .role("ROLE_USER")
                .active(true)
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() ->
            authService.login(new LoginRequest("test@example.com", "wrong"))
        ).isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_shouldFail_whenAccountIsInactive() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("hashed")
                .role("ROLE_USER")
                .active(false)
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() ->
            authService.login(new LoginRequest("test@example.com", "pass"))
        ).isInstanceOf(BadCredentialsException.class)
         .hasMessageContaining("disabled");
    }
}
