package com.isaias.finance.user_service.config;

import com.isaias.finance.user_service.domain.exception.UserAlreadyExistsException;
import com.isaias.finance.user_service.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler (BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException (BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return new ResponseEntity <> (error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler (UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException (UserAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return new ResponseEntity <> (error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler (UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException (UsernameNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return new ResponseEntity <> (error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException (UserNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return new ResponseEntity <> (error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "You do not have permission to perform this action.");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "An unexpected error occurred.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}