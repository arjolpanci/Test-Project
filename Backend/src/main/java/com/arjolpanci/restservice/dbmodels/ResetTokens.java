package com.arjolpanci.restservice.dbmodels;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ResetTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @Column
    private String token;
}
