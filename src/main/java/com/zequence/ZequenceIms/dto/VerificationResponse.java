package com.zequence.ZequenceIms.dto;

import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class VerificationResponse {
    private boolean success;
    private String errorMessage;
    private UserAuthenticationDetails data;
    private String userId;
}
