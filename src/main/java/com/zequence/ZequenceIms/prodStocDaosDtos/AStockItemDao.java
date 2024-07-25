package com.zequence.ZequenceIms.prodStocDaosDtos;


import jakarta.persistence.Id;
import lombok.*;

/*
This is an ArticleDao == AStockIntemDao:  Note That: This class now represents a stock item
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AStockItemDao {

    @Id
    private Long id;

    private String name;
    private long stock;
    // Assuming 'availability' is a property to indicate whether the stock item is available or not
    private boolean availability;
}
