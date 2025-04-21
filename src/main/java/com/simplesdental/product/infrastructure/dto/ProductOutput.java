package com.simplesdental.product.infrastructure.dto;

import com.simplesdental.product.domain.model.Category;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductOutput {
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean status;
    private String code;
    private Category category;
}
