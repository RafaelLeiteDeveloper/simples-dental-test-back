package com.simplesdental.product.infrastructure.dto.response;

import lombok.Data;

@Data
public class CategoryWithoutProductsResponse {
    private Long id;
    private String name;
    private String description;
}
