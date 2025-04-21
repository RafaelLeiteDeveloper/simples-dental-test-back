package com.simplesdental.product.application.port.in;


import com.simplesdental.product.infrastructure.dto.ProductOutput;
import com.simplesdental.product.infrastructure.dto.request.ProductRequest;

public interface ManageProductUseCase {
    ProductOutput createProduct(ProductRequest request);
    ProductOutput updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}