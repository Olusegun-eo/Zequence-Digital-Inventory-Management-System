package com.zequence.ZequenceIms.entity;

import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "stocks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1, message = "Stock quantity must be greater than zero")
    private Long stockQuantity;

    @NotNull @NotBlank
    private String stockCode;

    @NotNull @NotBlank
    private String stockName;

    @NotNull  @NotBlank
    private String stockCategory;

    @Enumerated(EnumType.STRING)
    private ProductAndStockStatus productAndStockStatus;

    @NotNull  @NotBlank
    private String stockBranch;

    private String stockDescription;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}