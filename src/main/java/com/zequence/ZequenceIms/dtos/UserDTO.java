package com.zequence.ZequenceIms.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String country;
    private String state;
    private String lgaCityZipcode;
    private String companyName;
    private String companyLogo;
    private String gender;
    private String phone;
    private Set<String> roles; // Assuming roles are represented by their names

    // Getters and Setters
}