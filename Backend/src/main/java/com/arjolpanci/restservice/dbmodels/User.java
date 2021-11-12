package com.arjolpanci.restservice.dbmodels;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

    public static final String ROLE_USER = "user";
    public static final String ROLE_SUPERVISOR = "supervisor";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column private String username;
    @Column private String email;
    @Column private String password;
    @Column private String role;

}
