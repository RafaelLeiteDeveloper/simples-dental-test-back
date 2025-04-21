package com.simplesdental.product.infrastructure.dto.response;

import com.simplesdental.product.infrastructure.dto.response.field.ProductResponse;
import lombok.Data;
import java.util.List;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private List<ProductResponse> products;
}
