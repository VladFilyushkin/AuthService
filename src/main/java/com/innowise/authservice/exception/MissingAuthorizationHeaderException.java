package com.innowise.authservice.exception;

public class MissingAuthorizationHeaderException extends RuntimeException {

  public MissingAuthorizationHeaderException() {
    super("Missing Authorization header");
  }
}
