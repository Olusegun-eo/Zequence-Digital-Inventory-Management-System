package com.zequence.ZequenceIms.service.token;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "confirmation_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String verificationTokenCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(nullable = false, name = "user_authentication_details_id")
    @JsonBackReference
    private UserAuthenticationDetails userAuthenticationDetails;

    public ConfirmationToken(String verificationTokenCode, LocalDateTime createdAt, LocalDateTime expiresAt, UserAuthenticationDetails userAuthenticationDetails) {
        this.verificationTokenCode = verificationTokenCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userAuthenticationDetails = userAuthenticationDetails;
    }
}