package com.simplesdental.product.useCase;

import com.simplesdental.product.application.usecase.ProductUseCase;
import com.simplesdental.product.domain.model.Product;
import com.simplesdental.product.infrastructure.dto.ProductOutput;
import com.simplesdental.product.infrastructure.dto.request.ProductRequest;
import com.simplesdental.product.infrastructure.mapper.ProductMapper;
import com.simplesdental.product.infrastructure.repository.CategoryRepository;
import com.simplesdental.product.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductUseCaseTest {

    @InjectMocks
    private ProductUseCase productUseCase;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllProducts() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        ProductOutput output = new ProductOutput();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAllWithCategory(pageable)).thenReturn(productPage);
        when(productMapper.toOutput(product)).thenReturn(output);

        // When
        Page<ProductOutput> result = productUseCase.getAllProducts(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findAllWithCategory(pageable);
    }

    @Test
    void shouldGetProductById() {
        // Given
        Product product = new Product();
        product.setId(1L);
        ProductOutput output = new ProductOutput();

        when(productRepository.getProductByIdOrElseThrow(1L)).thenReturn(product);
        when(productMapper.toOutput(product)).thenReturn(output);

        // When
        ProductOutput result = productUseCase.getProductById(1L);

        // Then
        assertNotNull(result);
        verify(productRepository).getProductByIdOrElseThrow(1L);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // Given
        ProductRequest request = new ProductRequest();
        request.setName("New Product");
        request.setCode("PROD-001");
        request.setCategoryId(10L);

        Product product = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        ProductOutput output = new ProductOutput();

        doNothing().when(productRepository).existsByNameThenThrow("New Product", null);
        doNothing().when(productRepository).existsByCodeThenThrow("PROD-001", null);
        doNothing().when(categoryRepository).notExistsByIdThenThrow(10L);
        when(productMapper.toEntity(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toOutput(savedProduct)).thenReturn(output);

        // When
        ProductOutput result = productUseCase.createProduct(request);

        // Then
        assertNotNull(result);
        verify(productRepository).save(product);
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        // Given
        Long id = 1L;
        ProductRequest request = new ProductRequest();
        request.setName("Updated Product");
        request.setCode("PROD-002");
        request.setCategoryId(20L);

        Product product = new Product();
        product.setId(id);
        ProductOutput output = new ProductOutput();

        when(productRepository.getProductByIdOrElseThrow(id)).thenReturn(product);
        doNothing().when(productRepository).existsByNameThenThrow("Updated Product", id);
        doNothing().when(productRepository).existsByCodeThenThrow("PROD-002", id);
        doNothing().when(categoryRepository).notExistsByIdThenThrow(20L);
        doNothing().when(productMapper).updateEntityFromDto(request, product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toOutput(product)).thenReturn(output);

        // When
        ProductOutput result = productUseCase.updateProduct(id, request);

        // Then
        assertNotNull(result);
        verify(productRepository).save(product);
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        // Given
        Long id = 1L;
        Product product = new Product();
        product.setId(id);

        when(productRepository.getProductByIdOrElseThrow(id)).thenReturn(product);
        doNothing().when(productRepository).delete(product);

        // When / Then
        assertDoesNotThrow(() -> productUseCase.deleteProduct(id));
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowDuplicateKeyExceptionWhenProductNameAlreadyExists() {
        // Given
        ProductRequest request = new ProductRequest();
        request.setName("Duplicate Product");
        request.setCode("PROD-003");
        request.setCategoryId(30L);

        doThrow(DuplicateKeyException.class)
                .when(productRepository)
                .existsByNameThenThrow("Duplicate Product", null);

        // When / Then
        assertThrows(DuplicateKeyException.class, () -> productUseCase.createProduct(request));
    }
}
