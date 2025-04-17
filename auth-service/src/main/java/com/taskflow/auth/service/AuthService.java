package com.taskflow.auth.service;

import org.springframework.stereotype.Service;

import com.taskflow.auth.dto.LoginRequest;
import com.taskflow.auth.dto.TokenResponse;
import com.taskflow.auth.exception.UnauthorizedException;

/**
 * 
 * Main logic to authenticate the user.
 */
@Service
public class AuthService {
	private final JwtService jwtService;

	public AuthService(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	public TokenResponse authenticate(LoginRequest request) {
		if ("evandro".equals(request.getUsername()) && "123456".equals(request.getPassword())) {
			String token = jwtService.generateToken(request.getUsername());
			return new TokenResponse(token);
		}
		throw new UnauthorizedException("Invalid credentials");
	}
}