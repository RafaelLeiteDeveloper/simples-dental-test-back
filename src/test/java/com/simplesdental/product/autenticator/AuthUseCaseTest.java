package com.simplesdental.product.autenticator;

import com.simplesdental.product.application.usecase.AuthUseCase;
import com.simplesdental.product.domain.exception.InvalidPasswordException;
import com.simplesdental.product.domain.model.User;
import com.simplesdental.product.domain.model.field.Role;
import com.simplesdental.product.infrastructure.dto.UserContextOutput;
import com.simplesdental.product.infrastructure.dto.UserLoginOutput;
import com.simplesdental.product.infrastructure.dto.UserOutput;
import com.simplesdental.product.infrastructure.dto.request.PasswordUpdateRequest;
import com.simplesdental.product.infrastructure.dto.request.UserLoginRequest;
import com.simplesdental.product.infrastructure.dto.request.UserRegisterRequest;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("Test");
        request.setEmail("test@email.com");
        request.setPassword("12345678");
        request.setRole(Role.USER);

        User user = new User();
        User savedUser = new User();
        savedUser.setId(1L);
        UserOutput output = new UserOutput();

        when(userMapper.toEntity(request)).thenReturn(user);
        when(encoder.encode("12345678")).thenReturn("encodedPwd");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toOutput(savedUser)).thenReturn(output);

        // When
        UserOutput result = authUseCase.createUser(request);

        // Then
        verify(userRepository).existsByNameThenThrow("Test", null);
        verify(userRepository).existsByEmailThenThrow("test@email.com", null);
        verify(userMapper).toEntity(request);
        verify(userRepository).save(user);
        verify(userMapper).toOutput(savedUser);
        assertEquals(output, result);
    }

    @Test
    void shouldReturnUserContextOutputById() {
        // Given
        User user = new User();
        user.setId(1L);

        UserContextOutput contextOutput = new UserContextOutput();
        contextOutput.setId(1L);
        contextOutput.setEmail("test@email.com");
        contextOutput.setRole(Role.USER);

        when(userRepository.getByIdOrElseThrow(1L)).thenReturn(user);
        when(userMapper.toContextOutput(user)).thenReturn(contextOutput);

        // When
        UserContextOutput result = authUseCase.getUserById(1L);

        // Then
        assertEquals(contextOutput, result);
        verify(userRepository).getByIdOrElseThrow(1L);
        verify(userMapper).toContextOutput(user);
    }

    @Test
    void shouldUpdatePasswordById() {
        // Given
        String token = "Bearer token";
        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setPassword("newpass");

        User user = new User();
        user.setId(1L);

        when(jwtAuthentication.getUserIdFromJwt(token)).thenReturn(1L);
        when(userRepository.getByIdOrElseThrow(1L)).thenReturn(user);
        when(encoder.encode("newpass")).thenReturn("encoded");

        // When
        authUseCase.updatePasswordById(token, request);

        // Then
        verify(userRepository).save(user);
        assertEquals("encoded", user.getPassword());
    }

    @Test
    void shouldLoginSuccessfully() {
        // Given
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@email.com");
        request.setPassword("123456");

        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("encodedPwd");

        when(userRepository.getUserByEmailOrElseThrow("test@email.com")).thenReturn(user);
        when(encoder.matches("123456", "encodedPwd")).thenReturn(true);
        when(jwtAuthentication.generateToken(user)).thenReturn("mocked.jwt.token");

        // When
        UserLoginOutput result = authUseCase.getTokenByLogin(request);

        // Then
        assertEquals("mocked.jwt.token", result.getToken());
        verify(jwtAuthentication).generateToken(user);
    }

    @Test
    void shouldThrowInvalidPasswordExceptionOnWrongPassword() {
        // Given
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@email.com");
        request.setPassword("wrongpass");

        User user = new User();
        user.setPassword("encodedPwd");

        when(userRepository.getUserByEmailOrElseThrow("test@email.com")).thenReturn(user);
        when(encoder.matches("wrongpass", "encodedPwd")).thenReturn(false);

        // When / Then
        assertThrows(InvalidPasswordException.class, () -> authUseCase.getTokenByLogin(request));
        verify(userRepository).getUserByEmailOrElseThrow("test@email.com");
        verify(encoder).matches("wrongpass", "encodedPwd");
    }
}
