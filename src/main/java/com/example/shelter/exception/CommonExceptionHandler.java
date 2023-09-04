package com.example.shelter.exception;

import com.example.shelter.exception.badinput.BadInputException;
import com.example.shelter.exception.duplicate.DuplicateException;
import com.example.shelter.exception.notfound.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> DuplicateExceptionHandler(DuplicateException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<ErrorResponse> BadInputExceptionHandler(BadInputException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            errorMap.put("message", constraintViolation.getMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("000")
                .message("유효하지 않은 값입니다.")
                .errors(errorMap)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
            FieldError fieldError = (FieldError) objectError;
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("000")
                .message("유효하지 않은 값입니다.")
                .errors(errorMap)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
