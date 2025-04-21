package com.simplesdental.product.application.usecase;

import com.simplesdental.product.application.port.in.GetProductUseCase;
import com.simplesdental.product.application.port.in.ManageProductUseCase;
import com.simplesdental.product.domain.model.Product;
import com.simplesdental.product.infrastructure.dto.ProductOutput;
import com.simplesdental.product.infrastructure.dto.request.ProductRequest;
import com.simplesdental.product.infrastructure.mapper.ProductMapper;
import com.simplesdental.product.infrastructure.repository.CategoryRepository;
import com.simplesdental.product.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductUseCase implements GetProductUseCase, ManageProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductOutput> getAllProducts(Pageable pageable) {
        return productRepository.findAllWithCategory(pageable)
                                .map(productMapper::toOutput);
    }

    @Override
    public ProductOutput getProductById(Long id) {
        Product product = productRepository.getProductByIdOrElseThrow(id);
        return productMapper.toOutput(product);
    }

    @Override
    public ProductOutput createProduct(ProductRequest request) {
        this.productRepository.existsByNameThenThrow(request.getName(), null);
        this.productRepository.existsByCodeThenThrow(request.getCode(), null);
        categoryRepository.notExistsByIdThenThrow(request.getCategoryId());
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toOutput(savedProduct);
    }

    @Override
    public ProductOutput updateProduct(Long id, ProductRequest request) {
        productRepository.existsByNameThenThrow(request.getName(), id);
        this.productRepository.existsByCodeThenThrow(request.getCode(), id);
        categoryRepository.notExistsByIdThenThrow(request.getCategoryId());
        Product existingProduct = productRepository.getProductByIdOrElseThrow(id);
        productMapper.updateEntityFromDto(request, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toOutput(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.getProductByIdOrElseThrow(id);
        productRepository.delete(product);
    }

}
