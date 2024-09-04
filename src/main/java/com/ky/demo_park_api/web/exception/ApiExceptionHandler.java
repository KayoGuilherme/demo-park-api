package com.ky.demo_park_api.web.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ky.demo_park_api.exception.CodigoUniqueViolationException;
import com.ky.demo_park_api.exception.EmailUniqueViolationException;
import com.ky.demo_park_api.exception.EntityNotFoundException;
import com.ky.demo_park_api.exception.PasswordInvalidException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessager> handleMethodArgumentPasswordInvalidException(PasswordInvalidException ex,
            HttpServletRequest request) {
        log.error("Api Error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessager(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessager> handleMethodArgumentNotUserException(EntityNotFoundException ex,
            HttpServletRequest request) {
        log.error("Api Error", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessager(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(EmailUniqueViolationException.class)
    public ResponseEntity<ErrorMessager> handleUniqueViolationException(EmailUniqueViolationException  ex,
            HttpServletRequest request) {
        log.error("Api Error", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessager(request, HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(CodigoUniqueViolationException.class)
    public ResponseEntity<ErrorMessager> handleCodigoUniqueViolationException(CodigoUniqueViolationException  ex,
            HttpServletRequest request) {
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessager> accessDeniedException(AccessDeniedException ex,
            HttpServletRequest request, BindingResult result) {

        log.error("Api Error", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessager(request, HttpStatus.FORBIDDEN, ex.getMessage()));
    }
}
