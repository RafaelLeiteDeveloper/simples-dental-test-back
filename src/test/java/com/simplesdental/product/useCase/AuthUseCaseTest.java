package com.simplesdental.product.useCase;

import com.simplesdental.product.application.usecase.AuthUseCase;
import com.simplesdental.product.domain.exception.InvalidPasswordException;
import com.simplesdental.product.domain.model.User;
import com.simplesdental.product.domain.model.field.Role;
import com.simplesdental.product.infrastructure.dto.*;
import com.simplesdental.product.infrastructure.dto.request.*;
import com.simplesdental.product.infrastructure.mapper.UserMapper;
import com.simplesdental.product.infrastructure.repository.UserRepository;
import com.simplesdental.product.infrastructure.security.auth.JwtAuthentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthUseCaseTest {

    @InjectMocks
    private AuthUseCase authUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtAuthentication jwtAuthentication;

    private final String email = "user@email.com";
    private final String rawPassword = "password123";
    private final String encodedPassword = "encodedPassword123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        var request = new UserRegisterRequest();
        request.setName("User");
        request.setEmail(email);
        request.setPassword(rawPassword);
        request.setRole(Role.USER);

        var user = new User();
        var savedUser = new User();
        savedUser.setId(1L);
        var output = new UserOutput();

        when(userMapper.toEntity(request)).thenReturn(user);
        when(encoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toOutput(savedUser)).thenReturn(output);

        // Act
        UserOutput result = authUseCase.createUser(request);

        // Assert
        verify(userRepository).existsByNameThenThrow("User", null);
        verify(userRepository).existsByEmailThenThrow(email, null);
        verify(userMapper).toEntity(request);
        verify(userRepository).save(user);
        verify(userMapper).toOutput(savedUser);
        assertEquals(output, result);
    }

    @Test
    void shouldReturnUserContextOutputById() {
        // Arrange
        Long id = 1L;
        User user = new User();
        UserContextOutput output = new UserContextOutput();
        output.setId(id);
        output.setEmail(email);
        output.setRole(Role.USER);

        when(userRepository.getByIdOrElseThrow(id)).thenReturn(user);
        when(userMapper.toContextOutput(user)).thenReturn(output);

        // Act
        UserContextOutput result = authUseCase.getUserById(id);

        // Assert
        assertEquals(output, result);
        verify(userRepository).getByIdOrElseThrow(id);
        verify(userMapper).toContextOutput(user);
    }

    @Test
    void shouldUpdatePasswordById() {
        // Arrange
        String token = "Bearer abc.def.ghi";
        Long userId = 1L;
        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setPassword(rawPassword);

        User user = new User();

        when(jwtAuthentication.getUserIdFromJwt(token)).thenReturn(userId);
        when(userRepository.getByIdOrElseThrow(userId)).thenReturn(user);
        when(encoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Act
        authUseCase.updatePasswordById(token, request);

        // Assert
        verify(userRepository).save(user);
        assertEquals(encodedPassword, user.getPassword());
    }

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(rawPassword);

        User user = new User();
        user.setId(1L);
        user.setPassword(encodedPassword);

        when(userRepository.getUserByEmailOrElseThrow(email)).thenReturn(user);
        when(encoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtAuthentication.generateToken(user)).thenReturn("token.jwt.123");

        // Act
        UserLoginOutput result = authUseCase.getTokenByLogin(request);

        // Assert
        assertEquals("token.jwt.123", result.getToken());
    }

    @Test
    void shouldThrowInvalidPasswordException() {
        // Arrange
        UserLoginRequest request = new UserLoginRequest();
        request.setPassword( "wrongPass");
        request.setEmail(email);

        User user = new User();
        user.setPassword(encodedPassword);

        when(userRepository.getUserByEmailOrElseThrow(email)).thenReturn(user);
        when(encoder.matches("wrongPass", encodedPassword)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> {
            authUseCase.getTokenByLogin(request);
        });
    }
}