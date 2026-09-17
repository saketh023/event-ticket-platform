package com.sakeva.tickets.controllers;

import com.sakeva.tickets.domain.dtos.ErrorDto;
import com.sakeva.tickets.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
    log.error("Caught UserNotFoundException", ex);
    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("User not found");
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.error("Caught MethodArgumentNotValidException", ex);
    ErrorDto errorDto = new ErrorDto();
    BindingResult bindingResult = ex.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    String errorMessage =
        fieldErrors.stream()
            .findFirst()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .orElse("Validation error occurred");
    errorDto.setError(errorMessage);
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
    log.error("Caught a ConstraintViolationException", ex);
    ErrorDto errorDto = new ErrorDto();
    String errorMessage =
        ex.getConstraintViolations().stream()
            .findFirst()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .orElse("Constraint violation occurred");
    errorDto.setError(errorMessage);
    return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception exception) {
    log.error("Caught exception", exception);
    ErrorDto errorDto = new ErrorDto();
    errorDto.setError("An unknown error occurred");
    return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
