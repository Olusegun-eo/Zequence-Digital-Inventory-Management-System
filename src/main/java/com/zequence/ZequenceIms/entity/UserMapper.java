package com.zequence.ZequenceIms.entity;

import com.zequence.ZequenceIms.dtos.UserAuthenticationDetailsDTO;
import com.zequence.ZequenceIms.dtos.UserDTO;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCountry(user.getCountry());
        dto.setState(user.getState());
        dto.setLgaCityZipcode(user.getLgaCityZipcode());
        dto.setCompanyName(user.getCompanyName());
        dto.setCompanyLogo(user.getCompanyLogo());
        dto.setGender(user.getGender());
        dto.setPhone(user.getPhone());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }


    public static UserAuthenticationDetailsDTO toUserAuthenticationDetailsDTO(UserAuthenticationDetails userAuthDetails) {
        if (userAuthDetails == null) {
            return null;
        }
        UserAuthenticationDetailsDTO dto = new UserAuthenticationDetailsDTO();
        dto.setId(userAuthDetails.getId());
        dto.setUsername(userAuthDetails.getUsername());
        dto.setEmail(userAuthDetails.getEmail());
        dto.setVerified(userAuthDetails.isVerified());
        dto.setEnabled(userAuthDetails.isEnabled());
        dto.setAccountNonLocked(userAuthDetails.isAccountNonLocked());
        dto.setDeleted(userAuthDetails.isDeleted());
        dto.setUser(toUserDTO(userAuthDetails.getUser()));
        return dto;
    }
}
