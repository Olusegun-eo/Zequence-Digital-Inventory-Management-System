package com.zequence.ZequenceIms.prodStocDaosDtos;


import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import com.zequence.ZequenceIms.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String productName;
    private Integer productQuantity;
    private List<ProductCategory> categories;
    private ProductAndStockStatus productAndStockStatus;
    private List<StockDao> stockDaos;
    private String productBranch;
    private String productDescription;
    private Integer productThreshHoldLimit;
    private List<StockDTO> stockDtos;
}