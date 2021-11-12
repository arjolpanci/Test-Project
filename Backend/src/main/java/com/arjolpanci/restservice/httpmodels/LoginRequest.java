package com.arjolpanci.restservice.httpmodels;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

}
