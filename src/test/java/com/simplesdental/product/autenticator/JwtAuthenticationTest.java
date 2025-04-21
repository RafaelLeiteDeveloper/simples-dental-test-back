package com.simplesdental.product.autenticator;

import com.simplesdental.product.domain.model.User;
import com.simplesdental.product.domain.model.field.Role;
import com.simplesdental.product.infrastructure.security.auth.JwtAuthenticationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationTest {
    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private JwtAuthenticationImpl jwtAuthentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAuthenticateValidToken() {
        String token = "Bearer valid.token.here";
        Jwt mockJwt = mock(Jwt.class);

        when(mockJwt.getExpiresAt()).thenReturn(Instant.now().plusSeconds(3600));
        when(mockJwt.getClaim("roles")).thenReturn(List.of("ROLE_USER"));
        when(mockJwt.getClaim("username")).thenReturn("user@example.com");
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);

        jwtAuthentication.authenticate(token);

        var auth = (UsernamePasswordAuthenticationToken)
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        assertEquals("user@example.com", auth.getPrincipal());
        assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void shouldThrowWhenTokenIsNull() {
        JwtDecoderInitializationException exception = assertThrows(
                JwtDecoderInitializationException.class,
                () -> jwtAuthentication.authenticate(null)
        );

        assertTrue(exception.getCause() instanceof NullPointerException);
        assertEquals("Token cannot be null.", exception.getCause().getMessage());
    }

    @Test
    void shouldThrowWhenTokenIsExpired() {
        Jwt expiredJwt = mock(Jwt.class);
        when(expiredJwt.getExpiresAt()).thenReturn(Instant.now().minusSeconds(10));
        when(jwtDecoder.decode(anyString())).thenReturn(expiredJwt);

        Exception exception = assertThrows(JwtDecoderInitializationException.class, () -> {
            jwtAuthentication.authenticate("Bearer expired.token");
        });

        assertTrue(exception.getMessage().contains("Token expirado"));
    }

    @Test
    void shouldGenerateToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("admin@simplesdental.com");
        user.setPassword("encrypted");
        user.setRole(Role.ADMIN);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked.jwt.token");
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        String token = jwtAuthentication.generateToken(user);

        assertEquals("mocked.jwt.token", token);
        verify(jwtEncoder).encode(any());
    }

    @Test
    void shouldGetUserIdFromValidJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getExpiresAt()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwt.getClaim("id")).thenReturn(123L);
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        Long userId = jwtAuthentication.getUserIdFromJwt("Bearer valid.token");

        assertEquals(123L, userId);
    }
}

