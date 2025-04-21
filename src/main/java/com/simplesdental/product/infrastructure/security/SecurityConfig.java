package com.simplesdental.product.infrastructure.security;

import com.simplesdental.product.infrastructure.security.filter.RequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SecurityConfig {

    private final RequestFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.GET,"/api/categories").hasRole("USER");
                auth.requestMatchers(HttpMethod.GET,"/api/categories/{id}").hasRole("USER");
                auth.requestMatchers(HttpMethod.POST,"/api/categories").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.PUT,"/api/categories/{id}").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.DELETE,"/api/categories/{id}").hasRole("ADMIN");

                auth.requestMatchers(HttpMethod.GET,"/api/v1/products").hasRole("USER");
                auth.requestMatchers(HttpMethod.GET,"/api/v1/products/{id}").hasRole("USER");
                auth.requestMatchers(HttpMethod.POST,"/api/v1/products").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.PUT,"/api/v1/products/{id}").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.DELETE,"/api/v1/products/{id}").hasRole("ADMIN");

                auth.requestMatchers(HttpMethod.GET,"/api/v2/products").hasRole("USER");
                auth.requestMatchers(HttpMethod.GET,"/api/v2/products/{id}").hasRole("USER");
                auth.requestMatchers(HttpMethod.POST,"/api/v2/products").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.PUT,"/api/v2/products/{id}").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.DELETE,"/api/v2/products/{id}").hasRole("ADMIN");

                auth.requestMatchers(HttpMethod.POST,"/auth/login").permitAll();
                auth.requestMatchers(HttpMethod.POST,"/auth/register").hasRole("ADMIN");
                auth.requestMatchers(HttpMethod.GET,"/auth/context").hasRole("USER");
                auth.requestMatchers(HttpMethod.PUT,"/auth/users/password").hasRole("USER");

                auth.anyRequest().authenticated();
            })
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
