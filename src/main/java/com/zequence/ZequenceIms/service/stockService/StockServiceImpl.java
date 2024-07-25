package com.zequence.ZequenceIms.service.stockService;

import com.zequence.ZequenceIms.entity.Stock;
import com.zequence.ZequenceIms.entity.StockCategory;
import com.zequence.ZequenceIms.mappers.StockMapper;
import com.zequence.ZequenceIms.prodStocDaosDtos.StockDTO;
import com.zequence.ZequenceIms.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class StockServiceImpl implements StockServiceInterface{

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
    private final StockMapper stockMapper;
    private final StockRepository stockRepository;
    private final CacheManager cacheManager;
    @Override
    public StockDTO addStock(StockDTO stockDTO) {
        return null;
    }

    @Override
    public List<StockDTO> getAllStocks() {
        return null;
    }

    @Override
    public StockDTO getStock(Long stockId) throws StockNotFoundException {
        return null;
    }

    @Transactional
    @Override
    public StockDTO updateStock(Long stockId, StockDTO stockDTO) throws StockNotFoundException {
        logger.info("Updating stock with id: {}", stockId);

        if (stockId == null) {
            logger.error("Stock ID cannot be null");
            throw new IllegalArgumentException("Stock ID cannot be null");
        }

        if (stockDTO == null || stockDTO.getStockQuantity() <= 0) {
            logger.error("Invalid stock data: {}", stockDTO);
            throw new IllegalArgumentException("Invalid stock data.");
        }

        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        if (stockOptional.isEmpty()) {
            logger.error("Stock with id: {} does not exist", stockId);
            throw new StockNotFoundException("Stock with id: " + stockId + " does not exist");
        }

        Stock existingStock = stockOptional.get();

        existingStock.setStockName(stockDTO.getStockName());
        existingStock.setStockQuantity(stockDTO.getStockQuantity());
        existingStock.setStockStatus(stockDTO.getStockStatus()); // Assuming StockStatus is set in the DTO
        existingStock.setStockBranch(stockDTO.getStockBranch());
        existingStock.setStockDescription(stockDTO.getStockDescription());
        existingStock.setStockDate(stockDTO.getStockDate()); // Assuming StockDate is set in the DTO

        // Update StockCategory if provided
        if (stockDTO.getStockCategory() != null && stockDTO.getStockCategory().getId() != null) {
            StockCategory updatedCategory = stockCategoryRepository.findById(stockDTO.getStockCategory().getId())
                    .orElseGet(() -> new StockCategory(stockDTO.getStockCategory().getStockCategoryTitle()));
            existingStock.setStockCategory(updatedCategory);
        }

        // Save updated stock
        Stock savedStock = stockRepository.save(existingStock);

        // Convert back to DTO
        StockDTO savedStockDTO = stockMapper.convertDaoToDto(savedStock);

        logger.info("Stock updated successfully: {}", savedStockDTO);
        return savedStockDTO;
    }

    @Transactional
    @Override
    public void deleteStock(Long stockId) throws StockNotFoundException {
        logger.info("Deleting stock with id: {}", stockId);

        if (stockId == null) {
            logger.error("Stock ID cannot be null");
            throw new IllegalArgumentException("Stock ID cannot be null");
        }

        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        if (stockOptional.isEmpty()) {
            logger.error("Stock with id: {} does not exist", stockId);
            throw new StockNotFoundException("Stock with id: " + stockId + " does not exist");
        }

        stockRepository.deleteById(stockId);

        logger.info("Stock deleted successfully (id: {})", stockId);
    }

}
