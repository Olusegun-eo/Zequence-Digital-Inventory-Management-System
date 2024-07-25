package com.zequence.ZequenceIms.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponseDTO {

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotBlank(message = "LGA/City/Zipcode cannot be blank")
    private String lgaCityZipcode;

    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    //@NotBlank(message = "Company name cannot be blank")
    private String companyLogo;

    @NotBlank(message = "company Name cannot be blank")
    private String companyName;

    // Getters and Setters
}
