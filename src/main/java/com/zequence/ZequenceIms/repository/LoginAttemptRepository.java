package com.zequence.ZequenceIms.repository;





import com.zequence.ZequenceIms.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

 String INSERT = "INSERT INTO login_attempt (email, success, created_at) VALUES(:email, :success, :createdAt)";
 String FIND_RECENT = "SELECT * FROM login_attempt WHERE email = :email ORDER BY created_at DESC LIMIT :recentCount";

  @Modifying
  @Query(value = INSERT, nativeQuery = true)
  void add(@Param("email") String email, @Param("success") boolean success, @Param("createdAt") LocalDateTime createdAt);

  @Query(value = FIND_RECENT, nativeQuery = true)
  List<LoginAttempt> findRecent(String email);
}
