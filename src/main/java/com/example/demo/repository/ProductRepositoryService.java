package com.example.demo.repository;

import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryService  {
        Iterable<Product> getAll(ProductDTO itemParamsEntity);
//    Iterable<Product> findByNameContaining(String q, Pageable pageable);
//    Iterable<Product> findByPriceBetween(double price, double price2, Pageable pageable);
//    Iterable<Product> findByPrice(double price, Pageable pageable);
}
