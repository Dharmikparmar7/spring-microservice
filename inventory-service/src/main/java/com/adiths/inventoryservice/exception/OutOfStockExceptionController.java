package com.adiths.inventoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class OutOfStockExceptionController {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<String> exception(ConstraintViolationException constraintViolationException){
        return new ResponseEntity<String>("Out of Stock", HttpStatus.NOT_FOUND);
    }
}
