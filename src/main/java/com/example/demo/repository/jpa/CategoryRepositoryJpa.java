package com.example.demo.repository.jpa;

import com.example.demo.entity.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositoryJpa extends JpaRepository<Category,Long> {
}
