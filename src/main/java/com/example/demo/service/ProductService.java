package com.example.demo.service;

import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Product;

import java.util.Optional;

public interface ProductService {
    public Iterable<Product> getAll(ProductDTO productDTO);


    Optional<Product> getById(Long id);

    Product create(ProductDTO product);
    Object update(Long id,ProductDTO productDTO);

    void delete(Long id);

//    Iterable<Product> findByNameContaining(String q, Pageable pageable);
//    Iterable<Product> findByPriceBetween(double price, double price2, Pageable pageable);
//    Iterable<Product> findByPrice(double price, Pageable pageable);
}
