package com.zequence.ZequenceIms.mappers;


import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductDTO;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductDao;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO convertDaoToDto(ProductDao productDao) {
        if (productDao == null) {
            throw new IllegalArgumentException("ProductDao cannot be null");
        }

        return ProductDTO.builder()
                .id(productDao.getId())
                .productName(productDao.getProductName())
                .categories(productDao.getProductCategories())
                .productQuantity(productDao.getProductQuantity())
                .productAndStockStatus(ProductAndStockStatus.valueOf(productDao.getProductAndStockStatus().getAvailability()))
                .productBranch(productDao.getProductBranch())
                .productDescription(productDao.getProductDescription())
                .productThreshHoldLimit(productDao.getProductThreshHoldLimit())
                .build();
    }

    public ProductDao convertDtoToDao(ProductDTO productDto) {
        if (productDto == null) {
            throw new IllegalArgumentException("ProductDTO cannot be null");
        }

        return ProductDao.builder()
                .id(productDto.getId())
                .productName(productDto.getProductName())
                .productCategories(productDto.getCategories())
                .productQuantity(productDto.getProductQuantity())
                .productAndStockStatus(ProductAndStockStatus.valueOf(productDto.getProductAndStockStatus().getAvailability()))
                .productBranch(productDto.getProductBranch())
                .productDescription(productDto.getProductDescription())
                .productThreshHoldLimit(productDto.getProductThreshHoldLimit())
                .build();
    }

    public List<ProductDTO> convertDaosToDtos(List<ProductDao> productDaos) {
        if (productDaos == null) {
            return Collections.emptyList();
        }

        return productDaos.stream()
                .map(this::convertDaoToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDao> convertDtosToDaos(List<ProductDTO> productDtos) {
        if (productDtos == null) {
            return Collections.emptyList();
        }
        return productDtos.stream()
                .map(this::convertDtoToDao)
                .collect(Collectors.toList());
    }
}

