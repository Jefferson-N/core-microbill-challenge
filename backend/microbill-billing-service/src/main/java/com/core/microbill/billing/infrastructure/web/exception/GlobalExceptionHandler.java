package com.core.microbill.billing.infrastructure.web.exception;

import com.core.microbill.billing.api.model.ErrorResponse;
import com.core.microbill.billing.domain.exception.AuthorizationException;
import com.core.microbill.billing.domain.exception.BusinessLogicException;
import com.core.microbill.billing.domain.exception.ResourceNotFoundException;
import com.core.microbill.billing.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message(ex.getMessage())
            .error("Error de Validación")
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message(ex.getMessage())
            .error("Error de Lógica de Negocio")
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message("Acceso denegado")
            .error("Error de Autorización")
            .status(HttpStatus.FORBIDDEN.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse()
            .message(ex.getMessage())
            .error("Recurso No Encontrado")
            .status(HttpStatus.NOT_FOUND.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ErrorResponse error = new ErrorResponse()
            .message("Errores de validación: " + errors)
            .error("Error de Validación")
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Runtime exception occurred: ", ex);
        ErrorResponse error = new ErrorResponse()
            .message("Su operación no pudo ser procesada, consulte con el administrador")
            .error("Error Interno")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unhandled exception occurred: ", ex);
        ErrorResponse error = new ErrorResponse()
            .message("Su operación no pudo ser procesada, consulte con el administrador")
            .error("Error Interno")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(java.time.OffsetDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}