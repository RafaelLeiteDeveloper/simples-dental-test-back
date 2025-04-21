package com.simplesdental.product.infrastructure.dto;

import com.simplesdental.product.domain.model.Product;
import lombok.Data;
import java.util.List;

@Data
public class CategoryOutput {
    private Long id;
    private String name;
    private String description;
    private List<Product> products;
}
