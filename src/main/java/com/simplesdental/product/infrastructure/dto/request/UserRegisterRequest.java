package com.simplesdental.product.infrastructure.dto.request;

import com.simplesdental.product.domain.model.field.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @Schema(description = "Full name of the user", example = "John Doe")
    @NotNull(message = "Name must not be null")
    private String name;

    @Schema(description = "User email address (must be unique and valid)", example = "john.doe@example.com", required = true)
    @NotNull(message = "Email must not be null")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(description = "User password", example = "StrongPassword123")
    @NotNull(message = "Password must not be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Schema(description = "User's role", example = "ADMIN")
    @NotNull(message = "Role must not be null")
    private Role role;

}
