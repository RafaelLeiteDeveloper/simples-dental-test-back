package com.simplesdental.product.application.port.in;

import com.simplesdental.product.infrastructure.dto.CategoryOutput;
import com.simplesdental.product.infrastructure.dto.request.CategoryRequest;

public interface ManageCategoryUseCase {
    CategoryOutput createCategory(CategoryRequest request);
    CategoryOutput updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}