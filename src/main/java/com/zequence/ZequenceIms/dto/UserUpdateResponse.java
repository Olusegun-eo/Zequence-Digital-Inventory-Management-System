package com.zequence.ZequenceIms.dto;

import com.zequence.ZequenceIms.entity.User;

public class UserUpdateResponse {
    private String message;
    private String fullName;
    private String country;
    private String state;
    private String lgaCityZipcode;
    private String phoneNumber;
    private String companyName;
    private String companyLogoUrl; // Optional, if company logo upload is included

    public UserUpdateResponse(String message, String fullName, String country, String state,
                              String lgaCityZipcode, String phoneNumber, String companyName, String companyLogoUrl) {
        this.message = message;
        this.fullName = fullName;
        this.country = country;
        this.state = state;
        this.lgaCityZipcode = lgaCityZipcode;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.companyLogoUrl = companyLogoUrl;
    }

    // Getters (optional)
}
