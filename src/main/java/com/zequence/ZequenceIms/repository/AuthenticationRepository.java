package com.zequence.ZequenceIms.repository;



import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthenticationRepository extends JpaRepository<UserAuthenticationDetails, Long> {
    @Query("SELECT u FROM UserAuthenticationDetails u WHERE u.isDeleted = false AND LOWER(u.email) = LOWER(:email)")
    Optional<UserAuthenticationDetails> findByEmail(@Param("email") String email);
    @Query("SELECT u FROM UserAuthenticationDetails u WHERE u.isDeleted = false AND LOWER(u.username) = LOWER(:username)")
    Optional<UserAuthenticationDetails> findByUsername(@Param("username") String username);
    @Query("SELECT u FROM UserAuthenticationDetails u WHERE u.isDeleted = false AND (LOWER(u.username) = LOWER(:username) OR LOWER(u.email) = LOWER(:email))")
    Optional<UserAuthenticationDetails> findByEmailOrUsername(String email, String username);
    Optional<UserAuthenticationDetails> findByConfirmationTokensVerificationTokenCode(String verificationTokenCode);
    @Modifying
    @Query("UPDATE UserAuthenticationDetails u SET u.enabled = TRUE WHERE u.email = :email")
    int enableAppUser(@Param("email") String email);

    @Query("SELECT u FROM UserAuthenticationDetails u WHERE u.isDeleted = false")
    Page<UserAuthenticationDetails> findAll(Pageable pageable);
}
