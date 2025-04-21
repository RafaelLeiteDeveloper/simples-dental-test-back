package com.simplesdental.product.domain.exception.globalHandler;

import com.simplesdental.product.domain.exception.InvalidPasswordException;
import com.simplesdental.product.infrastructure.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, WebRequest request, HttpServletRequest s) {
        return ErrorResponse.response(ex.getReason(), HttpStatus.valueOf(ex.getStatusCode().value()) , s.getRequestURI());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> DuplicateKeyException(HttpServletRequest s, DuplicateKeyException e) {
        return ErrorResponse.response(e.getMessage(), HttpStatus.CONFLICT, s.getRequestURI());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> MissingRequestHeaderException(HttpServletRequest s, MissingRequestHeaderException e) {
        return ErrorResponse.response(e.getMessage(), HttpStatus.BAD_REQUEST, s.getRequestURI());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> InvalidPasswordException(HttpServletRequest s, InvalidPasswordException e) {
        return ErrorResponse.response(e.getMessage(), HttpStatus.BAD_REQUEST, s.getRequestURI());
    }

}