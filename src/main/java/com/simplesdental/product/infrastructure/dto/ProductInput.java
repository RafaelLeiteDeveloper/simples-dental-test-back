package com.simplesdental.product.infrastructure.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductInput {
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean status;
    private String code;
    private Long categoryId;
}
