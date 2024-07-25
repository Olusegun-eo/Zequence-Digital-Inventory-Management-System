package com.zequence.ZequenceIms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class StockCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stockCategoryTitle;

    @OneToMany(mappedBy = "stockCategory")
    private List<Stock> products;

    // Getters and Setters omitted for brevity
}