package com.arjolpanci.restservice.httpmodels;

import lombok.Data;

@Data
public class PasswordResetRequest {

    private String token;
    private String email;
    private String password;

}
