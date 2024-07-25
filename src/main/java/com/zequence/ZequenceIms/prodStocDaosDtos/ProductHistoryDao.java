package com.zequence.ZequenceIms.prodStocDaosDtos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistoryDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_descriptions", nullable = false)
    private String productDescription;

    @Column(name = "change_timestamps", nullable = false)
    private LocalDateTime changeTimestamp;

    @Column(name = "product_names", nullable = false)
    private String productName;

    @Column(name = "product_quantity", nullable = false)
    private Integer productThreshHoldLimit;
}
