
package com.zequence.ZequenceIms.controller;



import com.zequence.ZequenceIms.RequireAuthentication;
import com.zequence.ZequenceIms.dto.*;
import com.zequence.ZequenceIms.dtos.UserAuthenticationDetailsDTO;
import com.zequence.ZequenceIms.entity.LoginAttempt;
import com.zequence.ZequenceIms.entity.MessageResponse;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import com.zequence.ZequenceIms.exceptions.InvalidRequestException;
import com.zequence.ZequenceIms.exceptions.TokenExpiredException;
import com.zequence.ZequenceIms.exceptions.UserNotFoundException;
import com.zequence.ZequenceIms.helper.JwtHelper;
import com.zequence.ZequenceIms.service.emailServices.exception.MessagingException;
import com.zequence.ZequenceIms.service.logService.LoginService;
import com.zequence.ZequenceIms.service.userAuthService.UserAuthenticationService;
import com.zequence.ZequenceIms.utils.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserAuthenticationService userAuthenticationService;
  private final LoginService loginService;
  private final JwtHelper jwtHelper;


  //Set the URL to http://localhost:8080/api/auth/signup   "POST"
  @Operation(summary = "Signup user")
  @ApiResponse(responseCode = "201")
  @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @PostMapping("/signup")
  public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest requestDto) throws MessagingException {
    //String responseMessage = "Registration successful. Please check your email to activate your account.";
    if (!loginService.isValidEmail(requestDto.getEmail())) {
      throw new InvalidRequestException("Please enter a valid email.");
    }

    if (requestDto.getUserName().isEmpty()) {
      throw new InvalidRequestException("Name field cannot be empty.");
    }
    String returnResponse = userAuthenticationService.registerAccount(requestDto);
    return ResponseEntity.ok(returnResponse);
  }
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    try {
      UserAuthenticationDetails user = userAuthenticationService.login(request);

      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
      );

      saveLoginAttempt(request.getUsernameOrEmail());

      String token = jwtHelper.generateToken(user.getEmail());
      logSuccessOrFailure(request.getUsernameOrEmail(), true);
      return ResponseEntity.ok(new LoginResponse(user.getEmail(), token, "Login successful. Welcome back!"));
    } catch (BadCredentialsException | UsernameNotFoundException e) {
      logSuccessOrFailure(request.getUsernameOrEmail(), false);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
  }

  @RequireAuthentication
  @PostMapping(path = "/confirm")
  public ResponseEntity<String> confirm(@RequestBody Map<String, String> requestBody) {
    String verificationCode = requestBody.get("token");
    System.out.println("Received confirmation request for token: " + verificationCode);

    if (verificationCode == null || verificationCode.isEmpty()) {
      System.err.println("Token is missing in the request.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing.");
    }

    try {
      String result = userAuthenticationService.confirmToken(verificationCode);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      System.err.println("Error confirming token: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
  @RequireAuthentication @PostMapping("/resend-token")
  public ResponseEntity<String> resendVerificationToken(@RequestBody Map<String, String> requestBody) {
    String email = requestBody.get("email");
    System.out.println("Email To Resend Token: " + email);
    try {
      userAuthenticationService.resendVerificationToken(email);
      return ResponseEntity.ok("Verification token resent successfully. Please check your email.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("User not found with email: " + email);
    } catch (MessagingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email. Please try again.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
  }
  @Operation(summary = "Get recent login attempts")
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
  @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @RequireAuthentication @GetMapping(value = "/loginAttempts")
  public ResponseEntity<List<LoginAttemptResponse>> loginAttempts(@RequestHeader("Authorization") String token) {
    String email = jwtHelper.extractUsername(token.replace("Bearer ", ""));
    List<LoginAttempt> loginAttempts = loginService.findRecentLoginAttempts(email);
    return ResponseEntity.ok(convertToDTOs(loginAttempts));
  }

  @RequireAuthentication
  @GetMapping("/users-logged-in")
  public ResponseEntity<List<UserAuthenticationDetailsDTO>> getCurrentlyLoggedInUsers() {
    List<UserAuthenticationDetailsDTO> loggedInUsers = userAuthenticationService.getCurrentlyLoggedInUsers();
    if (loggedInUsers.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loggedInUsers);
    }
    return ResponseEntity.ok(loggedInUsers);
  }


  @RequireAuthentication
  @GetMapping("/get-all-users")
  @ResponseBody
  public ResponseEntity<PaginatedResponse<UserAuthenticationDetailsDTO>> findUsers(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
    Page<UserAuthenticationDetails> userPage = userAuthenticationService.getAllUsers(pageable);
    List<UserAuthenticationDetailsDTO> users = userPage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());

    PaginatedResponse<UserAuthenticationDetailsDTO> response = new PaginatedResponse<>(
            users,
            userPage.getNumber(),
            userPage.getSize(),
            userPage.getTotalElements(),
            userPage.getTotalPages()
    );

    return ResponseEntity.ok(response);
  }


  @RequireAuthentication
  @PostMapping("/get-user-by-id")
  public ResponseEntity<?> getUserById(@RequestBody Map<String, Long> requestBody) {
    Long userId = requestBody.get("userId");
    System.out.println("Received request to get user by ID: " + userId);
    try {
      UserAuthenticationDetailsDTO user = userAuthenticationService.getUserById(userId);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException e) {
      System.out.println("User not found with ID: " + userId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(new MessageResponse("User not found with id: " + userId));
    } catch (Exception e) {
      System.out.println("Error retrieving user by ID: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new MessageResponse("An error occurred while retrieving user"));
    }
  }

  @RequireAuthentication @DeleteMapping("/delete-user")
  public ResponseEntity<String> deleteUser(@RequestBody Map<String, Long> requestBody) {
    Long id = requestBody.get("id");
    return userAuthenticationService.deleteUserAndReturnMessage(id)
            .map(message -> ResponseEntity.ok(message))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
  }

  @RequireAuthentication @PostMapping("/verify")
  public ResponseEntity<String> verifyAccount(@RequestParam("token") String token,
                                              @RequestParam("email") String email,
                                              @RequestParam("username") String userName,
                                              @RequestParam("password") String password,
                                              @RequestParam("confirmNewPassword") String confirmNewPassword) {
    try {
      boolean verified = userAuthenticationService.verifyAccount(token, email, userName, password, confirmNewPassword);
      if (verified) {
        return ResponseEntity.ok("Account verification successful. Your account is now activated.");
      } else {
        return ResponseEntity.badRequest().body("Invalid verification details.");
      }
    } catch (TokenExpiredException e) {
      return ResponseEntity.badRequest().body("Token expired. Please request a new verification token.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
  }

  @RequireAuthentication @PostMapping("/updateDetails")
  public ResponseEntity<String> updateUserDetails(@RequestParam("token") String token,
                                                  @RequestParam("username") String userName,
                                                  @RequestParam("password") String password,
                                                  @RequestParam("confirmNewPassword") String confirmNewPassword) {
    try {
      boolean updated = userAuthenticationService.updateUserDetails(token, userName, password, confirmNewPassword);
      if (updated) {
        return ResponseEntity.ok("User details updated successfully. Check your email to activate your account.");
      } else {
        return ResponseEntity.badRequest().body("Failed to update user details.");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
  }
  private List<LoginAttemptResponse> convertToDTOs(List<LoginAttempt> loginAttempts) {
    return loginAttempts.stream()
            .map(LoginAttemptResponse::convertToDTO)
            .collect(Collectors.toList());
  }
  private static void logSuccessOrFailure(String email, boolean success) {
    log.info("Login attempt status: email={}, success={}, createdAt={}", email, success, LocalDateTime.now());
  }
  private void saveLoginAttempt(String email) {
    loginService.addLoginAttempt(email, true);
  }

  // Helper method to convert UserAuthenticationDetails to UserAuthenticationDetailsDTO
  private UserAuthenticationDetailsDTO convertToDTO(UserAuthenticationDetails userAuthDetails) {
    return new UserAuthenticationDetailsDTO(
            userAuthDetails.getId(),
            userAuthDetails.getUsername(),
            userAuthDetails.getEmail(),
            userAuthDetails.isVerified(),
            userAuthDetails.isEnabled(),
            userAuthDetails.isAccountNonLocked(),
            userAuthDetails.getUser() != null ? userAuthDetails.getUser().getFullName() : null
    );
  }
}