package com.zequence.ZequenceIms.prodStocDaosDtos;


import com.zequence.ZequenceIms.entity.Product;
import com.zequence.ZequenceIms.entity.ProductCategory;
import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "product_dao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description_s")
    private String productDescription;

    @Column(name = "product_thresh_hold_limit")
    private int productThreshHoldLimit;

    @Column(name = "product_branch")
    private String productBranch;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "product_category_product_dao", // Define a join table name if needed
            joinColumns = @JoinColumn(name = "product_dao_id"),
            inverseJoinColumns = @JoinColumn(name = "product_category_id"))
    private List<ProductCategory> productCategories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_category_id") // Reference the category ID
    private ProductCategory productCategory; // Explicit field for the owning side

    @Column(name = "product_quantity")
    private int productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_and_stock_status")
    private ProductAndStockStatus productAndStockStatus;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockDao> stocks;
}
