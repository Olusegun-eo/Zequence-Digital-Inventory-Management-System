package com.zequence.ZequenceIms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductDao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull @NotBlank @Size(max = 255)
    @Column(name = "category_title", nullable = false, length = 100)
    private String productCategoryTitle;

//    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    @JsonIgnore
//    private List<ProductDao> productDaos;

    @Column(name = "text_Description",nullable = false, length = 500)
    private String description;

    @Override
    public String toString() {
        return "ProductCategory{" +
                "id=" + id +
                ", productCategoryTitle='" + productCategoryTitle + '\'' +
                ", productDaos=" +
                '}';
    }
}
