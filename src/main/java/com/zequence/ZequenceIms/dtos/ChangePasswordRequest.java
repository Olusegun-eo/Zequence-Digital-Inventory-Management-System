package com.zequence.ZequenceIms.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor@AllArgsConstructor
public class ChangePasswordRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
    @NotNull
    private String confirmNewPassword;
}
