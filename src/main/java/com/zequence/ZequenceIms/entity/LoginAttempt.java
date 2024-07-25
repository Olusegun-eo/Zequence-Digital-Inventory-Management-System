package com.zequence.ZequenceIms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table()
@Getter
@Setter
@RequiredArgsConstructor
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean success;

    @Column(nullable = false)
    private LocalDateTime createdAt;


}



