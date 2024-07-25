package com.zequence.ZequenceIms.mappers;


import com.zequence.ZequenceIms.prodStocDaosDtos.ProductHistoryDTO;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductHistoryDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductHistoryMapper {
    public ProductHistoryDTO convertDaoToDto(ProductHistoryDao productHistoryDao) {
        return ProductHistoryDTO.builder()
                .id(productHistoryDao.getProductId())
                .productDescription(productHistoryDao.getProductDescription())
                .changeTimestamp(productHistoryDao.getChangeTimestamp())
                .build();
    }
    public ProductHistoryDao convertDtoToDao(ProductHistoryDTO productHistoryDto) {
        return ProductHistoryDao.builder()
                .productId(productHistoryDto.getId())
                .productDescription(productHistoryDto.getProductDescription())
                .changeTimestamp(productHistoryDto.getChangeTimestamp())
                .build();
    }

    public List<ProductHistoryDTO> convertDaosToDtos(List<ProductHistoryDao> productHistoryDaos) {
        return productHistoryDaos.stream()
                .map(this::convertDaoToDto)
                .collect(Collectors.toList());
    }

    public List<ProductHistoryDao> convertDtosToDaos(List<ProductHistoryDTO> productHistoryDtos) {
        return productHistoryDtos.stream()
                .map(this::convertDtoToDao)
                .collect(Collectors.toList());
    }
}

