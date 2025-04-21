package com.simplesdental.product.infrastructure.mapper;

import com.simplesdental.product.domain.model.User;
import com.simplesdental.product.infrastructure.dto.UserContextOutput;
import com.simplesdental.product.infrastructure.dto.UserLoginOutput;
import com.simplesdental.product.infrastructure.dto.UserOutput;
import com.simplesdental.product.infrastructure.dto.request.UserContextRequest;
import com.simplesdental.product.infrastructure.dto.request.UserRegisterRequest;
import com.simplesdental.product.infrastructure.dto.response.UserContextResponse;
import com.simplesdental.product.infrastructure.dto.response.UserLoginResponse;
import com.simplesdental.product.infrastructure.dto.response.UserRegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    User toEntity(UserRegisterRequest request);
    
    UserOutput toOutput(User savedUser);

    UserRegisterResponse toResponse(UserOutput registerRequest);

    UserContextOutput toContextOutput(User user);

    UserContextResponse toContextResponse(UserContextOutput userContextOutput);

    UserLoginResponse toLoginResponse(UserLoginOutput output);
}
