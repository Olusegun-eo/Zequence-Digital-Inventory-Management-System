package com.zequence.ZequenceIms.mappers;

import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import com.zequence.ZequenceIms.prodStocDaosDtos.StockDTO;
import com.zequence.ZequenceIms.prodStocDaosDtos.StockDao;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockMapper {
    public StockDTO convertDaoToDto(StockDao stockDao) {
        if (stockDao == null) {
            throw new IllegalArgumentException("StockDao cannot be null");
        }
        return StockDTO.builder()
                .id(stockDao.getId())
                .stockName(stockDao.getStockName())
                .stockCategory(stockDao.getStockCategory())
                .stockQuantity(stockDao.getStockQuantity())
                .productAndStockStatus(ProductAndStockStatus.valueOf(stockDao.getProductAndStockStatus().getAvailability()))
                .stockBranch(stockDao.getStockBranch())
                .stockDescription(stockDao.getStockDescription())
                .build();
    }

    public StockDao convertDtoToDao(StockDTO stockDto) {
        if (stockDto == null) {
            throw new IllegalArgumentException("StockDTO cannot be null");
        }
        return StockDao.builder()
                .id(stockDto.getId())
                .stockName(stockDto.getStockName())
                .stockCategory(stockDto.getStockCategory())
                .stockQuantity(stockDto.getStockQuantity())
                .productAndStockStatus(ProductAndStockStatus.valueOf(stockDto.getProductAndStockStatus().getAvailability()))
                .stockBranch(stockDto.getStockBranch())
                .stockDescription(stockDto.getStockDescription())
                .build();
    }
    public List<StockDTO> convertDaosToDtos(List<StockDao> stockDaos) {
        if (stockDaos == null) {
            return Collections.emptyList();
        }
        return stockDaos.stream()
                .map(this::convertDaoToDto)
                .collect(Collectors.toList());
    }
    public List<StockDao> convertDtosToDaos(List<StockDTO> stockDtos) {
        if (stockDtos == null) {
            return Collections.emptyList();
        }
        return stockDtos.stream()
                .map(this::convertDtoToDao)
                .collect(Collectors.toList());
    }
}


