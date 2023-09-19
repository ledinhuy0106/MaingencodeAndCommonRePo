package com.example.demo.controller;

import com.example.demo.common.sqlcommon.Constants;
import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(Constants.API_V1+"products")
public class ProductControlle {
    @Autowired
    ProductService productService;


    /**
     *
     * @param productDTO
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<Iterable<Product>> getAllProduct(ProductDTO productDTO) {
        return ResponseEntity.ok(productService.getAll(productDTO));
    }

    /**
     *
     * @param productDTO
     * @return ResponseEntity
     */
    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.create(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
   @PutMapping ("/update/{id}")
    public ResponseEntity<Object> update(@RequestBody ProductDTO productDTO,@PathVariable Long id) {
        productDTO.setId(id);
        Object createdProduct = productService.update(id, productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    /**
     *
     * @param id
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * @param id
     * @return delete
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Success");
    }

}