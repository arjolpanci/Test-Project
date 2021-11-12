package com.arjolpanci.restservice.httpmodels;

import lombok.Data;

@Data
public class LoginResponse {

    private Long userId;
    private String token;
    private String role;

    public LoginResponse(Long userId, String token, String role) {
        this.userId = userId;
        this.token = token;
        this.role = role;
    }
}
