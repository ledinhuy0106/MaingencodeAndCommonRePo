package com.example.demo.service;

import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Category;

import java.util.Optional;

public interface CategoryService {
    Iterable<Category> getAll(ProductDTO productDTO);

    Optional<Category> getById(Long id);

    Category create(ProductDTO productDTO);
    Category update(Long id, ProductDTO productDTO);

    void delete(Long id);
}
