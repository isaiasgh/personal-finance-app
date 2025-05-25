package com.isaias.finance.category_service.config;

import com.isaias.finance.category_service.data.dto.ErrorResponseDTO;
import com.isaias.finance.category_service.domain.exception.CategoryAlreadyExistsException;
import com.isaias.finance.category_service.domain.exception.CategoryNotBelongsToUserException;
import com.isaias.finance.category_service.domain.exception.CategoryNotFoundException;
import com.isaias.finance.category_service.domain.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors (MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity <> (errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryAlreadyExistsException (CategoryAlreadyExistsException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler (CategoryNotBelongsToUserException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryNotBelongsToUserException (CategoryNotBelongsToUserException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler (CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryNotFoundException (CategoryNotFoundException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException (UnauthorizedException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    private ErrorResponseDTO buildResponse (String message) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage(message);
        return error;
    }
}