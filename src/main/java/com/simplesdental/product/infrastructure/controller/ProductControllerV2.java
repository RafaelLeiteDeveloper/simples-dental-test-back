package com.simplesdental.product.infrastructure.controller;

import com.simplesdental.product.application.usecase.ProductUseCase;
import com.simplesdental.product.infrastructure.dto.*;
import com.simplesdental.product.infrastructure.dto.request.ProductRequest;
import com.simplesdental.product.infrastructure.dto.response.ProductResponse;
import com.simplesdental.product.infrastructure.dto.response.ProductResponseV2;
import com.simplesdental.product.infrastructure.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v2/products")
@Tag(name = "Products", description = "Operations related to product management")
public class ProductControllerV2 {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;

    @Operation(summary = "Get all products", description = "Returns a paginated list of all registered products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public Page<ProductResponseV2> getAllProductsV2(Pageable pageable) {
        log.info("Fetching all products V2 - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<ProductOutput> outputs = productUseCase.getAllProducts(pageable);
        return outputs.map(productMapper::toResponseV2);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a product using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ProductResponseV2 getProductByIdV2(@PathVariable Long id) {
        log.debug("Fetching product with ID: {}", id);
        var productOutput = productUseCase.getProductById(id);
        return productMapper.toResponseV2(productOutput);
    }

    @Operation(summary = "Create a new product", description = "Creates a new product and returns the created resource.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseV2 createProductV2(@Valid @RequestBody ProductRequest request) {
        log.info("Creating product - name: {}", request.getName());
        ProductOutput response = productUseCase.createProduct(request);
        log.debug("Product created: {}", response);
        return productMapper.toResponseV2(response);
    }

    @Operation(summary = "Update an existing product", description = "Updates the product with the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseV2> updateProductV2(@PathVariable Long id, @RequestBody ProductRequest request) {
        log.info("Updating product - ID: {}, data: {}", id, request);
        ProductOutput response = productUseCase.updateProduct(id, request);
        log.debug("Product updated: {}", response);
        return ResponseEntity.ok(productMapper.toResponseV2(response));
    }

    @Operation(summary = "Delete a product", description = "Deletes a product using its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.warn("Deleting product - ID: {}", id);
        productUseCase.deleteProduct(id);
        log.info("Product deleted - ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
