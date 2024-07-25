package com.zequence.ZequenceIms.service.userAuthService;


import com.zequence.ZequenceIms.dto.LoginRequest;
import com.zequence.ZequenceIms.dto.SignupRequest;
import com.zequence.ZequenceIms.dtos.UserAuthenticationDetailsDTO;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import com.zequence.ZequenceIms.exceptions.UserNotFoundException;
import com.zequence.ZequenceIms.service.emailServices.exception.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserAuthenticationService {
     String registerAccount(SignupRequest request) throws MessagingException;
     String confirmToken(String token);
     UserAuthenticationDetails login(LoginRequest request);
     void resendVerificationToken(String email) throws MessagingException;
    List<UserAuthenticationDetailsDTO> getCurrentlyLoggedInUsers();
    UserAuthenticationDetailsDTO getUserById(Long userId) throws UserNotFoundException;
    boolean updateUserDetails(String token, String userName, String password, String confirmNewPassword) throws jakarta.mail.MessagingException ;

    boolean verifyAccount(String token, String email, String userName, String password, String confirmNewPassword);

    Page<UserAuthenticationDetails> getAllUsers(Pageable pageable);

    Optional<String> deleteUserAndReturnMessage(Long id);
}