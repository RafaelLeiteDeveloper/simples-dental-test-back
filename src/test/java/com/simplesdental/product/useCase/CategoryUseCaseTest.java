package com.simplesdental.product.useCase;

import com.simplesdental.product.application.usecase.CategoryUseCase;
import com.simplesdental.product.domain.model.Category;
import com.simplesdental.product.domain.model.Product;
import com.simplesdental.product.infrastructure.dto.CategoryOutput;
import com.simplesdental.product.infrastructure.dto.request.CategoryRequest;
import com.simplesdental.product.infrastructure.mapper.CategoryMapper;
import com.simplesdental.product.infrastructure.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryUseCaseTest {

    @InjectMocks
    private CategoryUseCase categoryUseCase;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllCategories() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category();
        CategoryOutput output = new CategoryOutput();
        Page<Category> page = new PageImpl<>(List.of(category));

        when(categoryRepository.findAllWithProducts(pageable)).thenReturn(page);
        when(categoryMapper.toResponseOutput(category)).thenReturn(output);

        // When
        Page<CategoryOutput> result = categoryUseCase.getAllCategories(pageable);

        // Then
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.getTotalElements())
        );
        verify(categoryRepository).findAllWithProducts(pageable);
        verify(categoryMapper).toResponseOutput(category);
    }

    @Test
    void shouldGetCategoryById() {
        // Given
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        CategoryOutput output = new CategoryOutput();

        when(categoryRepository.getCategoryOrElseThrow(id)).thenReturn(category);
        when(categoryMapper.toResponseOutput(category)).thenReturn(output);

        // When
        CategoryOutput result = categoryUseCase.getCategoryById(id);

        // Then
        assertNotNull(result);
        verify(categoryRepository).getCategoryOrElseThrow(id);
        verify(categoryMapper).toResponseOutput(category);
    }

    @Test
    void shouldCreateCategory() {
        // Given
        CategoryRequest request = new CategoryRequest();
        request.setName("New Category");

        Category category = new Category();
        Category savedCategory = new Category();
        savedCategory.setId(1L);

        CategoryOutput output = new CategoryOutput();

        doNothing().when(categoryRepository).existsByNameThenThrow("New Category", null);
        when(categoryMapper.toEntity(request)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toResponseOutput(savedCategory)).thenReturn(output);

        // When
        CategoryOutput result = categoryUseCase.createCategory(request);

        // Then
        assertNotNull(result);
        verify(categoryRepository).existsByNameThenThrow("New Category", null);
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldUpdateCategory() {
        // Given
        Long id = 1L;
        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Category");

        Category category = new Category();
        category.setId(id);
        CategoryOutput output = new CategoryOutput();

        when(categoryRepository.getCategoryOrElseThrow(id)).thenReturn(category);
        doNothing().when(categoryRepository).existsByNameThenThrow("Updated Category", id);
        doNothing().when(categoryMapper).updateEntityFromDto(request, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseOutput(category)).thenReturn(output);

        // When
        CategoryOutput result = categoryUseCase.updateCategory(id, request);

        // Then
        assertNotNull(result);
        verify(categoryRepository).existsByNameThenThrow("Updated Category", id);
        verify(categoryMapper).updateEntityFromDto(request, category);
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldDeleteCategoryWithoutProducts() {
        // Given
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        category.setProducts(Collections.emptyList());

        when(categoryRepository.getCategoryOrElseThrow(id)).thenReturn(category);
        doNothing().when(categoryRepository).delete(category);

        // When / Then
        assertDoesNotThrow(() -> categoryUseCase.deleteCategory(id));
        verify(categoryRepository).delete(category);
    }

    @Test
    void shouldNotDeleteCategoryWithProducts() {
        // Given
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        category.setProducts(List.of(new Product()));

        when(categoryRepository.getCategoryOrElseThrow(id)).thenReturn(category);

        // When / Then
        assertThrows(RuntimeException.class, () -> categoryUseCase.deleteCategory(id));
    }
}
