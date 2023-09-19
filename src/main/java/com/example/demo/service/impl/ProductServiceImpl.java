package com.example.demo.service.impl;

import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Category;
import com.example.demo.entity.model.Product;
import com.example.demo.repository.ProductRepositoryService;
import com.example.demo.repository.jpa.CategoryRepositoryJpa;
import com.example.demo.repository.jpa.ProductRepositoryJpa;
import com.example.demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ProductRepositoryService productRepositoryService;
    @Autowired
    ProductRepositoryJpa productRepositoryJpa;
    @Autowired
    CategoryRepositoryJpa categoryRepositoryJpa;

    @Override
    public  Iterable<Product> getAll(ProductDTO productDTO) {
        return  productRepositoryService.getAll(productDTO);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return productRepositoryJpa.findById(id);
    }

    @Override
    public Product create(ProductDTO productDTO) {
     Product product=modelMapper.map(productDTO,Product.class);
      return productRepositoryJpa.save(product);
    }
    @Override
    public String update(Long id, ProductDTO productDTO) {
        Optional<Product> optionalProduct = getById(id);

        if (!optionalProduct.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        Product existingProduct = optionalProduct.get();

        // Tìm đối tượng Category mới dựa trên categoryId trong productDTO
        Long categoryId = productDTO.getCategoryId();
        Category newCategory = categoryRepositoryJpa.findById(categoryId).orElse(null);
        if (newCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        // Cập nhật trường category trong Product để trỏ đến Category mới
        existingProduct.setCategory(newCategory);

        // Lưu lại Product để cập nhật khóa ngoại
        productRepositoryJpa.save(existingProduct);

        return "Product updated successfully";
    }


    @Override
    public void delete(Long id) {
        Optional<Product> optionalProduct = getById(id);

        if (!optionalProduct.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        try {
            productRepositoryJpa.delete(optionalProduct.get());
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete product", e);
        }
    }

}
