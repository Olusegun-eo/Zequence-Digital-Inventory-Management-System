package com.zequence.ZequenceIms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserAuthenticationDetailsDTO {
    private Long id;
    private String username;
    private String email;
    private boolean isVerified;
    private boolean isEnabled;
    private boolean isAccountNonLocked;
    private boolean isDeleted;
    private UserDTO user;
    private String fullName;

    public UserAuthenticationDetailsDTO(Long id, String username, String email, boolean verified, boolean enabled, boolean accountNonLocked, String s) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.isVerified = verified;
        this.isEnabled = enabled;
        this.isAccountNonLocked = accountNonLocked;
        this.isDeleted = false;
    }

    // Getters and Setters
}