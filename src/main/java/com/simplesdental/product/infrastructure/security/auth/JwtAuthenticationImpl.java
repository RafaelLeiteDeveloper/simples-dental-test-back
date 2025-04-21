package com.simplesdental.product.infrastructure.security.auth;

import com.simplesdental.product.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class JwtAuthenticationImpl implements JwtAuthentication {

    private static final long EXPIRES_IN = 24L * 60L * 60L;
    private static final String ISSUER = "api-auth";
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public void authenticate(String jwtRequest) {
        try {
            Objects.requireNonNull(jwtRequest, "Token cannot be null.");
            String pureJwt = this.pureJwt(jwtRequest);
            Jwt decodedJwt = jwtDecoder.decode(pureJwt);
            this.ifExpiredThenThrow(decodedJwt);
            List<String> roles = decodedJwt.getClaim("roles");
            List<SimpleGrantedAuthority> authorities = this.toRoles(roles);
            var user = new UsernamePasswordAuthenticationToken(decodedJwt.getClaim("username"), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(user);
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            throw new JwtDecoderInitializationException(String.format("Error validating token: %s", e.getMessage()), e);
        }
    }

    @Override
    public String generateToken(User user) {
        List<String> rolesList = this.getAuthorities(user);
        JwtClaimsSet claims = getClaims(user, rolesList);
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public Long getUserIdFromJwt(String token) {
        Jwt decodedJwt = jwtDecoder.decode(this.pureJwt(token));
        this.ifExpiredThenThrow(decodedJwt);
        return decodedJwt.getClaim("id");
    }

    private String pureJwt(String jwt) {
        return jwt.trim().replace("Bearer", "");
    }

    private List<String> getAuthorities(UserDetails userDetails){
        return userDetails.getAuthorities()
                          .stream()
                          .map(GrantedAuthority::getAuthority)
                          .toList();
    }

    private JwtClaimsSet getClaims(User user, List<String> authorities){
        return JwtClaimsSet.builder()
                           .issuer(ISSUER)
                           .issuedAt(Instant.now())
                           .expiresAt(Instant.now().plusSeconds(EXPIRES_IN))
                           .claim("id", user.getId())
                           .claim("email", user.getEmail() )
                           .claim("roles", authorities)
                           .build();
    }

    private List<SimpleGrantedAuthority> toRoles(List<String> roles){
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    private void ifExpiredThenThrow(Jwt decodedJwt) {
        if (Objects.requireNonNull(decodedJwt.getExpiresAt()).isBefore(Instant.now())) {
            throw new JwtDecoderInitializationException("Token expirado.", new Exception());
        }
    }

}
