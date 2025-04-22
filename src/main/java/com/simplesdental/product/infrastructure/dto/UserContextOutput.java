package com.simplesdental.product.infrastructure.dto;

import com.simplesdental.product.domain.model.field.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserContextOutput implements Serializable {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
