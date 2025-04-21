package com.simplesdental.product.infrastructure.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @Schema(description = "Product name", example = "Electric Toothbrush")
    private String name;

    @Schema(description = "Product description", example = "Toothbrush with 3 speed modes and charging base")
    private String description;

    @Schema(description = "Product price", example = "129.90")
    private BigDecimal price;

    @Schema(description = "Product status", example = "true")
    private Boolean status;

    @Schema(description = "Product code", example = "PROD-001")
    private String code;

    @Schema(description = "Category ID", example = "3")
    private Long categoryId;

}
