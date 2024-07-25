package com.zequence.ZequenceIms.service.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
    public Optional<ConfirmationToken> getToken(String verificationTokenCode) {
        return confirmationTokenRepository.findByVerificationTokenCode(verificationTokenCode);
    }
    public void setConfirmedAt(String verificationTokenCode) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByVerificationTokenCode(verificationTokenCode)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
    }
    @Transactional
    public void deleteByUserId(Long userId) {
        confirmationTokenRepository.deleteByUserAuthenticationDetailsId(userId);
    }
}