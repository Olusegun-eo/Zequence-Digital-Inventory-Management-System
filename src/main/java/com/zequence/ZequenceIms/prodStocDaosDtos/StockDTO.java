package com.zequence.ZequenceIms.prodStocDaosDtos;

import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockDTO {
    private Long id;

    @NotNull
    @Min(value = 1, message = "Stock quantity must be greater than zero")
    private Long stockQuantity;

    @NotNull @NotBlank
    private String stockCode;

    @NotNull @NotBlank
    private String stockName;

    @NotNull @NotBlank
    private String stockCategory;

    private ProductAndStockStatus productAndStockStatus;

    @NotNull @NotBlank
    private String stockBranch;

    private String stockDescription;
}
