package com.adiths.orderservice.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception exception){
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
