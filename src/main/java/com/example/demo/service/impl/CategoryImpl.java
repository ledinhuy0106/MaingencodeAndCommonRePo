package com.example.demo.service.impl;

import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Category;
import com.example.demo.repository.CategoryRepositoryService;
import com.example.demo.repository.jpa.CategoryRepositoryJpa;
import com.example.demo.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CategoryImpl implements CategoryService {
    @Autowired
    CategoryRepositoryService categoryRepositoryService;
    @Autowired
    CategoryRepositoryJpa categoryRepositoryJpa;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Iterable<Category> getAll(ProductDTO productDTO) {
        return categoryRepositoryService.getAll(productDTO);
    }

    @Override
    public Optional<Category> getById(Long id) {
        return categoryRepositoryJpa.findById(id);
    }

    @Override
    public Category create(ProductDTO productDTO) {
        Category category=modelMapper.map(productDTO,Category.class);
        return categoryRepositoryJpa.save(category);
    }

    @Override
    public Category update(Long id, ProductDTO productDTO) {
        Optional<Category> optionalProduct = getById(id);

        if (!optionalProduct.isPresent()) {
            return null;
        }
        Category existingCategory = optionalProduct.get();
        modelMapper.map(productDTO, existingCategory);
        return categoryRepositoryJpa.save(existingCategory);
    }

    @Override
    public void delete(Long id) {
        Optional<Category> optionalCategory = getById(id);

        if (!optionalCategory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        try {
            categoryRepositoryJpa.delete(optionalCategory.get());
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete category", e);
        }
    }
}
