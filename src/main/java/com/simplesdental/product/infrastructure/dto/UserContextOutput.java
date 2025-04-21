package com.simplesdental.product.infrastructure.dto;

import com.simplesdental.product.domain.model.field.Role;
import lombok.Data;

@Data
public class UserContextOutput {
    private Long id;
    private String email;
    private Role role;
}
