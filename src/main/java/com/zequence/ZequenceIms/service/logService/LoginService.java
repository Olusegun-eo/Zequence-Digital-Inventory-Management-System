package com.zequence.ZequenceIms.service.logService;



import com.zequence.ZequenceIms.entity.LoginAttempt;
import com.zequence.ZequenceIms.repository.AuthenticationRepository;
import com.zequence.ZequenceIms.repository.LoginAttemptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;

import static com.zequence.ZequenceIms.service.token.EmailValidator.EMAIL_PATTERN;


@Service
@Transactional
public class LoginService {

  private final LoginAttemptRepository repository;
  private final AuthenticationRepository authenticationRepository;

  public LoginService(LoginAttemptRepository repository, AuthenticationRepository authenticationRepository) {
    this.authenticationRepository = authenticationRepository;
    this.repository = repository;
  }

  @Transactional
  public void addLoginAttempt(String email, boolean success) {
    LocalDateTime createdAt = LocalDateTime.now();
    repository.add(email, success, createdAt);
  }
  public List<LoginAttempt> findRecentLoginAttempts(String email) {
    return repository.findRecent(email);
  }

  public boolean isValidEmail(String email) {
    Matcher matcher = EMAIL_PATTERN.matcher(email);
    return matcher.matches();
  }

}
