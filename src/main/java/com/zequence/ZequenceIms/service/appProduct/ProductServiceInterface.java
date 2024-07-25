package com.zequence.ZequenceIms.service.appProduct;

import com.zequence.ZequenceIms.exceptions.ProductNotFoundException;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductDTO;
import com.zequence.ZequenceIms.entity.Product;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductHistoryDTO;

import java.util.List;

public interface ProductServiceInterface {

    ProductDTO addProduct(ProductDTO product); //throws ProductAlreadyExistsException;
    List<ProductDTO> getListProducts();
    ProductDTO getProduct(Long id) throws ProductNotFoundException;
    ProductDTO updateProduct(Long productId, ProductDTO productDTO) throws ProductNotFoundException;
    List<ProductHistoryDTO> getProductHistory(Long productId);
    boolean deleteProduct(Long productId);
}
