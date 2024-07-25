package com.zequence.ZequenceIms.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
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
