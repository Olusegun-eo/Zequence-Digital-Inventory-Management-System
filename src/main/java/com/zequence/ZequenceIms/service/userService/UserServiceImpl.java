package com.zequence.ZequenceIms.service.userService;


import com.zequence.ZequenceIms.cloudinary.CloudinaryService;
import com.zequence.ZequenceIms.dtos.UserUpdateRequestDTO;
import com.zequence.ZequenceIms.dtos.UserUpdateResponseDTO;
import com.zequence.ZequenceIms.entity.User;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import com.zequence.ZequenceIms.exceptions.UserNotFoundException;
import com.zequence.ZequenceIms.helper.JwtHelper;
import com.zequence.ZequenceIms.repository.AuthenticationRepository;
import com.zequence.ZequenceIms.repository.UserRepository;
import com.zequence.ZequenceIms.service.logoutService.BlackListedToken;
import com.zequence.ZequenceIms.service.logoutService.BlackListedTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final BlackListedTokenRepository blacklistedTokenRepository;
    private final JwtHelper jwtHelper;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


@Transactional
@Override
public UserUpdateResponseDTO updateUserDetails(Long userId, UserUpdateRequestDTO userUpdateRequest, MultipartFile companyLogo, String token) {
    // Extract user details from the token
    String username = jwtHelper.extractUsername(token);
    logger.info("Extracted username from token: {}", username);

    // Fetch user details from authentication repository
    UserAuthenticationDetails userAuthDetails = authenticationRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + username));

    // Fetch user from user repository
    User user = userRepository.findById(userId)
            .filter(userDetails -> !userDetails.getAuthenticationDetails().isDeleted())
            .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

    // Check if the authenticated user has permission to update this user's details
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String authenticatedUsername = authentication.getName();

    if (!authenticatedUsername.equals(user.getAuthenticationDetails().getUsername()) && !authenticatedUsername.equals(user.getAuthenticationDetails().getEmail())) {
        logger.error("Unauthorized data modification attempt by user: {}", authenticatedUsername);
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized data modification");
    }

    String companyLogoUrl = null;
    if (companyLogo != null && !companyLogo.isEmpty()) {
        try {
            // Upload company logo and get the URL
            companyLogoUrl = cloudinaryService.uploadFile(companyLogo);
        } catch (IOException e) {
            logger.error("Failed to upload company logo", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload company logo");
        }
    }

    // Update user details
    user.setFullName(userUpdateRequest.getFullName());
    user.setCountry(userUpdateRequest.getCountry());
    user.setState(userUpdateRequest.getState());
    user.setLgaCityZipcode(userUpdateRequest.getLgaCityZipcode());
    user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
    user.setCompanyName(userUpdateRequest.getCompanyName());
    if (companyLogoUrl != null) {
        user.setCompanyLogo(companyLogoUrl);
    }

    // Save updated user details
    userRepository.save(user);

    // Blacklist the token after successful update
    jwtHelper.blacklistToken(token);

    // Construct and return response DTO
    UserUpdateResponseDTO responseDTO = new UserUpdateResponseDTO();
    responseDTO.setFullName(user.getFullName());
    responseDTO.setCountry(user.getCountry());
    responseDTO.setState(user.getState());
    responseDTO.setLgaCityZipcode(user.getLgaCityZipcode());
    responseDTO.setPhoneNumber(user.getPhoneNumber());
    responseDTO.setCompanyName(user.getCompanyName());
    responseDTO.setCompanyLogo(user.getCompanyLogo());

    return responseDTO;
}
    @Override
    @Transactional
    public String logout(String token) {
        try {
            // Check if the token already exists in the blacklist
            Optional<BlackListedToken> existingToken = blacklistedTokenRepository.findByToken(token);
            if (existingToken.isPresent()) {
                return "Token already blacklisted";
            }
            Date expiryDate = jwtHelper.getExpiryDateFromToken(token);
            BlackListedToken blacklistedToken = new BlackListedToken();
            blacklistedToken.setToken(token);
            blacklistedToken.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            blacklistedTokenRepository.save(blacklistedToken);
            return "User logged out successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to logout user: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public UserAuthenticationDetails getUserAuthDetailsById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        return user.getAuthenticationDetails();
    }
    @Override
    @Transactional
    public boolean isTokenBlacklisted(String token, BlackListedTokenRepository blacklistedTokenRepository) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }

}
