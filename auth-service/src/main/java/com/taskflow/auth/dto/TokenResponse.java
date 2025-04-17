package com.taskflow.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Token sent to the user after login
 */
@Data
@AllArgsConstructor
public class TokenResponse {
	private String token;
}