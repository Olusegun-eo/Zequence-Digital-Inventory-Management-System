package com.zequence.ZequenceIms.service.userService;


import com.zequence.ZequenceIms.dto.UserUpdateRequest;
import com.zequence.ZequenceIms.dto.UserUpdateResponse;
import com.zequence.ZequenceIms.dtos.UserAuthenticationDetailsDTO;
import com.zequence.ZequenceIms.dtos.UserUpdateRequestDTO;
import com.zequence.ZequenceIms.dtos.UserUpdateResponseDTO;
import com.zequence.ZequenceIms.entity.User;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import com.zequence.ZequenceIms.service.logoutService.BlackListedTokenRepository;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String logout(String token);
    boolean isTokenBlacklisted(String token, BlackListedTokenRepository blacklistedTokenRepository);
    UserUpdateResponseDTO updateUserDetails(Long userId, UserUpdateRequestDTO userUpdateRequest, MultipartFile companyLogo, String token);
    UserAuthenticationDetails getUserAuthDetailsById(Long userId);
}
