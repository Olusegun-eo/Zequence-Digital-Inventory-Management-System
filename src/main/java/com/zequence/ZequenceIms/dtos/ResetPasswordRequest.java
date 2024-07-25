package com.zequence.ZequenceIms.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotNull
    private String token;
    @NotNull
    private String newPassword;
    @NotNull
    private String confirmNewPassword;
}
