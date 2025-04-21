package com.simplesdental.product.autenticator;

import com.simplesdental.product.infrastructure.security.auth.JwtAuthentication;
import com.simplesdental.product.infrastructure.security.filter.RequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RequestFilterTest {

    @Mock
    private JwtAuthentication jwtAuthentication;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private RequestFilter requestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAuthenticateAndContinueFilterChain() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer valid.jwt.token");
        when(request.getServletPath()).thenReturn("/api/products");

        // When
        requestFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(jwtAuthentication).authenticate("Bearer valid.jwt.token");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldHandleExceptionDuringAuthentication() throws Exception {
        // Given
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(request.getRequestURI()).thenReturn("/api/products");
        when(request.getServletPath()).thenReturn("/api/products");

        doThrow(new RuntimeException("Token inválido"))
                .when(jwtAuthentication).authenticate(anyString());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // When
        requestFilter.doFilterInternal(request, response, filterChain);
        writer.flush();

        // Then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(stringWriter.toString().contains("Token inválido"));
    }

    @Test
    void shouldNotFilterLoginEndpoint() {
        // Given
        when(request.getServletPath()).thenReturn("/auth/login");

        // When
        boolean shouldNotFilter = requestFilter.shouldNotFilter(request);

        // Then
        assertTrue(shouldNotFilter);
    }

    @Test
    void shouldFilterOtherEndpoints() {
        // Given
        when(request.getServletPath()).thenReturn("/api/products");

        // When
        boolean shouldNotFilter = requestFilter.shouldNotFilter(request);

        // Then
        assertFalse(shouldNotFilter);
    }
}
