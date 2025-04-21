package com.simplesdental.product.infrastructure.security.auth;

import com.simplesdental.product.domain.model.User;

public interface JwtAuthentication {
    void authenticate(String jwt);
    String generateToken(User user);
    Long getUserIdFromJwt(String token);
}
