package com.simplesdental.product.infrastructure.controller;

import com.simplesdental.product.application.usecase.CategoryUseCase;
import com.simplesdental.product.infrastructure.dto.CategoryOutput;
import com.simplesdental.product.infrastructure.dto.request.CategoryRequest;
import com.simplesdental.product.infrastructure.dto.response.CategoryResponse;
import com.simplesdental.product.infrastructure.dto.response.CategoryWithoutProductsResponse;
import com.simplesdental.product.infrastructure.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Operations related to category management")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Get all categories", description = "Returns a paginated list of all categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        log.info("Fetching categories: pageNumber={}, pageSize={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<CategoryOutput> outputs = categoryUseCase.getAllCategories(pageable);
        return outputs.map(categoryMapper::toResponse);
    }

    @Operation(summary = "Get category by ID", description = "Returns a category by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        log.debug("Fetching category with ID: {}", id);
        return ResponseEntity.ok(categoryMapper.toResponse(categoryUseCase.getCategoryById(id)));
    }

    @Operation(summary = "Create a new category", description = "Creates a new category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryWithoutProductsResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        log.info("Creating category: {}", request.getName());
        return categoryMapper.toSaveResponse(categoryUseCase.createCategory(request));
    }

    @Operation(summary = "Update a category", description = "Updates an existing category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryWithoutProductsResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        log.info("Updating category ID: {} with data: {}", id, request);
        return ResponseEntity.ok(categoryMapper.toSaveResponse(categoryUseCase.updateCategory(id, request)));
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        log.warn("Deleting category with ID: {}", id);
        categoryUseCase.deleteCategory(id);
    }

}
