package com.ky.demo_park_api.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ky.demo_park_api.exception.EmailUniqueViolationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EmailUniqueViolationException.class)
    public ResponseEntity<ErrorMessager> handleEmailUniqueViolationException(EmailUniqueViolationException ex, HttpServletRequest request) {
        log.error("Api Error", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessager(request, HttpStatus.CONFLICT, ex.getMessage()));
    }
    

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessager> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request, BindingResult result) {

        log.error("Api Error", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessager(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) inválidos", result));
    }
}
