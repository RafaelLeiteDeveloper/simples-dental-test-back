package com.simplesdental.product.infrastructure.dto.response.field;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean status;
    private String code;
}
