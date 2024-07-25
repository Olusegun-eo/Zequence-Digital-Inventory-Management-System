package com.zequence.ZequenceIms.repository;


import com.zequence.ZequenceIms.service.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository  extends JpaRepository<ConfirmationToken, Long> {
        Optional<ConfirmationToken> findByVerificationTokenCode(String verificationTokenCode);
}