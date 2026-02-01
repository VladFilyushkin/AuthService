package com.innowise.authservice.exception;

public class ExpiredTokenException extends RuntimeException {

  public ExpiredTokenException() {
    super("Token expired");
  }
}
