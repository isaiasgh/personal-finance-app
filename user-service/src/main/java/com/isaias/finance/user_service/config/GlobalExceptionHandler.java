package com.isaias.finance.user_service.config;

import com.isaias.finance.user_service.data.dto.ErrorResponseDTO;
import com.isaias.finance.user_service.domain.exception.InvalidPasswordException;
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
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException (BadCredentialsException ex) {
        return new ResponseEntity <> (buildResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler (UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException (UserAlreadyExistsException ex) {
        return new ResponseEntity <> (buildResponse(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler (UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException (UsernameNotFoundException ex) {
        return new ResponseEntity <> (buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException (UserNotFoundException ex) {
        return new ResponseEntity <> (buildResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(buildResponse("You do not have permission to perform this action."), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPasswordException(InvalidPasswordException ex) {
        return new ResponseEntity<>(buildResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return new ResponseEntity<>(buildResponse("An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponseDTO buildResponse (String message) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage(message);
        return error;
    }
}