package com.taskflow.auth.service;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * 🇺🇸 Creates and verifies JWT tokens
 */
@Service
public class JwtService {

	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	private final long expiration = 3600000;

	/**
	 * Generates a JWT token for the authenticated user.
	 *
	 * @param username
	 * @return token JWT as string
	 */
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(key).compact();
	}
}