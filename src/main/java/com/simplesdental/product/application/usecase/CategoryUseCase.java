package com.simplesdental.product.application.usecase;

import com.simplesdental.product.application.port.in.GetCategoryUseCase;
import com.simplesdental.product.application.port.in.ManageCategoryUseCase;
import com.simplesdental.product.domain.model.Category;
import com.simplesdental.product.infrastructure.dto.CategoryOutput;
import com.simplesdental.product.infrastructure.dto.request.CategoryRequest;
import com.simplesdental.product.infrastructure.mapper.CategoryMapper;
import com.simplesdental.product.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryUseCase implements GetCategoryUseCase, ManageCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryOutput> getAllCategories(Pageable pageable) {
        log.debug("Fetching all categories with pagination: {}", pageable);
        return categoryRepository.findAllWithProducts(pageable)
                                 .map(categoryMapper::toResponseOutput);
    }

    @Override
    public CategoryOutput getCategoryById(Long id) {
        log.debug("Fetching category by ID: {}", id);
        Category category = categoryRepository.getCategoryOrElseThrow(id);
        return categoryMapper.toResponseOutput(category);
    }

    @Override
    public CategoryOutput createCategory(CategoryRequest request) {
        log.info("Creating new category with name: {}", request.getName());
        this.categoryRepository.existsByNameThenThrow(request.getName(), null);
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        log.debug("Category created with ID: {}", savedCategory.getId());
        return categoryMapper.toResponseOutput(savedCategory);
    }

    @Override
    public CategoryOutput updateCategory(Long id, CategoryRequest request) {
        log.info("Updating category ID: {}", id);
        Category existingCategory = categoryRepository.getCategoryOrElseThrow(id);
        this.categoryRepository.existsByNameThenThrow(request.getName(), id);
        categoryMapper.updateEntityFromDto(request, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        log.debug("Category updated: {}", updatedCategory.getId());
        return categoryMapper.toResponseOutput(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category ID: {}", id);
        Category category = categoryRepository.getCategoryOrElseThrow(id);
        hasProductsThenThrow(category);
        categoryRepository.delete(category);
        log.info("Category deleted successfully: {}", id);
    }

    private void hasProductsThenThrow(Category category) {
        if (!category.getProducts().isEmpty()) {
            log.warn("Attempted to delete category with products: {}", category.getId());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete category with associated products"
            );
        }
    }

}
