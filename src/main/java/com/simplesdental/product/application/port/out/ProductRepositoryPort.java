package com.simplesdental.product.application.port.out;

import com.simplesdental.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ProductRepositoryPort {
    Page<Product> findAllWithCategory(Pageable pageable);
    Optional<Product> findById(Long id);
    Product save(Product product);
    Optional<Product> findByNameIgnoreCase(String name);
}