package com.example.demo.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private String amount ;
    private Long categoryId;
    private String categoryName;
    private Integer size;
    private Integer page;
}
