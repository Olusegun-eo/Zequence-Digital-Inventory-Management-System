package com.zequence.ZequenceIms.prodStocDaosDtos;

import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import lombok.*;

@Entity
@Table(name = "stock")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_name", nullable = false)
    private String stockName;

    @Column(name = "stock_category")
    private String stockCategory;

    @Column(name = "stock_quantity")
    private Long stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_and_stock_status")
    private ProductAndStockStatus productAndStockStatus;

    @Column(name = "stock_branch")
    private String stockBranch;

    @Column(name = "stock_description")
    private String stockDescription;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductDao product;
}

