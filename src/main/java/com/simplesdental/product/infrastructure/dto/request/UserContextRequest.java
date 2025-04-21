package com.simplesdental.product.infrastructure.dto.request;

import com.simplesdental.product.domain.model.field.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserContextRequest {

    @Schema(description = "User's unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Schema(description = "User's role", example = "ADMIN")
    private Role role;

}

