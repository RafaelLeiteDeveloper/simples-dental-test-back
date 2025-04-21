package com.simplesdental.product.application.port.in;

import com.simplesdental.product.infrastructure.dto.CategoryOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetCategoryUseCase {
    Page<CategoryOutput> getAllCategories(Pageable pageable);
    CategoryOutput getCategoryById(Long id);
}