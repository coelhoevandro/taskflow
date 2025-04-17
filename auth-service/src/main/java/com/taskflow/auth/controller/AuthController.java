package com.taskflow.auth.controller;

import com.taskflow.auth.dto.LoginRequest;
import com.taskflow.auth.dto.TokenResponse;
import com.taskflow.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles user authentication requests.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")

	/**
	 * Performs user login.
	 *
	 * @param request User login data
	 * @return ResponseEntity with token JWT
	 */
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}
}