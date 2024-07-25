package com.zequence.ZequenceIms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank
    private String productName;

    //It’s a many-to-many relationship because one product can be part of several
    // categories, and one category can include multiple products
    //a prroduct “Smartphone.” It can belong to categories like “Electronics,” “Mobile Devices,” and “Gadgets.
    //Conversely, the “Electronics” category may include products like smartphones, laptops, and cameras.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_category_mapping",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<ProductCategory> categories;

    @NotNull
    @Min(value = 1, message = "Product quantity must be greater than zero")
    private Integer productQuantity;

    @Enumerated(EnumType.STRING)
    private ProductAndStockStatus productAndStockStatus;

    //It’s a one-to-many relationship because one product can have several stock items
    // (e.g., different colors, sizes),but each stock item corresponds to a single product.
    //For the product “T-shirt,” there can be different stock items (e.g., “Small Red,” “Medium Blue,” “Large Green”).
    //Each stock item (e.g., “Small Red T-shirt”) is specific to that product.
    @JsonProperty("components")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks;

    @NotNull @NotBlank
    private String productBranch;

    @NotNull
    @Min(value = 1, message = "Product threshold limit must be greater than zero")
    private Integer productThreshHoldLimit;

    private String productDescription;
}