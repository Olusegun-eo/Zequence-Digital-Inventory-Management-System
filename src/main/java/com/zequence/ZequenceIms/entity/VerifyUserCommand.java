package com.zequence.ZequenceIms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class VerifyUserCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String confirmNewPassword;

    @Column(length = 64, nullable = false)
    private String verificationCode;
}
