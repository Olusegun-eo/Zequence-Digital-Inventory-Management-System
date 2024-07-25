package com.zequence.ZequenceIms.prodStocDaosDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductCategoryDto {
    private int id;
    private String productCategoryTitle;
    private String description;
}
