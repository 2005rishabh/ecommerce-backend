package com.rishabh.ecommerce.error;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ApiError {
    private HttpStatus status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errors;
}
