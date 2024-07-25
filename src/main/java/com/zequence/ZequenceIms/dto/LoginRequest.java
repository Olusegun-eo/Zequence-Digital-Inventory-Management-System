package com.zequence.ZequenceIms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginRequest {

    @Schema(description = "username or email", example = "Olusegun or mina@gmail.com")
    @NotBlank(message = "Username or email cannot be blank")
    private String usernameOrEmail;

    @Schema(description = "password", example = "123456")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
}