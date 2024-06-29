package com.example.course.exception;

import com.example.course.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        ErrorResponse errorResponse = new ErrorResponse("Malformed JSON request or required fields are missing");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
