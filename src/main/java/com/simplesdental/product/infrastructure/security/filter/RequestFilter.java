package com.simplesdental.product.infrastructure.security.filter;

import com.simplesdental.product.infrastructure.security.auth.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtAuthentication jwtAuthentication;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) {
        try {
            String jwt = request.getHeader("Authorization");
            jwtAuthentication.authenticate(jwt);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            FilterResponseError
                    .builder()
                    .message(e.getMessage())
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build()
                    .getError(response);
        }
    }

    @Override
    public boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        return path.contains("/auth/login");
    }

}