package com.simplesdental.product.application.usecase;

import com.simplesdental.product.application.port.in.ManageUserUseCase;
import com.simplesdental.product.domain.exception.InvalidPasswordException;
import com.simplesdental.product.domain.model.User;
import com.simplesdental.product.infrastructure.dto.UserContextOutput;
import com.simplesdental.product.infrastructure.dto.UserLoginOutput;
import com.simplesdental.product.infrastructure.dto.UserOutput;
import com.simplesdental.product.infrastructure.dto.request.PasswordUpdateRequest;
import com.simplesdental.product.infrastructure.dto.request.UserLoginRequest;
import com.simplesdental.product.infrastructure.dto.request.UserRegisterRequest;
import com.simplesdental.product.infrastructure.mapper.UserMapper;
import com.simplesdental.product.infrastructure.repository.UserRepository;
import com.simplesdental.product.infrastructure.security.auth.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@CacheConfig(cacheNames = "customerCache")
@RequiredArgsConstructor
public class AuthUseCase implements ManageUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final JwtAuthentication jwtAuthentication;

    @Override
    public UserOutput createUser(UserRegisterRequest registerRequest) {
        log.info("Creating user with email: {}", registerRequest.getEmail());
        userRepository.existsByNameThenThrow(registerRequest.getName(), null);
        userRepository.existsByEmailThenThrow(registerRequest.getEmail(), null);
        User user = userMapper.toEntity(registerRequest);
        user.encryptPassword(encoder.encode(registerRequest.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return userMapper.toOutput(savedUser);
    }

    @Override
    @Cacheable(value = "userContext", key = "#id")
    public UserContextOutput getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.getByIdOrElseThrow(id);
        return userMapper.toContextOutput(user);
    }

    @Override
    @CacheEvict(value = "userContext", key = "#id")
    public void updatePasswordById(String token, PasswordUpdateRequest passwordUpdateRequest) {
        Long id = jwtAuthentication.getUserIdFromJwt(token);
        log.info("Get user with id: {}", id);
        User user = userRepository.getByIdOrElseThrow(id);
        log.info("Updating password for user id: {}", id);
        user.setPassword(encoder.encode(passwordUpdateRequest.getPassword()));
        userRepository.save(user);
        log.info("Password updated successfully for user ID: {}", id);
    }

    @Override
    public UserLoginOutput getTokenByLogin(UserLoginRequest userLoginRequest) {
        log.info("Attempting to authenticate user with email: {}", userLoginRequest.getEmail());
        User user = userRepository.getUserByEmailOrElseThrow(userLoginRequest.getEmail());
        this.isNotValidPasswordThenThrow(userLoginRequest.getPassword(), user.getPassword());
        log.info("Authentication successful for user ID: {}", user.getId());
        return new UserLoginOutput(jwtAuthentication.generateToken(user));
    }

    private void isNotValidPasswordThenThrow(String password, String encodedPassword) {
        log.info("Verifying password for user authentication");
        if (!encoder.matches(password, encodedPassword)) {
            log.warn("Invalid password attempt for user");
            throw new InvalidPasswordException("Invalid password");
        }
    }

}
