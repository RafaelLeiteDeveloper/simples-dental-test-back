package com.simplesdental.product.application.port.in;

import com.simplesdental.product.infrastructure.dto.UserContextOutput;
import com.simplesdental.product.infrastructure.dto.UserLoginOutput;
import com.simplesdental.product.infrastructure.dto.UserOutput;
import com.simplesdental.product.infrastructure.dto.request.PasswordUpdateRequest;
import com.simplesdental.product.infrastructure.dto.request.UserLoginRequest;
import com.simplesdental.product.infrastructure.dto.request.UserRegisterRequest;

public interface ManageUserUseCase {
    UserOutput createUser(UserRegisterRequest registerRequest);
    void updatePasswordById(String token, PasswordUpdateRequest passwordUpdateRequest);
    UserContextOutput getUserById(Long id);
    UserLoginOutput getTokenByLogin(UserLoginRequest userLoginRequest);
}
