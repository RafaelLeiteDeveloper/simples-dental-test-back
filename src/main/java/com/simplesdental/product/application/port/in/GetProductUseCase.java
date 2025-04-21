package com.simplesdental.product.application.port.in;

import com.simplesdental.product.infrastructure.dto.ProductOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProductUseCase {
    Page<ProductOutput> getAllProducts(Pageable pageable);
    ProductOutput getProductById(Long id);
}