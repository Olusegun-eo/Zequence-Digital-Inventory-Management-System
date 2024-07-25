package com.zequence.ZequenceIms.dto;



import com.zequence.ZequenceIms.entity.LoginAttempt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class LoginAttemptResponse {

  @Schema(description = "The date and time of the login attempt")
  private LocalDateTime createdAt;

  @Schema(description = "The login status")
  private boolean success;

  // Constructor
  public LoginAttemptResponse(LocalDateTime createdAt, boolean success) {
    this.createdAt = createdAt;
    this.success = success;
  }

  public static LoginAttemptResponse convertToDTO(LoginAttempt loginAttempt) {
    return new LoginAttemptResponse(loginAttempt.getCreatedAt(), loginAttempt.isSuccess());
  }
}
