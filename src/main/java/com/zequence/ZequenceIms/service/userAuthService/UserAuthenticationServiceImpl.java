package com.zequence.ZequenceIms.service.userAuthService;



import com.zequence.ZequenceIms.dto.LoginRequest;
import com.zequence.ZequenceIms.dto.SignupRequest;
import com.zequence.ZequenceIms.dtos.UserAuthenticationDetailsDTO;
import com.zequence.ZequenceIms.entity.User;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import com.zequence.ZequenceIms.entity.UserMapper;
import com.zequence.ZequenceIms.exceptions.DuplicateException;
import com.zequence.ZequenceIms.exceptions.TokenExpiredException;
import com.zequence.ZequenceIms.exceptions.UserNotFoundException;
import com.zequence.ZequenceIms.repository.AuthenticationRepository;
import com.zequence.ZequenceIms.repository.UserRepository;
import com.zequence.ZequenceIms.service.emailServices.EmailService;
import com.zequence.ZequenceIms.service.emailServices.exception.MessagingException;
import com.zequence.ZequenceIms.service.token.ConfirmationToken;
import com.zequence.ZequenceIms.service.token.ConfirmationTokenService;
import com.zequence.ZequenceIms.service.token.EmailValidator;
import com.zequence.ZequenceIms.utils.UserDefaults;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Transactional
@AllArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService{
  private final AuthenticationRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final ConfirmationTokenService confirmationTokenService;
  private final EmailService emailService;
  private final EmailValidator emailValidator;
  private final SpringTemplateEngine templateEngine;
  private final UserRepository userRepository;


  @Transactional
  @Override
  public String registerAccount(SignupRequest request) throws MessagingException {
    boolean isValidEmail = emailValidator.test(request.getEmail());
    if (!isValidEmail) {
      throw new IllegalStateException(String.format("Email address '%s' is invalid.", request.getEmail()));
    }
    Optional<UserAuthenticationDetails> existingUser = repository.findByEmail(request.getEmail());
    if (existingUser.isPresent()) {
      throw new DuplicateException(String.format("User with the email address '%s' already exists.", request.getEmail()));
    }

    Optional<UserAuthenticationDetails> existingUsername = repository.findByUsername(request.getUserName());
    if (existingUsername.isPresent()) {
      throw new DuplicateException(String.format("User with the username '%s' already exists.", request.getUserName()));
    }

    // Encrypt the user's password
    String encodedPassword = passwordEncoder.encode(request.getPassword());

    // Create a new UserAuthenticationDetails object
    UserAuthenticationDetails appUser = new UserAuthenticationDetails(
            request.getUserName(),
            request.getEmail(),
            encodedPassword,
            false,
            false,
            false
    );

    // Save the new user
    UserAuthenticationDetails savedUser = repository.save(appUser);

    // Create and save the associated User entity
    createUser(savedUser);

    // Generate a verification code or token
    String verificationCode = generateVerificationCode();

    // Create a confirmation token with the saved user
    ConfirmationToken confirmationToken = new ConfirmationToken(
            verificationCode,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            savedUser
    );
    // Save the confirmation token to the database
    confirmationTokenService.saveConfirmationToken(confirmationToken);

    // Build the email content with the verification code only
    String emailContent = buildEmailContent(request.getUserName(), verificationCode);

    // Send a verification email
    emailService.sendActivateAccountMessage(request.getEmail(), "Account Registration Details: " + emailContent);

    return "Registration successful. Please check your email for the verification code.";
  }

  @Override
  @Transactional
  public UserAuthenticationDetails login(LoginRequest request) {
    String identifier = request.getUsernameOrEmail();
    System.out.println("Attempting login for identifier: " + identifier);

    Optional<UserAuthenticationDetails> userOpt;

    if (emailValidator.test(identifier)) {
      userOpt = repository.findByEmail(identifier);
      if (userOpt.isPresent()) {
        System.out.println("Identifier is an email: " + userOpt.get().getEmail());
      }
    } else {
      userOpt = repository.findByUsername(identifier);
      if (userOpt.isPresent()) {
        System.out.println("Identifier is a username: " + userOpt.get().getUsername());
      }
    }

    if (!userOpt.isPresent()) {
      System.out.println("User not found with identifier: " + identifier);
      throw new UsernameNotFoundException("User does not exist, identifier: " + identifier);
    }

    UserAuthenticationDetails user = userOpt.get();
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }

    return user;
  }
  @Override
  @Transactional
  public void resendVerificationToken(String email) throws MessagingException {
    Optional<UserAuthenticationDetails> userOptional = repository.findByEmail(email);
    if (userOptional.isPresent()) {
      UserAuthenticationDetails user = userOptional.get();
      String verificationCode = generateVerificationCode();
      ConfirmationToken confirmationToken = new ConfirmationToken(
              verificationCode,
              LocalDateTime.now(),
              LocalDateTime.now().plusMinutes(5),
              user);
      confirmationTokenService.saveConfirmationToken(confirmationToken);
//      user.setVerificationTokenCode(confirmationToken);
//      repository.save(user);

      //receive notification
      //String verificationLink = "http://localhost:8080/api/auth/confirm?token=" + verificationCode; //remove
      emailService.sendAccountCreationMessage(user.getEmail(), user.getUsername(), verificationCode);
      //return "Registration successful. Please check your email for the verification link.";
    } else {
      throw new IllegalArgumentException("User not found with email: " + email);
    }
  }

  @Override
  @Transactional
  public UserAuthenticationDetailsDTO getUserById(Long userId) throws UserNotFoundException {
    System.out.println("Fetching user with ID: " + userId);
    UserAuthenticationDetails userAuthDetails = repository.findById(userId)
            .filter(user -> !user.isDeleted())
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    return UserMapper.toUserAuthenticationDetailsDTO(userAuthDetails);
  }
  @Override
  @Transactional
  public String confirmToken(String token) {
    System.out.println("Confirming token: " + token);
    ConfirmationToken confirmationToken = confirmationTokenService
            .getToken(token)
            .orElseThrow(() -> {
              System.err.println("Token not found: " + token);
              return new IllegalStateException("token not found");
            });

    if (confirmationToken.getConfirmedAt() != null) {
      System.err.println("Token already confirmed: " + token);
      throw new IllegalStateException("email already confirmed");
    }
    LocalDateTime expiredAt = confirmationToken.getExpiresAt();
    if (expiredAt.isBefore(LocalDateTime.now())) {
      System.err.println("Token expired: " + token);
      throw new IllegalStateException("token expired, please generate a new one");
    }
    if (!confirmationToken.getVerificationTokenCode().equals(token)) {
      System.err.println("Token does not match: " + token);
      throw new IllegalStateException("token does not match");
    }
    confirmationTokenService.setConfirmedAt(token);
    enableAppUser(confirmationToken.getUserAuthenticationDetails().getEmail());
    System.out.println("Token successfully confirmed: " + token);
    return "User token successfully confirmed";
  }
  @Override
  @Transactional(readOnly = true)
  public List<UserAuthenticationDetailsDTO> getCurrentlyLoggedInUsers() {
    List<UserAuthenticationDetailsDTO> loggedInUsersDTO = new ArrayList<>();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
      String username = authentication.getName();
      Optional<UserAuthenticationDetails> user = repository.findByEmailOrUsername(username.toLowerCase(), username.toLowerCase());
      if (user.isPresent()) {
        UserAuthenticationDetails userDetails = user.get();
        UserAuthenticationDetailsDTO dto = new UserAuthenticationDetailsDTO(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.isVerified(),
                userDetails.isEnabled(),
                userDetails.isAccountNonLocked(),
                userDetails.getUser() != null ? userDetails.getUser().getFullName() : null
        );
        loggedInUsersDTO.add(dto);
      }
    }
    return loggedInUsersDTO;
  }
  @Override
  @Transactional
  public Optional<String> deleteUserAndReturnMessage(Long id) {
    Optional<UserAuthenticationDetails> userOptional = repository.findById(id);
    if (userOptional.isPresent()) {
      UserAuthenticationDetails user = userOptional.get();
      user.setDeleted(true); // Perform soft delete
      repository.save(user); // Save the updated user entity

      confirmationTokenService.deleteByUserId(id); // Optionally, handle related entities
      return Optional.of("User deleted successfully.");
    } else {
      return Optional.empty();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserAuthenticationDetails> getAllUsers(Pageable pageable) {
    return repository.findAll(pageable);
  }
  @Override
  @Transactional
  public boolean verifyAccount(String token, String email, String userName, String password, String confirmNewPassword) {
    try {
      System.out.println("Verifying account with token: " + token);

      ConfirmationToken confirmationToken = confirmationTokenService
              .getToken(token)
              .orElseThrow(() -> new IllegalStateException("Token not found"));

      if (confirmationToken.getConfirmedAt() != null) {
        throw new IllegalStateException("Email already confirmed");
      }

      LocalDateTime expiredAt = confirmationToken.getExpiresAt();
      if (expiredAt.isBefore(LocalDateTime.now())) {
        throw new TokenExpiredException("Token expired. Please request a new verification token.");
      }

      confirmationTokenService.setConfirmedAt(token);
      System.out.println("Token confirmed successfully: " + confirmationTokenService.getToken(token).get().getConfirmedAt());
      enableAppUser(confirmationToken.getUserAuthenticationDetails().getEmail());

      Optional<UserAuthenticationDetails> userOptional = repository.findByConfirmationTokensVerificationTokenCode(confirmationToken.getVerificationTokenCode());
      if (userOptional.isPresent()) {
        UserAuthenticationDetails user = userOptional.get();
        System.out.println("User found: " + user.getUsername());

        if (email.equals(user.getEmail()) &&
                userName.equals(user.getUsername()) &&
                passwordEncoder.matches(password, user.getPassword()) &&
                passwordEncoder.matches(confirmNewPassword, user.getPassword())) {
          user.setVerified(true);
          repository.save(user);
          System.out.println("User account verified and updated");
          return true;
        } else {
          System.out.println("Verification details do not match");
        }
      } else {
        System.out.println("No user found with the provided verification code");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  @Override
  @Transactional
  public boolean updateUserDetails(String verificationTokenCode, String userName, String password, String confirmNewPassword) throws jakarta.mail.MessagingException {
    if (!password.equals(confirmNewPassword)) {
      throw new IllegalArgumentException("Passwords do not match.");
    }

    Optional<UserAuthenticationDetails> userOptional = repository.findByConfirmationTokensVerificationTokenCode(verificationTokenCode);
    if (userOptional.isPresent()) {
      UserAuthenticationDetails user = userOptional.get();
      user.getUsername().equals(userName);
      user.setPassword(passwordEncoder.encode(password));
      repository.save(user);
      emailService.sendAccountUpdateMessage(user.getEmail(), user.getUsername(), verificationTokenCode);
      return true;
    }
    return false;
  }

  @Transactional
  public User createUser(UserAuthenticationDetails userAuthDetails) {
    User user = UserDefaults.createDefaultUser(userAuthDetails);
    return userRepository.save(user);
  }
 /* @Transactional
  public String forgotPassword(String email) throws MessagingException {
    UserAuthenticationDetails userAuthDetails = repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(String.format("User with email '%s' not found.", email)));

    String resetToken = generateVerificationCode();
    userAuthDetails.setResetPassword(resetToken);

    emailService.sendResetPasswordMessage(email, resetToken);

    repository.save(userAuthDetails);

    return "Reset password token sent to your email.";
  }

  @Transactional
  public String changePassword(String token, String newPassword) {
    UserAuthenticationDetails userAuthDetails = repository.findByResetPassword(token)
            .orElseThrow(() -> new InvalidTokenException("Invalid reset token."));

    userAuthDetails.setPassword(passwordEncoder.encode(newPassword));
    userAuthDetails.setResetPassword(null);

    repository.save(userAuthDetails);

    return "Password successfully changed.";
  } */

  //THE ARE THE HELPER'S METHODS
  private String generateVerificationCode() {
    String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();
    StringBuilder token = new StringBuilder(6);

    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(alphanumeric.length());
      token.append(alphanumeric.charAt(index));
    }

    return token.toString();
  }
  public void enableAppUser(String email) {
    int updatedRows = repository.enableAppUser(email);
    if (updatedRows == 0) {
      throw new IllegalStateException("User not found with email: " + email);
    }
    System.out.println("User enabled with email: " + email);
  }
  private String buildEmailContent(String userName, String verificationCode) {
    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("verificationCode", verificationCode);
    return templateEngine.process("emailTemplate", context);
  }
}
