package com.example.demo.controller;

import com.example.demo.common.sqlcommon.Constants;
import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Category;
import com.example.demo.entity.model.Product;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(Constants.API_V1+"categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;


    /**
     *
     * @param productDTO
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<Iterable<Category>> getAllProduct(ProductDTO productDTO) {
        return ResponseEntity.ok(categoryService.getAll(productDTO));
    }

    /**
     *
     * @param productDTO
     * @return ResponseEntity
     */
    @PostMapping("/create")
    public ResponseEntity<Category> create(@RequestBody ProductDTO productDTO) {
        Category createdProduct = categoryService.create(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    @PutMapping ("/update/{id}")
    public ResponseEntity<Category> update(@RequestBody ProductDTO productDTO, Long id) {
        Category category  = categoryService.update(id,productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }


    /**
     *
     * @param id
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Category>> getProductById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     *
     * @param id
     * @return delete
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
