package com.taskflow.auth.controller;

import com.taskflow.auth.dto.*;
import com.taskflow.auth.service.AuthService;
import com.taskflow.auth.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }

    // Internal endpoint — the gateway calls this to validate tokens before routing.
    // Not exposed publicly (gateway strips /internal routes from external traffic).
    @GetMapping("/internal/validate")
    public ResponseEntity<ValidateTokenResponse> validate(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ValidateTokenResponse(false, null, null, null));
        }

        String token = authHeader.substring(7);

        if (!jwtService.isValid(token)) {
            return ResponseEntity.ok(new ValidateTokenResponse(false, null, null, null));
        }

        return ResponseEntity.ok(new ValidateTokenResponse(
                true,
                jwtService.extractUserId(token),
                jwtService.extractEmail(token),
                jwtService.extractRole(token)
        ));
    }
}
