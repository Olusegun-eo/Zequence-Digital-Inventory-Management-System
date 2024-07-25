package com.zequence.ZequenceIms.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zequence.ZequenceIms.service.token.ConfirmationToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "user_authentication_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "reset_password")
    private String resetPassword;

    @Column(name = "change_password")
    private String forgotPassword;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isVerified;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean enabled = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean accountNonLocked;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "userAuthenticationDetails", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private List<ConfirmationToken> confirmationTokens = new ArrayList<>();

    @OneToOne(mappedBy = "authenticationDetails", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private User user;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthenticationDetails that = (UserAuthenticationDetails) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }

    public UserAuthenticationDetails(String username, String email, String password,
                                     boolean enabled, boolean isVerified, boolean isDeleted) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.isVerified = isVerified;
        this.isDeleted = isDeleted;
    }

    //TODO
    /*
    Consider implementing role-based or attribute-based access control (RBAC/ABAC)
     to enforce finer-grained access control beyond simple authentication.
     */

}