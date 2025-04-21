package com.simplesdental.product.infrastructure.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginRequest {

    @Schema(description = "User email address", example = "user@example.com")
    @NotNull(message = "Email must not be null")
    private String email;

    @Schema(description = "User password", example = "securePassword123")
    @NotNull(message = "Password must not be null")
    private String password;

}
