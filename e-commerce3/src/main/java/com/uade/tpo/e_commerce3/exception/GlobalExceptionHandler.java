package com.uade.tpo.e_commerce3.exception;

import com.uade.tpo.e_commerce3.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ArgumentInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleArgumentInvalid(
            ArgumentInvalidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "ARGUMENT_INVALID", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Error de validacion en la solicitud");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "GENERIC_ERROR",
                "Ocurrio un error inesperado"
        );
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String code, String message) {
        ErrorResponseDTO body = new ErrorResponseDTO(status.value(), code, message);
        return new ResponseEntity<>(body, status);
    }
}
