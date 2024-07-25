package com.zequence.ZequenceIms.service.appProduct;


import com.zequence.ZequenceIms.entity.Stock;
import com.zequence.ZequenceIms.enums.ProductAndStockStatus;
import com.zequence.ZequenceIms.exceptions.OutOfStockException;
import com.zequence.ZequenceIms.exceptions.ProductNotFoundException;
import com.zequence.ZequenceIms.mappers.ProductHistoryMapper;
import com.zequence.ZequenceIms.mappers.ProductMapper;
import com.zequence.ZequenceIms.mappers.StockMapper;
import com.zequence.ZequenceIms.prodStocDaosDtos.*;
import com.zequence.ZequenceIms.repository.ProductHistoryRepository;
import com.zequence.ZequenceIms.repository.ProductRepository;
import com.zequence.ZequenceIms.repository.StockRepository;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ProductServiceInterfaceImpl implements ProductServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceInterfaceImpl.class);
    private final ProductMapper productMapper;
    private  final StockMapper stockMapper;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final CacheManager cacheManager;
    private final ProductHistoryMapper productHistoryMapper;
    private final ProductHistoryRepository productHistoryRepository;

    @Transactional
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        logger.info("Creating product: {}", productDTO);

        // Validate input
        if (productDTO == null || productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
            logger.error("Invalid product data: {}", productDTO);
            throw new IllegalArgumentException("Invalid product data.");
        }
        // Check if product ID already exists
        if (productDTO.getId() != null && productRepository.existsById(productDTO.getId())) {
            logger.error("Product with ID {} already exists", productDTO.getId());
            throw new IllegalArgumentException("Product with ID " + productDTO.getId() + " already exists.");
        }
        // Convert DTO to DAO
        ProductDao productDao = productMapper.convertDtoToDao(productDTO);

        // Save product
        ProductDao savedProduct = productRepository.save(productDao);

        // Convert back to DTO
        ProductDTO savedProductDTO = productMapper.convertDaoToDto(savedProduct);

        logger.info("Product created successfully: {}", savedProductDTO);
        return savedProductDTO;
    }

    @Transactional
    @Override
    public List<ProductDTO> getListProducts() {
        logger.info("Fetching all products");

        List<ProductDao> productDaos = productRepository.findAll();

        // Check if no products are found
        if (productDaos.isEmpty()) {
            logger.warn("No products found");
            return Collections.emptyList();
        }
        // Set availability for each product
        setAvailability(productDaos);

        // Convert the list of ProductDao to ProductDTO
        List<ProductDTO> productDTOs = productMapper.convertDaosToDtos(productDaos);

        logger.info("Fetched {} products", productDTOs.size());
        return productDTOs;
    }
    private void setAvailability(List<ProductDao> products) {
        Map<Long, Long> remainingStock = new HashMap<>();

        products.forEach(product -> {
            for (StockDao stock : product.getStocks()) {
                Long stockId = stock.getId();
                if (remainingStock.get(stockId) == null) {
                    Optional<Stock> stockDetails = stockRepository.findById(stockId);
                    if (stockDetails.isPresent() && stockDetails.get().getStockQuantity() >= stock.getStockQuantity()) {
                        stock.setProductAndStockStatus(ProductAndStockStatus.INSTOCK);
                        remainingStock.put(stockId, stockDetails.get().getStockQuantity() - stock.getStockQuantity());
                    } else {
                        stock.setProductAndStockStatus(ProductAndStockStatus.OUTOFSTOCK);
                    }
                } else {
                    long remainingQuantity = remainingStock.get(stockId);
                    if (remainingQuantity >= stock.getStockQuantity()) {
                        stock.setProductAndStockStatus(ProductAndStockStatus.INSTOCK);
                        remainingStock.put(stockId, remainingQuantity - stock.getStockQuantity());
                    } else {
                        stock.setProductAndStockStatus(ProductAndStockStatus.OUTOFSTOCK);
                    }
                }
            }
            // Update the overall product status based on stock thresholds
            long totalStock = product.getStocks().stream().mapToLong(StockDao::getStockQuantity).sum();
            if (totalStock >= product.getProductThreshHoldLimit()) {
                product.setProductAndStockStatus(ProductAndStockStatus.INSTOCK);
            } else if (totalStock > 0 && totalStock < product.getProductThreshHoldLimit()) {
                product.setProductAndStockStatus(ProductAndStockStatus.RUNNINGOUT);
            } else {
                product.setProductAndStockStatus(ProductAndStockStatus.OUTOFSTOCK);
            }
        });
    }

    @Transactional
    @Override
    public ProductDTO getProduct(Long productId) throws ProductNotFoundException {
        logger.info("Fetching product with id: {}", productId);

        if (productId == null) {
            logger.error("Product ID cannot be null");
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        String cacheKey = "product_" + productId;
        Cache cache = cacheManager.getCache("products");

        ProductDTO cachedProduct = cache != null ? cache.get(cacheKey, ProductDTO.class) : null;
        if (cachedProduct != null) {
            logger.info("Product found in cache: {}", cachedProduct);
            return cachedProduct;
        }
        Optional<ProductDao> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            logger.error("Product with id: {} does not exist", productId);
            throw new ProductNotFoundException("Product with id: " + productId + " does not exist");
        }

        ProductDao product = productOptional.get();
        setAvailability(Collections.singletonList(product));
        ProductDTO productDTO = productMapper.convertDaoToDto(product);

        if (cache != null) {
            cache.put(cacheKey, productDTO);
        }

        logger.info("Product fetched successfully: {}", productDTO);
        return productDTO;
    }

    @Transactional
    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) throws ProductNotFoundException {
        logger.info("Updating product with id: {}", productId);

        if (productId == null) {
            logger.error("Product ID cannot be null");
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productDTO == null || productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
            logger.error("Invalid product data: {}", productDTO);
            throw new IllegalArgumentException("Invalid product data.");
        }

        Optional<ProductDao> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            logger.error("Product with id: {} does not exist", productId);
            throw new ProductNotFoundException("Product with id: " + productId + " does not exist");
        }

        ProductDao existingProduct = productOptional.get();

        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setProductDescription(productDTO.getProductDescription());
        existingProduct.setProductThreshHoldLimit(productDTO.getProductThreshHoldLimit());
        existingProduct.setProductBranch(productDTO.getProductBranch());
        existingProduct.setProductCategories(productDTO.getCategories());
        existingProduct.setProductQuantity(productDTO.getProductQuantity());
        existingProduct.setProductAndStockStatus(productDTO.getProductAndStockStatus());

        List<StockDao> updatedStocks = productDTO.getStockDtos().stream()
                .map(stockMapper::convertDtoToDao)
                .collect(Collectors.toList());

        existingProduct.setStocks(updatedStocks);

        ProductDao updatedProduct = productRepository.save(existingProduct);

        setAvailability(Collections.singletonList(updatedProduct));

        ProductDTO updatedProductDTO = productMapper.convertDaoToDto(updatedProduct);

        logger.info("Product updated successfully: {}", updatedProductDTO);
        return updatedProductDTO;
    }

    @Transactional
    @Override
    public List<ProductHistoryDTO> getProductHistory(Long productId) throws ProductNotFoundException {
        logger.info("Fetching product history for id: {}", productId);

        if (productId == null) {
            logger.error("Product ID cannot be null");
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        Optional<ProductHistoryDao> productOptional = productHistoryRepository.findById(productId);
        if (productOptional.isEmpty()) {
            logger.error("Product with id: {} does not exist", productId);
            throw new ProductNotFoundException("Product with id: " + productId + " does not exist");
        }

        List<ProductHistoryDao> historyDaos = productHistoryRepository.findByProductId(productId);

        List<ProductHistoryDTO> productHistoryDTOs = historyDaos.stream()
                .map(productHistoryMapper::convertDaoToDto)
                .collect(Collectors.toList());

        logger.info("Fetched {} history records for product id: {}", productHistoryDTOs.size(), productId);
        return productHistoryDTOs;
    }
    @Transactional
    @Override
    public boolean deleteProduct(Long productId) {
        logger.info("Deleting product with id: {}", productId);
        Optional<ProductDao> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            logger.error("Product With Id: {} does not exist", productId);
            throw new ProductNotFoundException("Product With Id: " + productId + " does not exist");
        }

        ProductDao product = productOptional.get();

        List<Stock> stocksToUpdate = new ArrayList<>();
        for (StockDao stock : product.getStocks()) {
            Optional<Stock> stockOptional = stockRepository.findById(stock.getId());
            if (stockOptional.isEmpty() || stock.getStockQuantity() > stockOptional.get().getStockQuantity()) {
                logger.error("Insufficient stocks found with stockId: {} for product: {}", stock.getId(), productId);
                throw new OutOfStockException("Insufficient stocks: " + stock.getId() + " for the product: " + productId);
            }
            Stock stockToUpdate = stockOptional.get();
            stockToUpdate.setStockQuantity(stockToUpdate.getStockQuantity() - stock.getStockQuantity());
            stocksToUpdate.add(stockToUpdate);
        }
        stockRepository.saveAll(stocksToUpdate);
        productRepository.deleteById(productId);

        logger.info("Product with id: {} deleted successfully", productId);
        return true;
    }

}