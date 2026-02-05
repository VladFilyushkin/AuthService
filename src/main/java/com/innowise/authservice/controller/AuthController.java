package com.innowise.authservice.controller;

import com.innowise.authservice.dto.request.LoginRequest;
import com.innowise.authservice.dto.request.RefreshTokenRequest;
import com.innowise.authservice.dto.request.RegisterRequest;
import com.innowise.authservice.dto.response.TokenResponse;
import com.innowise.authservice.dto.response.ValidateTokenResponse;
import com.innowise.authservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(
      @RequestBody @Valid LoginRequest req
  ) {
    return ResponseEntity.ok(
        authenticationService.login(req));
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @RequestBody @Valid RegisterRequest request
  ) {
    authenticationService.register(request);
    return ResponseEntity.status(
        HttpStatus.CREATED).build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(
      @RequestBody @Valid RefreshTokenRequest request
  ) {
    return ResponseEntity.ok(
        authenticationService.refresh(request.getRefreshToken())
    );
  }

  @PostMapping("/validate")
  public ResponseEntity<ValidateTokenResponse> validate(
      @RequestHeader(value = "Authorization", required = false) String authHeader
  ) {
    return ResponseEntity.ok(
        authenticationService.validateAuthHeader(authHeader)
    );
  }
}

