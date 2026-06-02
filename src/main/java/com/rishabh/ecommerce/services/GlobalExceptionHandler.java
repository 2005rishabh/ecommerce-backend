package com.rishabh.ecommerce.services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.catalina.loader.ResourceEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rishabh.ecommerce.error.ProductNotFoundException;

@RestControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException productNotFoundException) {
        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND,
            productNotFoundException.getMessage(), 
            LocalDateTime.now(), 
            Collections.singletonList("The requested resource cannot be found")
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error ->  error.getField() + ":" + error.getDefaultMessage())
        .toList();

        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,
            "Validation Failed",
            LocalDateTime.now(), 
            errors
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleProductNotFound(Exception ex) {
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected internal server error has occured",
            LocalDateTime.now(), 
            Collections.singletonList(ex.getMessage())
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
