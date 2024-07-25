package com.zequence.ZequenceIms.newEntity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suppliers {
    @Id
    Long id;

    private String supplierCode;
    private String supplierFullName;
    private String supplierLocation;
    private String supplierMobile;
}
