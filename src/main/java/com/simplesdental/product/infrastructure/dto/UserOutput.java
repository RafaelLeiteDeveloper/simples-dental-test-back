package com.simplesdental.product.infrastructure.dto;

import com.simplesdental.product.domain.model.field.Role;
import lombok.Data;

@Data
public class UserOutput {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
}
