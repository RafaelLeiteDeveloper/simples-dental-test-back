package com.simplesdental.product.infrastructure.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordUpdateRequest {

    @NotNull(message = "Password cannot be null.")
    @Schema(description = "User password", example = "StrongPassword123")
    private String password;

}
