package com.example.demo.repository;

import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Category;

public interface CategoryRepositoryService {
    Iterable<Category> getAll(ProductDTO itemParamsEntity);
}
