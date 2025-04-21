package com.simplesdental.product.infrastructure.dto.response;

import com.simplesdental.product.infrastructure.dto.response.field.CategoryResponse;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean status;
    private String code;
    private CategoryResponse category;
}
