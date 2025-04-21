package com.simplesdental.product.infrastructure.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.Instant;

@Builder
public record ErrorResponse(String timestamp, Integer status, String error, String path) {

    public static ResponseEntity<ErrorResponse> response (String message, HttpStatus status, String uri){
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                           .timestamp(Instant.now().toString())
                           .error(message)
                           .status(status.value())
                           .path(uri)
                           .build()
                );
    }
}