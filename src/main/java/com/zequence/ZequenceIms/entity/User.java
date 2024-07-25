package com.zequence.ZequenceIms.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "name", example = "Olusegun Okikiola")
    @NotBlank(message = "Name cannot be blank")
    private String fullName;

    @Schema(description = "phone number", example = "+2348012345678")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    @Schema(description = "country", example = "Nigeria")
    @NotBlank(message = "Country cannot be blank")
    private String country;

    @Schema(description = "state", example = "Lagos")
    @NotBlank(message = "State cannot be blank")
    private String state;

    @Schema(description = "lga/city/zipcode", example = "Ikeja")
    @NotBlank(message = "LGA/City/Zipcode cannot be blank")
    private String lgaCityZipcode;

    @Schema(description = "company name", example = "Acme Inc.")
    private String companyName;

    @Schema(description = "company logo", example = "acme_logo.png")
    private String companyLogo;

    //@OneToOne(cascade = CascadeType.ALL)  This is what I have before implementing bidirectional update
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "authentication_details_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private UserAuthenticationDetails authenticationDetails;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "UserRole",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String gender;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(fullName, user.fullName) && Objects.equals(country, user.country) && Objects.equals(state, user.state)
                && Objects.equals(lgaCityZipcode, user.lgaCityZipcode) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(companyName, user.companyName)
                && Objects.equals(companyLogo, user.companyLogo) && Objects.equals(authenticationDetails, user.authenticationDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, country, state, lgaCityZipcode, phoneNumber, companyName, companyLogo, authenticationDetails);
    }
}