package com.zequence.ZequenceIms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateRequestDTO {
    private String fullName;
    private String country;
    private String state;
    private String lgaCityZipcode;
    private String phoneNumber;
    private String companyName;
    // Other fields as necessary

    // Getters and Setters
}