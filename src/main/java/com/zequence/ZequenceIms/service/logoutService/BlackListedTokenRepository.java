package com.zequence.ZequenceIms.service.logoutService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, Long> {
    Optional<BlackListedToken> findByToken(String token);
    boolean existsByToken(String token);
}
