package com.simplesdental.product.infrastructure.controller;

import com.simplesdental.product.application.usecase.AuthUseCase;
import com.simplesdental.product.infrastructure.dto.request.UserLoginRequest;
import com.simplesdental.product.infrastructure.dto.request.UserRegisterRequest;
import com.simplesdental.product.infrastructure.dto.request.PasswordUpdateRequest;
import com.simplesdental.product.infrastructure.dto.response.UserContextResponse;
import com.simplesdental.product.infrastructure.dto.response.UserLoginResponse;
import com.simplesdental.product.infrastructure.dto.response.UserRegisterResponse;
import com.simplesdental.product.infrastructure.mapper.UserMapper;
import com.simplesdental.product.infrastructure.security.auth.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints related to user authentication and context retrieval")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final UserMapper userMapper;
    private final JwtAuthentication jwtAuthentication;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user using email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        log.info("Attempting login for email: {}", userLoginRequest.getEmail());
        return ResponseEntity.ok(userMapper.toLoginResponse(authUseCase.getTokenByLogin(userLoginRequest)));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user if registration is allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        log.info("Registering new user with email: {}", userRegisterRequest.getEmail());
        return ResponseEntity.status(201).body(userMapper.toResponse(authUseCase.createUser(userRegisterRequest)));
    }

    @GetMapping("/context")
    @Operation(summary = "Get authenticated user context", description = "Returns the authenticated user's ID, email, and role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User context retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content)
    })
    public ResponseEntity<UserContextResponse> getContext(@RequestHeader("Authorization") String token) {
        log.info("Fetching authenticated user context from token");
        return ResponseEntity.ok(userMapper.toContextResponse(this.authUseCase.getUserById(jwtAuthentication.getUserIdFromJwt(token))));
    }

    @PutMapping("/users/password")
    @Operation(summary = "Update password", description = "Updates the password of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content)
    })
    public ResponseEntity<Void> updatePassword(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest
    ) {
        authUseCase.updatePasswordById(token, passwordUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
