package com.taskflow.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User's login data
 */
@Data 
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    
}