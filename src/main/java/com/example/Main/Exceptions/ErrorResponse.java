package com.example.Main.Exceptions;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path; 
    private LocalDateTime timestamp;
}