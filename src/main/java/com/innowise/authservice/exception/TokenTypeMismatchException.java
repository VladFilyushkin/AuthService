package com.innowise.authservice.exception;

public class TokenTypeMismatchException extends RuntimeException {

  public TokenTypeMismatchException(String type) {
    super("Invalid token type, expected: " + type);
  }
}
