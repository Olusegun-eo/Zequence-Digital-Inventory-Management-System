package com.zequence.ZequenceIms.controller;


import com.zequence.ZequenceIms.exceptions.OutOfStockException;
import com.zequence.ZequenceIms.exceptions.ProductNotFoundException;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductDTO;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductHistoryDTO;
import com.zequence.ZequenceIms.service.appProduct.ProductServiceInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(path = "/api/products")
@Validated
@AllArgsConstructor
public class ProductController {

    private final ProductServiceInterface productService;

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.addProduct(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getListProducts() {
        List<ProductDTO> products = productService.getListProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProduct(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ProductHistoryDTO>> getProductHistory(@PathVariable Long id) {
        try {
            List<ProductHistoryDTO> productHistory = productService.getProductHistory(id);
            if (productHistory.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productHistory, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (OutOfStockException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}