package com.core.microbill.billing.infrastructure.web.exception;

import com.core.microbill.billing.api.model.ErrorResponse;
import com.core.microbill.billing.domain.exception.AuthorizationException;
import com.core.microbill.billing.domain.exception.BusinessLogicException;
import com.core.microbill.billing.domain.exception.ResourceNotFoundException;
import com.core.microbill.billing.domain.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message("Acceso denegado")
            .error("Authorization Error")
            .status(HttpStatus.FORBIDDEN.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
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
        ErrorResponse error = new ErrorResponse()
            .message("Error interno del servidor")
            .error("Internal Error")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}