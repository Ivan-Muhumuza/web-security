package com.example.secureapi.exception;

import com.example.secureapi.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Global exception handler for REST controllers
@RestControllerAdvice
public class CustomExceptionHandler {

    // Handle validation exceptions thrown during request processing
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Use a set to collect unique error messages
        Set<String> errorMessages = new HashSet<>();

        // Iterate through all validation errors
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // Extract field name and error message, then add to set
            String errorMessage = ((FieldError) error).getField() + " " + error.getDefaultMessage();
            errorMessages.add(errorMessage);
        });

        // Create an ErrorResponse object with current timestamp, status, and error details
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                new ArrayList<>(errorMessages) // Convert set of error messages to list
        );

        // Return the error response with HTTP 400 Bad Request status
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

