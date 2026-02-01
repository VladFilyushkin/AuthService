package com.innowise.authservice.service;

import com.innowise.authservice.dto.request.LoginRequest;
import com.innowise.authservice.dto.request.RegisterRequest;
import com.innowise.authservice.dto.response.TokenResponse;
import com.innowise.authservice.dto.response.ValidateTokenResponse;
import com.innowise.authservice.entity.Role;
import com.innowise.authservice.entity.User;
import com.innowise.authservice.exception.InvalidCredentialsException;
import com.innowise.authservice.exception.MissingAuthorizationHeaderException;
import com.innowise.authservice.exception.TokenTypeMismatchException;
import com.innowise.authservice.exception.UserAlreadyExistsException;
import com.innowise.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CustomUserDetailService customUserDetailService;


  public TokenResponse login(LoginRequest req) {
    try {
      var auth = authManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.getLogin(), req.getPassword())
      );

      var user = (User) auth.getPrincipal();

      return new TokenResponse(
          jwtService.generateAccessToken(user),
          jwtService.generateRefreshToken(user)
      );

    } catch (BadCredentialsException e) {
      throw new InvalidCredentialsException();
    }
  }

  public void register(RegisterRequest request) {

    if (userRepository.findByLogin(request.getLogin()).isPresent()) {
      throw new UserAlreadyExistsException(request.getLogin());
    }

    User user = new User();
    user.setLogin(request.getLogin());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.USER);

    userRepository.save(user);
  }

  public TokenResponse refresh(String refreshToken) {

    var claims = jwtService.extractAllClaims(refreshToken);

    if (!"REFRESH".equals(claims.get("type", String.class))) {
      throw new TokenTypeMismatchException("REFRESH");
    }

    String login = claims.getSubject();

    User user = (User) customUserDetailService.loadUserByUsername(login);

    return new TokenResponse(
        jwtService.generateAccessToken(user),
        jwtService.generateRefreshToken(user)
    );
  }

  public ValidateTokenResponse validate(String token) {
    var claims = jwtService.extractAllClaims(token);
    return new ValidateTokenResponse(
        true, claims.get("role", String.class)
    );
  }

  public ValidateTokenResponse validateAuthHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new MissingAuthorizationHeaderException();
    }

    String token = authHeader.substring(7);
    return validate(token);
  }
}


