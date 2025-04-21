package com.simplesdental.product.application.port.out;

import com.simplesdental.product.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryRepositoryPort {
    Page<Category> findAllWithProducts(Pageable pageable);
    Optional<Category> findById(Long id);
    Category save(Category category);
    Optional<Category> findByNameIgnoreCase(String name);
}