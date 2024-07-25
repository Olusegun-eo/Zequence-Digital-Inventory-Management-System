package com.zequence.ZequenceIms.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserUpdateRequest {
    private String fullName;
    private String country;
    private String state;
    private String lgaCityZipcode;
    private String phoneNumber;
    private String companyName;
    private MultipartFile companyLogo;
}
