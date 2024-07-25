package com.zequence.ZequenceIms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignupRequest {

    @Schema(description = "name", example = "OluEmmy")
    @NotBlank(message = "Name cannot be blank")
    private String userName;

    @Schema(description = "email", example = "emmanuelolusegun.co@gmail.com")
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Schema(description = "password", example = "123456")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
}
