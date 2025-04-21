package com.simplesdental.product.infrastructure.dto.response;

import com.simplesdental.product.domain.model.field.Role;
import lombok.Data;

@Data
public class UserRegisterResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
