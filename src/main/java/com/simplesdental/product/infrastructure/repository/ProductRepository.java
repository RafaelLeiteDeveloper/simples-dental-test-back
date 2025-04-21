package com.simplesdental.product.infrastructure.repository;

import com.simplesdental.product.domain.model.Product;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category",
            countQuery = "SELECT count(p) FROM Product p")
    Page<Product> findAllWithCategory(Pageable pageable);

    Optional<Product> findByNameIgnoreCase(String name);

    Optional<Product> findByCode(String code);

    default void existsByNameThenThrow(String name, Long id){
        this.findByNameIgnoreCase(name)
            .filter( product -> !product.getId().equals(id))
            .ifPresent( product -> {
                throw new DuplicateKeyException("Product name already exists");
            });
    }

    default Product getProductByIdOrElseThrow(Long id){
        return this.findById(id)
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    default void existsByCodeThenThrow(String code, Long id){
        this.findByCode(code)
            .filter( product -> !product.getId().equals(id))
            .ifPresent( product -> {
                    throw new DuplicateKeyException("Code already exists");
            });
    }

}