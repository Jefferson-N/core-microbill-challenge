package com.core.microbill.authentication.infrastructure.web.exception;

import com.core.microbill.authentication.domain.exception.AuthenticationException;
import com.core.microbill.authentication.domain.exception.BusinessLogicException;
import com.core.microbill.authentication.domain.exception.ResourceNotFoundException;
import com.core.microbill.authentication.domain.exception.ValidationException;
import com.core.microbill.authentication.infrastructure.adapter.in.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message(ex.getMessage())
            .error("Validation Error")
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message(ex.getMessage())
            .error("Business Logic Error")
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AuthenticationException.class )
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message(ex.getMessage())
            .error("Authentication Error")
            .status(HttpStatus.UNAUTHORIZED.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler( BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleCredentialsException(BadCredentialsException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
                .message(ex.getMessage())
                .error("Credenciales invalidas")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(java.time.OffsetDateTime.now())
                .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message(ex.getMessage())
            .error("Resource Not Found")
            .status(HttpStatus.NOT_FOUND.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("handleGenericException ",ex);
        ErrorResponse error = new ErrorResponse()
            .message("Error interno del servidor")
            .error("Internal Error")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
