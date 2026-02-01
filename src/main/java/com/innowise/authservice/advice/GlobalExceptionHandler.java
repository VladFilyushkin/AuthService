package com.innowise.authservice.advice;

import com.innowise.authservice.exception.InvalidCredentialsException;
import com.innowise.authservice.exception.InvalidTokenException;
import com.innowise.authservice.exception.MissingAuthorizationHeaderException;
import com.innowise.authservice.exception.TokenTypeMismatchException;
import com.innowise.authservice.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<String> handleInvalidCredentials(
      InvalidCredentialsException ex
  ) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ex.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handleUserAlreadyExists(
      UserAlreadyExistsException ex
  ) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<String> handleInvalidToken(
      InvalidTokenException ex
  ) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ex.getMessage());
  }

  @ExceptionHandler(TokenTypeMismatchException.class)
  public ResponseEntity<String> handleTokenTypeMismatch(
      TokenTypeMismatchException ex
  ) {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ex.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body("Invalid login or password");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneric(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Unexpected error");
  }

  @ExceptionHandler(MissingAuthorizationHeaderException.class)
  public ResponseEntity<String> handleMissingAuthorizationHeader(
      MissingAuthorizationHeaderException ex
  ) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ex.getMessage());
  }
}
