package com.simplesdental.product.infrastructure.repository;

import com.simplesdental.product.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdWithProducts(@Param("id") Long id);

    @EntityGraph(attributePaths = "products")
    @Query("SELECT c FROM Category c")
    Page<Category> findAllWithProducts(Pageable pageable);

    Optional<Category> findByNameIgnoreCase(String name);

    default void notExistsByIdThenThrow(Long id){
        if (!existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    default Category getCategoryOrElseThrow(Long id){
        return this.findByIdWithProducts(id)
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    default void existsByNameThenThrow(String name, Long id){
        this.findByNameIgnoreCase(name)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name already exists");
            });
    }

}